package com.pug.darkmatter.ecs.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.pug.darkmatter.ecs.component.AttachComponent
import com.pug.darkmatter.ecs.component.GraphicComponent
import com.pug.darkmatter.ecs.component.RemoveComponent
import com.pug.darkmatter.ecs.component.TransformComponent
import com.sun.corba.se.impl.orbutil.graph.Graph
import ktx.ashley.addComponent
import ktx.ashley.allOf
import ktx.ashley.get

class AttachSystem : EntityListener,
    IteratingSystem(allOf(AttachComponent::class, TransformComponent::class, GraphicComponent::class).get()) {

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        engine.addEntityListener(this)
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        engine.removeEntityListener(this)
    }

    override fun entityAdded(entity: Entity) = Unit

    override fun entityRemoved(removedEntity: Entity) {
        entities.forEach { entity ->
            entity[AttachComponent.mapper]?.let { attachComponent ->
                if (attachComponent.entity == removedEntity) {
                    entity.addComponent<RemoveComponent>(engine)
                }
            }
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val attachComponent = entity[AttachComponent.mapper]
        require(attachComponent != null) { "Entity |entity| must have an AttachComponent. entity=$entity" }
        val graphic = entity[GraphicComponent.mapper]
        require(graphic != null) { "Entity |entity| must have a GraphicComponent. entity=$entity" }
        val transform = entity[TransformComponent.mapper]
        require(transform != null) { "Entity |entity| must have a TransformComponent. entity=$entity" }

        //update position
        attachComponent.entity[TransformComponent.mapper]?.let { attachTransform ->
            transform.interpolatedPosition.set(
                attachTransform.interpolatedPosition.x + attachComponent.offset.x,
                attachTransform.interpolatedPosition.y + attachComponent.offset.y,
                transform.position.z
            )
        }

        // update graphic alpha value
        attachComponent.entity[GraphicComponent.mapper]?.let { attachGraphic ->
            graphic.sprite.setAlpha(attachGraphic.sprite.color.a)
        }

    }
}