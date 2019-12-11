package com.ja3son.gllib.demo.senior_physics.jbullet_demo

import android.opengl.GLES30
import com.bulletphysics.collision.shapes.CollisionShape
import com.bulletphysics.dynamics.DiscreteDynamicsWorld
import com.bulletphysics.dynamics.RigidBody
import com.bulletphysics.dynamics.RigidBodyConstructionInfo
import com.bulletphysics.linearmath.DefaultMotionState
import com.bulletphysics.linearmath.Transform
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.vecmath.Vector3f


class JBulletFloorEntity(private val UNIT_CONST: Float, private val y_offset: Float,
                         ground_shape: CollisionShape,
                         dynamics_world: DiscreteDynamicsWorld) : BaseEntity() {

    val bufferIds = IntArray(2)
    var vertexBufferId = 0
    var texCoordBufferId = 0

    init {
        val ground_transform = Transform()
        ground_transform.setIdentity()
        ground_transform.origin.set(Vector3f(0f, y_offset, 0f))

        val local_inertia = Vector3f(0f, 0f, 0f)

        val motion_state = DefaultMotionState(ground_transform)
        val info = RigidBodyConstructionInfo(0f, motion_state, ground_shape, local_inertia)
        val body = RigidBody(info)
        body.restitution = 0.4f
        body.friction = 0.8f

        dynamics_world.addRigidBody(body)

        init()
    }

    override fun initVertexData() {
        vCounts = 6
        val vertices = floatArrayOf(
                1 * UNIT_CONST, y_offset, 1 * UNIT_CONST,
                -1 * UNIT_CONST, y_offset, -1 * UNIT_CONST,
                -1 * UNIT_CONST, y_offset, 1 * UNIT_CONST,
                1 * UNIT_CONST, y_offset, 1 * UNIT_CONST,
                1 * UNIT_CONST, y_offset, -1 * UNIT_CONST,
                -1 * UNIT_CONST, y_offset, -1 * UNIT_CONST)

        val texCoords = floatArrayOf(
                UNIT_CONST / 2, UNIT_CONST / 2,
                0f, 0f,
                0f, UNIT_CONST / 2,
                UNIT_CONST / 2, UNIT_CONST / 2,
                UNIT_CONST / 2, 0f,
                0f, 0f
        )

        GLES30.glGenBuffers(2, bufferIds, 0)

        vertexBufferId = bufferIds[0]
        texCoordBufferId = bufferIds[1]

        vCounts = vertices.size / 3
        verticesBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(vertices).position(0) as FloatBuffer

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBufferId)
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertices.size * FLOAT_SIZE, verticesBuffer, GLES30.GL_STATIC_DRAW)

        texCoorBuffer = ByteBuffer.allocateDirect(texCoords.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(texCoords).position(0) as FloatBuffer

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, texCoordBufferId)
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, texCoords.size * FLOAT_SIZE, texCoorBuffer, GLES30.GL_STATIC_DRAW)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("jbullet/vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("jbullet/fragment.glsl")
        )
    }

    override fun drawSelf(textureId: Int) {
        GLES30.glUseProgram(program)
        GLES30.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES30.glUniformMatrix4fv(uMMatrix, 1, false, MatrixState.getModelMatrix(), 0)
        GLES30.glUniform3fv(uLightLocation, 1, MatrixState.lightPositionFB)
        GLES30.glUniform3fv(uCamera, 1, MatrixState.cameraFB)

        GLES30.glEnableVertexAttribArray(aPosition)
        GLES30.glEnableVertexAttribArray(aTexCoor)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBufferId)
        GLES30.glVertexAttribPointer(aPosition, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, 0)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, texCoordBufferId)
        GLES30.glVertexAttribPointer(aTexCoor, texLen, GLES30.GL_FLOAT, false, texLen * FLOAT_SIZE, 0)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId)

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCounts)

        GLES30.glDisableVertexAttribArray(aPosition)
        GLES30.glDisableVertexAttribArray(aTexCoor)
    }
}