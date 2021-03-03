package com.pug.darkmatter

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pug.darkmatter.screen.DarkMatterScreen
import com.pug.darkmatter.screen.GameScreen
import ktx.app.KtxGame
import ktx.log.Logger
import ktx.log.debug
import ktx.log.logger

const val UNIT_SCALE: Float = 1/16f
private val LOG: Logger = logger<DarkMatter>()

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms.  */
class DarkMatter : KtxGame<DarkMatterScreen>() {

    val batch: Batch by lazy { SpriteBatch() }
    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG
        LOG.debug { "Create game instance" }
        addScreen(GameScreen(this))
        setScreen<GameScreen>()
    }

    override fun dispose() {
        batch.dispose()
    }
}