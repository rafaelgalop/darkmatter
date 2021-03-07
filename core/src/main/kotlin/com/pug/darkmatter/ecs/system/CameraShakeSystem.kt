package com.pug.darkmatter.ecs.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Pool
import com.pug.darkmatter.event.GameEvent
import com.pug.darkmatter.event.GameEventListener
import com.pug.darkmatter.event.GameEventManager
import ktx.collections.GdxArray

const val MAX_CAMERA_SHAKE_INSTANCES = 4
const val SHAKE_DURATION_PER_INSTANCE_SECONDS = 0.25f
const val MAX_DISTORTION_VERT_UNITS = 0.25f

private class CameraShake : Pool.Poolable {
    var maxDistortion = 0f
    var duration = 0f
    lateinit var camera: Camera
    private var storeCameraPos = true
    private val origCamPosition = Vector3()
    private var currentDuration = 0f

    fun update(deltaTime: Float): Boolean {
        if (storeCameraPos) {
            storeCameraPos = false
            origCamPosition.set(camera.position)
        }
        if (currentDuration < duration) {
            val currentPower = maxDistortion * ((duration - currentDuration) / duration)

            camera.position.x = origCamPosition.x + MathUtils.random(-1f, 1f) * currentPower
            camera.position.y = origCamPosition.y + MathUtils.random(-1f, 1f) * currentPower
            camera.update()

            currentDuration += deltaTime
            return false
        }

        camera.position.set(origCamPosition)
        camera.update()
        return true
    }

    override fun reset() {
        maxDistortion = 0f
        duration = 0f
        origCamPosition.set(Vector3.Zero)
        storeCameraPos = true
        currentDuration = 0f
    }
}

private class CameraShakePool(private val gameCamera:Camera): Pool<CameraShake> (){
    override fun newObject() = CameraShake().apply {
        this.camera = gameCamera
    }
}

class CameraShakeSystem(
    camera: Camera,
    private val gameEventManager: GameEventManager
) : EntitySystem(), GameEventListener {
    private val shakePool = CameraShakePool(camera)
    private val activeShakes = GdxArray<CameraShake>()

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        gameEventManager.addListener(GameEvent.PlayerHit::class, this)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
        gameEventManager.removeListener(this)
    }

    override fun update(deltaTime: Float) {
        if(!activeShakes.isEmpty){
            val shake = activeShakes.first()
            if(shake.update(deltaTime)){
                activeShakes.removeIndex(0)
                shakePool.free(shake)
            }
        }
    }
    override fun onEvent(event: GameEvent) {
        if(activeShakes.size < MAX_CAMERA_SHAKE_INSTANCES){
            activeShakes.add(shakePool.obtain().apply {
                duration = SHAKE_DURATION_PER_INSTANCE_SECONDS
                maxDistortion = MAX_DISTORTION_VERT_UNITS
            })
        }
    }
}