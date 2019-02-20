package com.ja3son.gllib.entity

import android.opengl.GLES32
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class ColorRectEntity : BaseEntity() {

    init {
        init()
    }

    override fun initVertexData() {
        vCounts = 6
        val vertices = floatArrayOf(0f, 0f, 0f, UNIT_SIZE, UNIT_SIZE, 0f, -UNIT_SIZE, UNIT_SIZE, 0f, -UNIT_SIZE, -UNIT_SIZE, 0f, UNIT_SIZE, -UNIT_SIZE, 0f, UNIT_SIZE, UNIT_SIZE, 0f)

        val colors = floatArrayOf(1f, 1f, 1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f, 0f)

        verticesBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(vertices).position(0) as FloatBuffer

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
        uMMatrix = GLES32.glGetUniformLocation(program, "uMMatrix")
    }

    override fun drawSelf() {
        super.drawSelf()
        GLES32.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES32.glUniformMatrix4fv(uMMatrix, 1, false, MatrixState.getModelMatrix(), 0)
        GLES32.glDrawArrays(GLES32.GL_TRIANGLE_FAN, 0, vCounts)
    }
}