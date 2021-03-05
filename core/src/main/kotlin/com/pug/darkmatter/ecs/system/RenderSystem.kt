package com.pug.darkmatter.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.viewport.Viewport
import com.pug.darkmatter.ecs.component.GraphicComponent
import com.pug.darkmatter.ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.graphics.use
import ktx.log.Logger
import ktx.log.error
import ktx.log.logger

private var LOG: Logger = logger<RenderSystem>()

class RenderSystem(
    private val batch: Batch,
    private val gameViewport: Viewport
) : SortedIteratingSystem(
    allOf(TransformComponent::class, GraphicComponent::class).get(),
    compareBy { entity -> entity[TransformComponent.mapper] }
) {

    override fun update(deltaTime: Float) {
        forceSort()
        gameViewport.apply()
        batch.use(gameViewport.camera.combined) {
            super.update(deltaTime)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity[TransformComponent.mapper]
        require(transform != null) { "Entity |entity| must have a TransformComponent. entity=$entity" }

        val graphic = entity[GraphicComponent.mapper]
        require(graphic != null) { "Entity |entity| must have a GraphicComponent. entity=$entity" }

        if( graphic.sprite.texture == null){
            LOG.error{ "Entity has no texture for rendering"}
            return
        }

        graphic.sprite.run {
            rotation = transform.rotationDeg
            setBounds(transform.interpolatedPosition.x, transform.interpolatedPosition.y, transform.size.x, transform.size.y)
            draw(batch)
        }
    }
}