package com.pug.darkmatter.screen

import com.badlogic.gdx.utils.Logger
import com.pug.darkmatter.DarkMatter
import com.pug.darkmatter.ecs.asset.TextureAsset
import com.pug.darkmatter.ecs.asset.TextureAtlasAsset
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf
import ktx.log.debug
import ktx.log.logger

private val LOG = logger<LoadingScreen>()

class LoadingScreen(game: DarkMatter) : DarkMatterScreen(game) {

    override fun show() {
        val old = System.currentTimeMillis()
        // queue asset loading
        val assetRefs = gdxArrayOf(
            TextureAsset.values().map{ assets.loadAsync(it.descriptor)},
            TextureAtlasAsset.values().map { assets.loadAsync(it.descriptor)}
        ).flatten()

        //once assets are loaded, change to game screen
        KtxAsync.launch {
            assetRefs.joinAll()
            LOG.debug { "Time for loading assets: ${System.currentTimeMillis() -old}ms" }
            assetsLoaded()
        }
        // ...
        // setup UI and display loading bar
    }

    private fun assetsLoaded() {
        game.addScreen(GameScreen(game))
        game.setScreen<GameScreen>()
        game.removeScreen<LoadingScreen>()
        dispose()
    }
}