package com.pug.darkmatter.screen

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.pug.darkmatter.DarkMatter
import com.pug.darkmatter.ecs.asset.ShaderProgramAsset
import com.pug.darkmatter.ecs.asset.SoundAsset
import com.pug.darkmatter.ecs.asset.TextureAsset
import com.pug.darkmatter.ecs.asset.TextureAtlasAsset
import com.pug.darkmatter.ui.LabelStyles
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.actors.plus
import ktx.actors.plusAssign
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf
import ktx.log.debug
import ktx.log.logger
import ktx.scene2d.*

private val LOG = logger<LoadingScreen>()
private const val ELEMENT_PADDING = 7f

class LoadingScreen(game: DarkMatter) : DarkMatterScreen(game) {
    private lateinit var progressBar: Image
    private lateinit var touchToBeginLabel: Label


    override fun show() {
        val old = System.currentTimeMillis()
        // queue asset loading
        val assetRefs = gdxArrayOf(
            TextureAsset.values().map { assets.loadAsync(it.descriptor) },
            TextureAtlasAsset.values().map { assets.loadAsync(it.descriptor) },
            SoundAsset.values().map { assets.loadAsync(it.descriptor) },
            ShaderProgramAsset.values().map { assets.loadAsync(it.descriptor) }
        ).flatten()

        //once assets are loaded, change to game screen
        KtxAsync.launch {
            assetRefs.joinAll()
            LOG.debug { "Time for loading assets: ${System.currentTimeMillis() - old}ms" }
            assetsLoaded()
        }
        // ...
        // setup UI and display loading bar
        setupUi()
    }

    override fun hide() {
        stage.clear()
    }

    private fun setupUi() {
        stage.actors {
            table {
                defaults().fillX().expandX()
                label("Loading Screen", LabelStyles.GRADIENT.name) {
                    wrap = true
                    setAlignment(Align.center)
                }
                row()

                touchToBeginLabel = label("Touch to Begin!", LabelStyles.DEFAULT.name) { cell ->
                    wrap = true
                    setAlignment(Align.center)
                    color.a = 0f
                }
                row()
                stack { cell ->
                    progressBar = image("life_bar").apply {
                        scaleX = 0f
                    }
                    label("Loading...", LabelStyles.DEFAULT.name) {
                        setAlignment(Align.center)
                    }
                    cell.padLeft(5f).padRight(5f)
                }
                setFillParent(true)
                pack()
            }
        }
        stage.isDebugAll = Gdx.app.logLevel == Application.LOG_DEBUG
    }

    override fun render(delta: Float) {
        if (assets.progress.isFinished && Gdx.input.justTouched() && game.containsScreen<GameScreen>()) {
            game.setScreen<GameScreen>()
            game.removeScreen<LoadingScreen>()
            dispose()
        }
        progressBar.scaleX = assets.progress.percent
        stage.run {
            viewport.apply()
            act()
            draw()
        }
    }

    private fun assetsLoaded() {
        game.addScreen(GameScreen(game))
        touchToBeginLabel += Actions.forever(Actions.sequence(Actions.fadeIn(0.5f) + Actions.fadeOut(0.5f)))
    }
}