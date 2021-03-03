package com.pug.darkmatter.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.utils.viewport.FitViewport
import com.pug.darkmatter.DarkMatter
import com.pug.darkmatter.UNIT_SCALE
import ktx.graphics.use
import ktx.log.Logger
import ktx.log.debug
import ktx.log.logger

private var LOG: Logger = logger<GameScreen>()

class GameScreen(game: DarkMatter) : DarkMatterScreen(game) {

    private val viewport = FitViewport(9f, 16f)
    private val texture = Texture(Gdx.files.internal("graphics/ship_base.png"))
    private val sprite = Sprite(texture).apply {
        setSize(1f, 1f)

    }

    override fun show() {
        LOG.debug { "Game Screen is shown" }
        sprite.setPosition(1f, 1f)
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        viewport.apply()
        batch.use(viewport.camera.combined) {
            sprite.draw(it)
        }
    }

    override fun dispose() {
        texture.dispose()
    }
}