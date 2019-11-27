package com.ja3son.gllib.entity

import android.opengl.GLES30
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class ColorInRectEntity(val unitSize: Float, val colorIn: FloatArray) : BaseEntity() {

    init {
        init()
    }

    override fun initVertexData() {
        vCounts = 6
        val vertices = floatArrayOf(0f, 0f, 0f, unitSize, unitSize, 0f, -unitSize, unitSize, 0f, -unitSize, -unitSize, 0f, unitSize, -unitSize, 0f, unitSize, unitSize, 0f)

        verticesBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(vertices).position(0) as FloatBuffer

        val colors = floatArrayOf(
                colorIn[0], colorIn[1], colorIn[2], colorIn[3],
                colorIn[0], colorIn[1], colorIn[2], colorIn[3],
                colorIn[0], colorIn[1], colorIn[2], colorIn[3],
                colorIn[0], colorIn[1], colorIn[2], colorIn[3],
                colorIn[0], colorIn[1], colorIn[2], colorIn[3],
                colorIn[0], colorIn[1], colorIn[2], colorIn[3]
        )

        colorsBuffer = ByteBuffer.allocateDirect(colors.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(colors).position(0) as FloatBuffer
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("triangle_model_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("color_rect_fragment.glsl")
        )
    }

    override fun initShaderParams() {
        super.initShaderParams()
        uMMatrix = GLES30.glGetUniformLocation(program, "uMMatrix")
    }

    override fun drawSelf() {
        super.drawSelf()
        GLES30.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES30.glUniformMatrix4fv(uMMatrix, 1, false, MatrixState.getModelMatrix(), 0)
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_FAN, 0, vCounts)
    }
}