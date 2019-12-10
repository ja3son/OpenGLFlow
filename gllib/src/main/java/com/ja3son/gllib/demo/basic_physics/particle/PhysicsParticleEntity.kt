package com.ja3son.gllib.demo.basic_physics.particle

import android.opengl.GLES30
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import kotlin.math.cos
import kotlin.math.sin


class PhysicsParticleEntity(private val scale: Float, private val counts: Int) : BaseEntity() {
    val bufferIds = IntArray(1)
    var vertexBufferId = 0

    var timeLive = 0f
    var timeStamp: Long = 0

    var uPointSizeHandle = 0
    var uTimeHandle = 0

    init {
        init()
    }

    override fun initVertexData() {
        GLES30.glGenBuffers(1, bufferIds, 0)

        vertexBufferId = bufferIds[0]

        val velocity = FloatArray(counts * 3)
        for (i in 0 until counts) {
            val fwj = 2 * Math.PI * Math.random()
            val yj = 0.35 * Math.PI * Math.random() + 0.15 * Math.PI
            val vTotal = 1.5 + 1.5 * Math.random()
            val vy = vTotal * sin(yj)
            val vx = vTotal * cos(yj) * sin(fwj)
            val vz = vTotal * cos(yj) * cos(fwj)
            velocity[i * 3] = vx.toFloat()
            velocity[i * 3 + 1] = vy.toFloat()
            velocity[i * 3 + 2] = vz.toFloat()
        }

        vCounts = counts
        verticesBuffer = ByteBuffer.allocateDirect(velocity.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(velocity).position(0) as FloatBuffer

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBufferId)
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, velocity.size * FLOAT_SIZE, verticesBuffer, GLES30.GL_STATIC_DRAW)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("physics_particle/physics_particle_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("physics_particle/physics_particle_fragment.glsl")
        )
    }

    override fun initShaderParams() {
        super.initShaderParams()
        uPointSizeHandle = GLES30.glGetUniformLocation(program, "uPointSize")
        uTimeHandle = GLES30.glGetUniformLocation(program, "uTime")
    }

    override fun drawSelf() {
        val currTimeStamp = System.nanoTime() / 1000000
        if (currTimeStamp - timeStamp >= 10) {
            timeLive += 0.02f
            timeStamp = currTimeStamp
        }

        GLES30.glUseProgram(program)

        GLES30.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)

        GLES30.glUniform1f(uPointSizeHandle, scale)
        GLES30.glUniform1f(uTimeHandle, timeLive)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBufferId)
        GLES30.glVertexAttribPointer(aPosition, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, 0)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)

        GLES30.glEnableVertexAttribArray(aPosition)

        GLES30.glDrawArrays(GLES30.GL_POINTS, 0, vCounts)

        GLES30.glDisableVertexAttribArray(aNormal)
        GLES30.glDisableVertexAttribArray(aPosition)
        GLES30.glDisableVertexAttribArray(aTexCoor)
    }
}