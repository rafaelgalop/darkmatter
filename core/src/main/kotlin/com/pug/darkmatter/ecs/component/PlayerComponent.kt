package com.pug.darkmatter.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import com.pug.darkmatter.ecs.asset.SoundAsset
import ktx.ashley.mapperFor

const val MAX_LIFE =100f
const val MAX_SHIELD =100f

class PlayerComponent: Component, Pool.Poolable {
    var life = MAX_LIFE
    var maxLife = MAX_LIFE
    var shield = 0f
    var maxShield = MAX_SHIELD
    var distance = 0f
    val damageSoundAsset: SoundAsset = SoundAsset.DAMAGE
    val explosionSoundAsset: SoundAsset = SoundAsset.EXPLOSION

    override fun reset() {
        life = MAX_LIFE
        maxLife = MAX_LIFE
        shield = 0f
        maxShield = MAX_SHIELD
        distance = 0f
    }

    companion object {
        val mapper = mapperFor<PlayerComponent>()
    }
}