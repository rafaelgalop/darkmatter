package com.pug.darkmatter.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.MathUtils
import com.pug.darkmatter.DarkMatter
import com.pug.darkmatter.UNIT_SCALE
import com.pug.darkmatter.ecs.component.GraphicComponent
import com.pug.darkmatter.ecs.component.TransformComponent
import ktx.ashley.entity
import ktx.ashley.with
import ktx.graphics.use
import ktx.log.Logger
import ktx.log.debug
import ktx.log.logger

private var LOG: Logger = logger<GameScreen>()

class GameScreen(game: DarkMatter) : DarkMatterScreen(game) {
    private val playerTexture = Texture(Gdx.files.internal("graphics/ship_base.png"))

    override fun show() {
        LOG.debug { "Game Screen is shown" }
        repeat(10){
            engine.entity {
                with<TransformComponent> {
                    position.set(MathUtils.random(0f,9f), MathUtils.random(0f,16f), 0f)
                }
                with<GraphicComponent>
                {
                    sprite.run {
                        setRegion(playerTexture)
                        setSize(texture.width * UNIT_SCALE, texture.height * UNIT_SCALE)
                        setOriginCenter()
                    }
                }

            }

        }
    }

    override fun render(delta: Float) {
        engine.update(delta)
    }

    override fun dispose() {
        playerTexture.dispose()
    }
}