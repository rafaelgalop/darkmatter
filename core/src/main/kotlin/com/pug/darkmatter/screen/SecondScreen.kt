package com.pug.darkmatter.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.pug.darkmatter.DarkMatter
import ktx.app.KtxScreen
import ktx.log.Logger
import ktx.log.debug
import ktx.log.logger

private val LOG: Logger = logger<SecondScreen>()

class SecondScreen(game:DarkMatter): DarkMatterScreen(game) {
    override fun show() {
        LOG.debug { "Second Screen is shown" }
    }

    override fun render(delta: Float) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)){
            game.setScreen<FirstScreen>();
        }
    }
}