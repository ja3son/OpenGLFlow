package com.ja3son.gllib.demo.senior_physics.jbullet_demo

import android.opengl.GLES30
import com.bulletphysics.collision.dispatch.CollisionFlags
import com.bulletphysics.collision.dispatch.CollisionObject
import com.bulletphysics.collision.shapes.BvhTriangleMeshShape
import com.bulletphysics.collision.shapes.CollisionShape
import com.bulletphysics.collision.shapes.TriangleIndexVertexArray
import com.bulletphysics.dynamics.DiscreteDynamicsWorld
import com.bulletphysics.dynamics.RigidBody
import com.bulletphysics.dynamics.RigidBodyConstructionInfo
import com.bulletphysics.linearmath.DefaultMotionState
import com.bulletphysics.linearmath.Transform
import com.ja3son.gllib.R
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.vecmath.Vector3f


class JBulletLandFloorEntity(private val UNIT_CONST: Float, private val y_offset: Float,
                             val dynamics_world: DiscreteDynamicsWorld) : BaseEntity() {

    val bufferIds = IntArray(2)
    var vertexBufferId = 0
    var texCoordBufferId = 0

    var COLS = 0
    var ROWS = 0

    init {
        init()
    }

    fun generateTexCoor(bw: Int, bh: Int): FloatArray {
        val result = FloatArray(bw * bh * 6 * 2)
        val sizew = 16.0f / bw
        val sizeh = 16.0f / bh
        var c = 0
        for (i in 0 until bh) {
            for (j in 0 until bw) {
                val s = j * sizew
                val t = i * sizeh
                result[c++] = s
                result[c++] = t
                result[c++] = s
                result[c++] = t + sizeh
                result[c++] = s + sizew
                result[c++] = t
                result[c++] = s + sizew
                result[c++] = t
                result[c++] = s
                result[c++] = t + sizeh
                result[c++] = s + sizew
                result[c++] = t + sizeh
            }
        }
        return result
    }

    override fun initVertexData() {
        val landForms = ShaderUtils.loadLandForms(R.drawable.landform)
        COLS = ShaderUtils.getWidth(R.drawable.land)
        ROWS = ShaderUtils.getHeight(R.drawable.land)

        vCounts = COLS * ROWS * 2 * 3

        val vertices = FloatArray(vCounts * 3)

        var count = 0

        for (j in 0 until ROWS) {
            for (i in 0 until COLS) {
                val zsx: Float = -UNIT_CONST * COLS / 2 + i * UNIT_CONST
                val zsz: Float = -UNIT_CONST * ROWS / 2 + j * UNIT_CONST
                vertices[count++] = zsx
                vertices[count++] = landForms[j][i] + y_offset
                vertices[count++] = zsz
                vertices[count++] = zsx
                vertices[count++] = landForms[j + 1][i] + y_offset
                vertices[count++] = zsz + UNIT_CONST
                vertices[count++] = zsx + UNIT_CONST
                vertices[count++] = landForms[j][i + 1] + y_offset
                vertices[count++] = zsz
                vertices[count++] = zsx + UNIT_CONST
                vertices[count++] = landForms[j][i + 1] + y_offset
                vertices[count++] = zsz
                vertices[count++] = zsx
                vertices[count++] = landForms[j + 1][i] + y_offset
                vertices[count++] = zsz + UNIT_CONST
                vertices[count++] = zsx + UNIT_CONST
                vertices[count++] = landForms[j + 1][i + 1] + y_offset
                vertices[count++] = zsz + UNIT_CONST
            }
        }

        val texCoords = generateTexCoor(COLS, ROWS)

        GLES30.glGenBuffers(2, bufferIds, 0)

        vertexBufferId = bufferIds[0]
        texCoordBufferId = bufferIds[1]

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


        val gVertices = ByteBuffer.allocateDirect(vCounts * 3 * 4).order(ByteOrder.nativeOrder())
        for (i in vertices.indices) {
            gVertices.putFloat(i * 4, vertices[i])
        }
        gVertices.position(0)

        val gIndices = ByteBuffer.allocateDirect(vCounts * 4).order(ByteOrder.nativeOrder())
        for (i in 0 until vCounts) {
            gIndices.putInt(i)
        }
        gIndices.position(0)

        val vertStride = 4 * 3
        val indexStride = 4 * 3

        val indexVertexArrays = TriangleIndexVertexArray(
                vCounts / 3,
                gIndices,
                indexStride,
                vCounts,
                gVertices,
                vertStride
        )

        val groundShape: CollisionShape = BvhTriangleMeshShape(indexVertexArrays, true, true)

        val groundTransform = Transform()
        groundTransform.setIdentity()
        groundTransform.origin.set(Vector3f(0f, 0f, 0f))
        val localInertia = Vector3f(0f, 0f, 0f)

        val myMotionState = DefaultMotionState(groundTransform)
        val rbInfo = RigidBodyConstructionInfo(0f, myMotionState, groundShape, localInertia)
        val body = RigidBody(rbInfo)

        body.restitution = 0.4f
        body.friction = 0.8f
        body.collisionFlags = body.collisionFlags and CollisionFlags.KINEMATIC_OBJECT.inv()
        body.forceActivationState(CollisionObject.ACTIVE_TAG)

        dynamics_world.addRigidBody(body)
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