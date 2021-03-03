package com.pug.darkmatter.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.pug.darkmatter.DarkMatter
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

/** Launches the desktop (LWJGL3) application.  */

fun main() {
    createApplication()
}

private fun createApplication(): Lwjgl3Application {
    return Lwjgl3Application(
        DarkMatter(),
        Lwjgl3ApplicationConfiguration().apply {
            setTitle("Dark Matter")
            setWindowedMode(9*32, 16*32)
            setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")

        })
}