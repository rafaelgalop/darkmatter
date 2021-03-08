package com.pug.darkmatter.screen

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.viewport.Viewport
import com.pug.darkmatter.DarkMatter
import com.pug.darkmatter.ecs.audio.AudioService
import com.pug.darkmatter.event.GameEventManager
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage

abstract class DarkMatterScreen(
    val game: DarkMatter,
    val gameViewPort: Viewport = game.gameViewport,
    val uiViewPort: Viewport = game.uiViewport,
    val gameEventManager: GameEventManager = game.gameEventManager,
    val assets: AssetStorage = game.assets,
    val audioService: AudioService = game.audioService
) : KtxScreen {

    override fun resize(width: Int, height: Int) {
        gameViewPort.update(width, height, true)
        uiViewPort.update(width, height, true)
    }
}