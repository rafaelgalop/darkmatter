package com.pug.darkmatter

import com.badlogic.gdx.Game
import com.pug.darkmatter.FirstScreen

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms.  */
class DarkMatter : Game() {
    override fun create() {
        setScreen(FirstScreen())
    }
}