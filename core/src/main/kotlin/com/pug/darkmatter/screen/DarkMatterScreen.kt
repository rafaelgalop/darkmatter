package com.pug.darkmatter.screen

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.viewport.Viewport
import com.pug.darkmatter.DarkMatter
import com.pug.darkmatter.event.GameEventManager
import ktx.app.KtxScreen

abstract class DarkMatterScreen(
    val game: DarkMatter,
    val batch: Batch = game.batch,
    val gameViewPort: Viewport = game.gameViewport,
    val uiViewPort: Viewport = game.uiViewport,
    val engine: Engine = game.engine,
    val gameEventManager: GameEventManager = game.gameEventManager
) : KtxScreen {

    override fun resize(width: Int, height: Int) {
        gameViewPort.update(width, height, true)
        uiViewPort.update(width, height, true)
    }
}