package com.pug.darkmatter.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.pug.darkmatter.ecs.component.PlayerComponent
import com.pug.darkmatter.ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.getSystem
import kotlin.math.min

private const val WINDOW_INFO_UPDATE_RATE = 0.25f

class DebugSystem : IntervalIteratingSystem(allOf(PlayerComponent::class).get(), WINDOW_INFO_UPDATE_RATE) {
    init {
        setProcessing(true)
    }

    override fun processEntity(entity: Entity) {
        val transform = entity[TransformComponent.mapper]
        require(transform != null) { "Entity |entity| must have a TransformComponent. entity=$entity" }
        val player = entity[PlayerComponent.mapper]
        require(player != null) { "Entity |entity| must have a PlayerComponent. entity=$entity" }

        when {
            Gdx.input.isKeyPressed(Input.Keys.NUM_1) -> {
                //Kill player
                transform.position.y = 1f
                player.life = 1f
                player.shield = 0f
            }
            Gdx.input.isKeyPressed(Input.Keys.NUM_2) -> {
                // add shield
                player.shield = min(player.maxshield, player.shield + 25f)
            }
            Gdx.input.isKeyPressed(Input.Keys.NUM_3) -> {
                // remove shield
                player.shield = min(player.maxshield, player.shield - 25f)
            }
            Gdx.input.isKeyPressed(Input.Keys.NUM_4) -> {
                // disable movement
                engine.getSystem<MoveSystem>().setProcessing(false)
            }
            Gdx.input.isKeyPressed(Input.Keys.NUM_5) -> {
                // enable movement
                engine.getSystem<MoveSystem>().setProcessing(true)
            }
        }

        Gdx.graphics.setTitle("DM Debug - pos:${transform.position}, life ${player.life}, shield:${player.shield}")
    }
}