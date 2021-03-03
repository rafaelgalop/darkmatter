package com.pug.darkmatter

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.pug.darkmatter.screen.FirstScreen
import com.pug.darkmatter.screen.SecondScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.log.Logger
import ktx.log.debug
import ktx.log.logger

private val LOG: Logger = logger<DarkMatter>()

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms.  */
class DarkMatter : KtxGame<KtxScreen>() {
    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG
        LOG.debug { "Create game instance" }
        addScreen(FirstScreen(this))
        addScreen(SecondScreen(this))
        setScreen<FirstScreen>()
    }
}