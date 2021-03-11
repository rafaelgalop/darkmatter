package com.pug.darkmatter.ecs.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.pug.darkmatter.ecs.component.*
import com.pug.darkmatter.event.*
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get
import ktx.graphics.use
import ktx.log.Logger
import ktx.log.error
import ktx.log.logger
import kotlin.math.min

private var LOG: Logger = logger<RenderSystem>()

class RenderSystem(
    private val batch: Batch,
    private val gameViewport: Viewport,
    private val uiViewport: FitViewport,
    backgroundTexture: Texture,
    private val gameEventManager: GameEventManager,
    private val outlineShader: ShaderProgram
) : GameEventListener, SortedIteratingSystem(
    allOf(TransformComponent::class, GraphicComponent::class).get(),
    compareBy { entity -> entity[TransformComponent.mapper] }
) {
    private val background = Sprite(backgroundTexture.apply {
        setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat)
    })
    private val backgroundScrollSpeed = Vector2(0.03f, -0.25f)

    private val textureSizeLocation = outlineShader.getUniformLocation("u_textureSize")
    private val outlineColorLocation = outlineShader.getUniformLocation("u_outlineColor")
    private val outlineColor = Color(0f, 113f / 255f, 214 / 255f, 1f)
    private val playerEntities by lazy {
        engine.getEntitiesFor(allOf(PlayerComponent::class).exclude(RemoveComponent::class).get())

    }


    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        gameEventManager.addListener(GameEvent.CollectPowerUp::class, this)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
        gameEventManager.removeListener(GameEvent.CollectPowerUp::class, this)
    }

    override fun update(deltaTime: Float) {
        uiViewport.apply()
        batch.use(uiViewport.camera.combined) {
            // render background
            background.run {
                //go back to original scroll speed over time
                backgroundScrollSpeed.y = min(-0.25f, backgroundScrollSpeed.y + deltaTime * (1f / 10f))
                scroll(backgroundScrollSpeed.x * deltaTime, backgroundScrollSpeed.y * deltaTime)
                draw(batch)
            }
        }
        forceSort()
        gameViewport.apply()
        batch.use(gameViewport.camera.combined) {
            // entity rendering
            super.update(deltaTime)
        }

        // render outline of entities
        renderEntityOutlines()
    }

    private fun renderEntityOutlines() {
        batch.use(gameViewport.camera.combined) {
            it.shader = outlineShader
            playerEntities.forEach { entity ->
                renderPlayerOutlines(entity, it)
            }
            it.shader = null
        }
    }

    private fun renderPlayerOutlines(entity: Entity, batch: Batch) {
        val player = entity[PlayerComponent.mapper]
        require(player != null) { "Entity |entity| must have a PlayerComponent. entity=$entity" }

        if (player.shield > 0f) {
            outlineColor.a = MathUtils.clamp(player.shield / player.maxShield, 0f, 1f)

            outlineShader.setUniformf(outlineColorLocation, outlineColor)
            entity[GraphicComponent.mapper]?.let { graphic ->
                graphic.sprite.run {
                    outlineShader.setUniformf(textureSizeLocation, texture.width.toFloat(), texture.height.toFloat())
                    draw(batch)
                }
            }
        }

    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity[TransformComponent.mapper]
        require(transform != null) { "Entity |entity| must have a TransformComponent. entity=$entity" }

        val graphic = entity[GraphicComponent.mapper]
        require(graphic != null) { "Entity |entity| must have a GraphicComponent. entity=$entity" }

        if (graphic.sprite.texture == null) {
            LOG.error { "Entity has no texture for rendering" }
            return
        }

        graphic.sprite.run {
            rotation = transform.rotationDeg
            setBounds(
                transform.interpolatedPosition.x,
                transform.interpolatedPosition.y,
                transform.size.x,
                transform.size.y
            )
            draw(batch)
        }
    }

    override fun onEvent(event: GameEvent) {
        // if interested in different type of events do something like:
        // this will use kotlin smart cast functionality as benefit
//        when(event) {
//            is GameEvent.CollectPowerUp -> {
//            }
//            is GameEvent.PlayerDeath -> {
//                event.distance
//            }
//        }
        val powerUpEvent = event as GameEvent.CollectPowerUp
        if (powerUpEvent.type == PowerUpType.SPEED_1) {
            backgroundScrollSpeed.y -= 0.25f
        } else if (powerUpEvent.type == PowerUpType.SPEED_2) {
            backgroundScrollSpeed.y -= 0.5f
        }
    }
}