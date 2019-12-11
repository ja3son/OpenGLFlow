package com.ja3son.gllib.demo.senior_physics.jbullet_demo

import com.bulletphysics.collision.shapes.CollisionShape
import com.bulletphysics.dynamics.DiscreteDynamicsWorld
import com.bulletphysics.dynamics.RigidBody
import com.bulletphysics.dynamics.RigidBodyConstructionInfo
import com.bulletphysics.linearmath.DefaultMotionState
import com.bulletphysics.linearmath.Transform
import com.ja3son.gllib.util.MatrixState
import javax.vecmath.Quat4f
import javax.vecmath.Vector3f
import kotlin.math.acos
import kotlin.math.sin


class JBulletCubeEntity(private val halfSize: Float,
                        colShape: CollisionShape,
                        dynamicsWorld: DiscreteDynamicsWorld,
                        mass: Float,
                        cx: Float, cy: Float, cz: Float) {
    var texture_rect: JBulletRectEntity
    var body: RigidBody

    init {
        val isDynamic = mass != 0f
        val localInertia = Vector3f(0f, 0f, 0f)
        if (isDynamic) {
            colShape.calculateLocalInertia(mass, localInertia)
        }
        val transform = Transform()
        transform.setIdentity()
        transform.origin.set(Vector3f(cx, cy, cz))
        val motion_state = DefaultMotionState(transform)
        val info = RigidBodyConstructionInfo(mass, motion_state, colShape, localInertia)
        body = RigidBody(info)
        body.restitution = 0.6f
        body.friction = 0.8f
        dynamicsWorld.addRigidBody(body)
        texture_rect = JBulletRectEntity(halfSize)
    }

    private fun fromSYStoAXYZ(q4: Quat4f): FloatArray {
        val sitaHalf = acos(q4.w.toDouble())
        val nx = (q4.x / sin(sitaHalf)).toFloat()
        val ny = (q4.y / sin(sitaHalf)).toFloat()
        val nz = (q4.z / sin(sitaHalf)).toFloat()
        return floatArrayOf(Math.toDegrees(sitaHalf * 2).toFloat(), nx, ny, nz)
    }

    fun drawSelf(texIda: IntArray) {
        var texId = texIda[0]
        if (!body.isActive) {
            texId = texIda[1]
        }
        MatrixState.pushMatrix()
        val trans = body.motionState.getWorldTransform(Transform())
        MatrixState.translate(trans.origin.x, trans.origin.y, trans.origin.z)
        val ro: Quat4f = trans.getRotation(Quat4f())
        if ((ro.x != 0.0f) or (ro.y != 0.0f) or (ro.z != 0.0f)) {
            val fa: FloatArray = fromSYStoAXYZ(ro)
            MatrixState.rotate(fa[0], fa[1], fa[2], fa[3])
        }
        MatrixState.pushMatrix()
        MatrixState.translate(0f, halfSize, 0f)
        MatrixState.rotate((-90).toFloat(), 1f, 0f, 0f)
        texture_rect.drawSelf(texId)
        MatrixState.popMatrix()
        MatrixState.pushMatrix()
        MatrixState.translate(0f, -halfSize, 0f)
        MatrixState.rotate(90f, 1f, 0f, 0f)
        texture_rect.drawSelf(texId)
        MatrixState.popMatrix()
        MatrixState.pushMatrix()
        MatrixState.translate(-halfSize, 0f, 0f)
        MatrixState.rotate((-90).toFloat(), 0f, 1f, 0f)
        texture_rect.drawSelf(texId)
        MatrixState.popMatrix()
        MatrixState.pushMatrix()
        MatrixState.translate(halfSize, 0f, 0f)
        MatrixState.rotate(90f, 0f, 1f, 0f)
        texture_rect.drawSelf(texId)
        MatrixState.popMatrix()
        MatrixState.pushMatrix()
        MatrixState.translate(0f, 0f, halfSize)
        texture_rect.drawSelf(texId)
        MatrixState.popMatrix()
        MatrixState.pushMatrix()
        MatrixState.translate(0f, 0f, -halfSize)
        MatrixState.rotate(180f, 0f, 1f, 0f)
        texture_rect.drawSelf(texId)
        MatrixState.popMatrix()
        MatrixState.popMatrix()
    }
}