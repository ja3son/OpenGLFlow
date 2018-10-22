package com.ja3son.gllib.entity

import android.opengl.GLES32
import android.opengl.Matrix
import com.ja3son.gllib.util.ShaderUtils
import org.jetbrains.anko.doAsync
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class Triangle : BaseEntity() {
    private val counts: Int = 3
    private val posLen: Int = 3
    private val colorLen: Int = 4
    private var rotate: Float = 0.0f

    init {
        doAsync {
            while (true) {
                Thread.sleep(20)
                rotate += 0.375f
                Matrix.setIdentityM(modelMatrix, 0)
                Matrix.rotateM(modelMatrix, 0, rotate, 1.0f, 0.0f, 0.0f)
            }
        }
    }

    override fun initVertexData() {
        val vertices: FloatArray = floatArrayOf(
                -4.0f, 0.0f, 0.0f,
                0.0f, -4.0f, 0.0f,
                4.0f, 0.0f, 0.0f
        )

        val colors: FloatArray = floatArrayOf(
                1.0f, 1.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f
        )

        verticesBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(vertices).position(0) as FloatBuffer

        colorsBuffer = ByteBuffer.allocateDirect(colors.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(colors).position(0) as FloatBuffer
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("fragment.glsl")
        )
    }

    override fun initShaderParams() {
        aPosition = GLES32.glGetAttribLocation(program, "aPosition")
        aColor = GLES32.glGetAttribLocation(program, "aColor")
        uMVPMatrix = GLES32.glGetUniformLocation(program, "uMVPMatrix")
    }

    override fun drawSelf() {
        super.drawSelf()
        GLES32.glUseProgram(program)
        GLES32.glUniformMatrix4fv(uMVPMatrix, 1, false, mvpMatrix, 0)
        GLES32.glVertexAttribPointer(aPosition, posLen, GLES32.GL_FLOAT, false, posLen * FLOAT_SIZE, verticesBuffer)
        GLES32.glVertexAttribPointer(aColor, colorLen, GLES32.GL_FLOAT, false, colorLen * FLOAT_SIZE, colorsBuffer)
        GLES32.glEnableVertexAttribArray(aPosition)
        GLES32.glEnableVertexAttribArray(aColor)
        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0, counts)
    }
}