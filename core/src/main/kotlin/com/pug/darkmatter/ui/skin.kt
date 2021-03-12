package com.pug.darkmatter.ui

import com.pug.darkmatter.ecs.asset.BitmapFontAsset
import com.pug.darkmatter.ecs.asset.TextureAtlasAsset
import ktx.assets.async.AssetStorage
import ktx.scene2d.Scene2DSkin
import ktx.style.label
import ktx.style.skin

enum class LabelStyles { DEFAULT, GRADIENT }

fun createSkin(assets: AssetStorage) {
    val atlas = assets[TextureAtlasAsset.UI.descriptor]
    val gradientFont = assets[BitmapFontAsset.FONT_LARGE_GRADIENT.descriptor]
    val normalFont = assets[BitmapFontAsset.FONT_DEFAULT.descriptor]
    Scene2DSkin.defaultSkin = skin(atlas) { skin ->
        label(LabelStyles.DEFAULT.name) {
            font = normalFont
        }
        label(LabelStyles.GRADIENT.name) {
            font = gradientFont
        }
    }
}