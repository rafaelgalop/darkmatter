package com.pug.darkmatter.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.pug.darkmatter.DarkMatter
import com.pug.darkmatter.UNIT_SCALE
import com.pug.darkmatter.V_HEIGHT
import com.pug.darkmatter.V_WIDTH
import com.pug.darkmatter.ecs.component.*
import com.pug.darkmatter.ecs.system.DAMAGE_AREA_HEIGHT
import ktx.ashley.entity
import ktx.ashley.with
import ktx.graphics.use
import ktx.log.Logger
import ktx.log.debug
import ktx.log.logger
import kotlin.math.min

private var LOG: Logger = logger<GameScreen>()

// const to avoid spiral of death
private const val MAX_DELTA_TIME = 1 / 20f

class GameScreen(game: DarkMatter) : DarkMatterScreen(game) {

    override fun show() {
        LOG.debug { "Game Screen is shown" }

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
            with<AnimationComponent>{
                type = AnimationType.FIRE
            }

        }
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

    override fun render(delta: Float) {
        (game.batch as SpriteBatch).renderCalls = 0
        engine.update(min(MAX_DELTA_TIME, delta))
//        LOG.debug { "Rendercalls: ${(game.batch as SpriteBatch).renderCalls}" }
    }
}