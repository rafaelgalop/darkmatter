package com.pug.darkmatter

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.pug.darkmatter.ecs.asset.MusicAsset
import com.pug.darkmatter.ecs.asset.ShaderProgramAsset
import com.pug.darkmatter.ecs.asset.TextureAsset
import com.pug.darkmatter.ecs.asset.TextureAtlasAsset
import com.pug.darkmatter.ecs.audio.AudioService
import com.pug.darkmatter.ecs.audio.DefaultAudioService
import com.pug.darkmatter.ecs.system.*
import com.pug.darkmatter.event.GameEventManager
import com.pug.darkmatter.file.DARK_MATTER_PREFERENCES_FILE
import com.pug.darkmatter.screen.DarkMatterScreen
import com.pug.darkmatter.screen.LoadingScreen
import ktx.app.KtxGame
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.log.Logger
import ktx.log.debug
import ktx.log.logger

const val UNIT_SCALE: Float = 1 / 16f
const val V_WIDTH = 9
const val V_HEIGHT = 16
const val V_WIDTH_PIXELS = 135
const val V_HEIGHT_PIXELS = 240
private val LOG: Logger = logger<DarkMatter>()

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms.  */
class DarkMatter : KtxGame<DarkMatterScreen>() {
    val gameViewport = FitViewport(V_WIDTH.toFloat(), V_HEIGHT.toFloat())
    val batch: Batch by lazy { SpriteBatch() }
    val gameEventManager = GameEventManager()
    val assets: AssetStorage by lazy {
        KtxAsync.initiate()
        AssetStorage()
    }
    //    Old way of loading atlas and assets, moved to a ktx storage system on loading screen
    //    val graphicsAtlas by lazy { TextureAtlas(Gdx.files.internal("graphics/graphics.atlas")) }
    //    val backgroundTexture by lazy { Texture(Gdx.files.internal("graphics/background.png")) }

    val audioService: AudioService by lazy { DefaultAudioService(assets) }
    val preferences: Preferences by lazy { Gdx.app.getPreferences(DARK_MATTER_PREFERENCES_FILE) }

    val engine: Engine by lazy {
        PooledEngine().apply {
            var graphicsAtlas = assets[TextureAtlasAsset.GAME_GRAPHICS.descriptor]
            var backgroundTexture = assets[TextureAsset.BACKGROUND.descriptor]

            addSystem(PlayerInputSystem(gameViewport))
            addSystem(MoveSystem())
            addSystem(PowerUpSystem(gameEventManager, audioService))
            addSystem(DamageSystem(gameEventManager, audioService))
            addSystem(CameraShakeSystem(gameViewport.camera, gameEventManager))
            addSystem(
                PlayerAnimationSystem(
                    graphicsAtlas.findRegion("ship_base"),
                    graphicsAtlas.findRegion("ship_left"),
                    graphicsAtlas.findRegion("ship_right")
                )
            )
            addSystem(AttachSystem())
            addSystem(AnimationSystem(graphicsAtlas))
            addSystem(
                RenderSystem(
                    batch,
                    gameViewport,
                    uiViewport,
                    backgroundTexture,
                    gameEventManager,
                    assets[ShaderProgramAsset.OUTLINE.descriptor]
                )
            )
            addSystem(RemoveSystem())
            addSystem(DebugSystem())
        }
    }

    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG
        LOG.debug { "Create game instance" }
        addScreen(LoadingScreen(this))
        setScreen<LoadingScreen>()
    }

    override fun dispose() {
        super.dispose()
        LOG.debug { "Sprites in batch: ${(batch as SpriteBatch).maxSpritesInBatch}" }
        //checking reference count for the music assets
        MusicAsset.values().forEach {
            LOG.debug { "RefCount $it: ${assets.getReferenceCount(it.descriptor)}" }

        }
        batch.dispose()
        assets.dispose()
    }
}