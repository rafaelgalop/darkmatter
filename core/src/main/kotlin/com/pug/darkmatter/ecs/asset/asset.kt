package com.pug.darkmatter.ecs.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas

enum class TextureAsset(
    fileName: String,
    directory: String = "graphics",
    val descriptor: AssetDescriptor<Texture> = AssetDescriptor("$directory/$fileName", Texture::class.java)
) {
    BACKGROUND("background.png")
}
enum class TextureAtlasAsset(
    fileName: String,
    directory: String = "graphics",
    val descriptor: AssetDescriptor<TextureAtlas> = AssetDescriptor("$directory/$fileName", TextureAtlas::class.java)
) {
    GMAE_GRAPHICS("graphics.atlas")
}