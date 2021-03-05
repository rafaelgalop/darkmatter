package com.pug.darkmatter.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx.input
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import com.pug.darkmatter.ecs.component.FacingComponent
import com.pug.darkmatter.ecs.component.FacingDirection
import com.pug.darkmatter.ecs.component.PlayerComponent
import com.pug.darkmatter.ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.Logger
import ktx.log.debug
import ktx.log.logger

private const val TOUCH_TOLERANCE_DISTANCE = 0.2f
private var LOG:Logger = logger<PlayerInputSystem>()
class PlayerInputSystem(
    private val gameViewport: Viewport
) : IteratingSystem(allOf(PlayerComponent::class, TransformComponent::class, FacingComponent::class).get()) {
    private val tmpVec = Vector2()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val facing = entity[FacingComponent.mapper]
        require(facing != null) {
            "Entity |entity| must have a Facing Component. entity=$entity"
        }
        val transform = entity[TransformComponent.mapper]
        require(transform != null) { "Entity |entity| must have a Transform Component. entity=$entity" }

        tmpVec.x = input.x.toFloat()
        gameViewport.unproject(tmpVec)
        val diffX = tmpVec.x - transform.position.x - transform.size.x * 0.5f
        facing.direction = when {
            diffX < -TOUCH_TOLERANCE_DISTANCE -> FacingDirection.LEFT
            diffX > TOUCH_TOLERANCE_DISTANCE -> FacingDirection.RIGHT
            else -> FacingDirection.DEFAULT
        }
    }
}