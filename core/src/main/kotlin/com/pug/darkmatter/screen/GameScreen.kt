package com.pug.darkmatter.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.utils.viewport.FitViewport
import com.pug.darkmatter.DarkMatter
import com.pug.darkmatter.UNIT_SCALE
import com.pug.darkmatter.ecs.component.GraphicComponent
import com.pug.darkmatter.ecs.component.TransformComponent
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with
import ktx.graphics.use
import ktx.log.Logger
import ktx.log.debug
import ktx.log.logger

private var LOG: Logger = logger<GameScreen>()

class GameScreen(game: DarkMatter) : DarkMatterScreen(game) {

    private val viewport = FitViewport(9f, 16f)
    private val playerTexture = Texture(Gdx.files.internal("graphics/ship_base.png"))
    private val player = engine.entity {
        with<TransformComponent> {
            position.set(1f, 1f, 0f)
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

    override fun show() {
        LOG.debug { "Game Screen is shown" }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        engine.update(delta)

        viewport.apply()
        batch.use(viewport.camera.combined) { batch ->
            player[GraphicComponent.mapper]?.let { graphic ->
                player[TransformComponent.mapper]?.let{ transform ->
                    graphic.sprite.run {
                        rotation = transform.rotationDeg
                        setBounds(transform.position.x, transform.position.y, transform.size.x, transform.size.y)
                        draw(batch)
                    }
                }

            }
        }
    }

    override fun dispose() {
        playerTexture.dispose()
    }
}