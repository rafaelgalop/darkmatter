package com.pug.darkmatter.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.pug.darkmatter.DarkMatter
import ktx.log.Logger
import ktx.log.debug
import ktx.log.logger

private var LOG: Logger = logger<FirstScreen>()
class FirstScreen(game: DarkMatter) : DarkMatterScreen(game) {
    override fun show() {
        LOG.debug { "First Screen is shown" }
    }

    override fun render(delta: Float) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
            game.setScreen<SecondScreen>();
        }
    }
}