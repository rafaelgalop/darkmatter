package com.pug.darkmatter.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.pug.darkmatter.DarkMatter
import com.pug.darkmatter.UNIT_SCALE
import com.pug.darkmatter.ecs.component.*
import ktx.ashley.entity
import ktx.ashley.with
import ktx.graphics.use
import ktx.log.Logger
import ktx.log.debug
import ktx.log.logger

private var LOG: Logger = logger<GameScreen>()

class GameScreen(game: DarkMatter) : DarkMatterScreen(game) {

    override fun show() {
        LOG.debug { "Game Screen is shown" }
        engine.entity {
            with<TransformComponent> {
                position.set(4.5f, 8f, 0f)
            }
            with<MoveComponent>()
            with<GraphicComponent>()
            with<PlayerComponent>()
            with<FacingComponent>()
        }
    }

    override fun render(delta: Float) {
        (game.batch as SpriteBatch).renderCalls =0
        engine.update(delta)
        LOG.debug { "Rendercalls: ${(game.batch as SpriteBatch).renderCalls}"}
    }
}