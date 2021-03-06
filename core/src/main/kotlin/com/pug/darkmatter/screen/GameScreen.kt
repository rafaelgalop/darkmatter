package com.pug.darkmatter.screen

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pug.darkmatter.DarkMatter
import com.pug.darkmatter.UNIT_SCALE
import com.pug.darkmatter.V_WIDTH
import com.pug.darkmatter.ecs.asset.MusicAsset
import com.pug.darkmatter.ecs.component.*
import com.pug.darkmatter.ecs.system.DAMAGE_AREA_HEIGHT
import com.pug.darkmatter.event.GameEvent
import com.pug.darkmatter.event.GameEventListener
import com.pug.darkmatter.file.PreferenceKeys
import com.pug.darkmatter.file.saveToPreferences
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.Logger
import ktx.log.debug
import ktx.log.logger
import ktx.preferences.get
import kotlin.math.min

private var LOG: Logger = logger<GameScreen>()

// const to avoid spiral of death
private const val MAX_DELTA_TIME = 1 / 20f

class GameScreen(
    game: DarkMatter,
    val engine: Engine = game.engine
) : DarkMatterScreen(game), GameEventListener {

    override fun show() {
        LOG.debug { "Game Screen is shown" }
        LOG.debug {
            "${preferences[PreferenceKeys.HIGH_SCORE.keyName, 0f]}"
        }
        gameEventManager.addListener(GameEvent.PlayerDeath::class, this)

        audioService.play(MusicAsset.GAME)
        spawnPlayer()
        engine.entity {
            with<TransformComponent> {
                size.set(
                    V_WIDTH.toFloat(),
                    DAMAGE_AREA_HEIGHT
                )
            }
            with<AnimationComponent> {
                type = AnimationType.DARK_MATTER
            }
            with<GraphicComponent>()
        }
    }

    override fun hide() {
        super.hide()
        gameEventManager.removeListener(this)
    }

    private fun spawnPlayer() {
        val playerShip = engine.entity {
            with<TransformComponent> {
                setInitialPosition(4.5f, 8f, -1f)
            }
            with<MoveComponent>()
            with<GraphicComponent>()
            with<PlayerComponent>()
            with<FacingComponent>()
        }
        engine.entity {
            with<TransformComponent>()
            with<AttachComponent> {
                entity = playerShip
                offset.set(1f * UNIT_SCALE, -6f * UNIT_SCALE)
            }
            with<GraphicComponent>()
            with<AnimationComponent> {
                type = AnimationType.FIRE
            }
        }
    }

    override fun render(delta: Float) {
        (game.batch as SpriteBatch).renderCalls = 0
        engine.update(min(MAX_DELTA_TIME, delta))
        audioService.update()

//        LOG.debug { "Render calls: ${(game.batch as SpriteBatch).renderCalls}" }
    }

    override fun onEvent(event: GameEvent) {
        // since GameEvent is a sealed kotlin class
        when (event) {
            is GameEvent.PlayerDeath -> {
                LOG.debug { "Player died with a distance of ${event.distance}" }
                if (preferences[PreferenceKeys.HIGH_SCORE.keyName, 0f] < event.distance) {
                    LOG.debug { "New High Score achieved: ${event.distance}" }
                    saveToPreferences(preferences, PreferenceKeys.HIGH_SCORE, event.distance)
                }
                spawnPlayer()
            }
            is GameEvent.CollectPowerUp -> Unit
            is GameEvent.PlayerHit -> Unit
        }
    }
}