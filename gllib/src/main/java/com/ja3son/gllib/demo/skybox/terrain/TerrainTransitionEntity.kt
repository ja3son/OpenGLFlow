package com.ja3son.gllib.demo.skybox.terrain

import android.opengl.GLES30
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


class TerrainTransitionEntity(private val rows: Int, private val cols: Int,
                              private val texArray: IntArray) : BaseEntity() {
    override var UNIT_SIZE = 3.0f //每个小格尺寸

    private var texCount: Int = -1

    private var startY: Int = -1
    private var ySpan: Int = -1

    private var lowest: Int = -1
    private var highest: Int = -1

    private var sTextureOne: Int = -1
    private var sTextureTwo: Int = -1
    private var sTextureThree: Int = -1

    init {
        init()
    }

    override fun initVertexData() {
        vCounts = cols * rows * 2 * 3 //每个矩形2个三角形，每个三角形3个顶点
        val vertices = FloatArray(vCounts * 3)
        var count = 0
        for (j in 0 until rows) {
            for (i in 0 until cols) {
                val zsx = -UNIT_SIZE * cols / 2 + i * UNIT_SIZE //左上角x坐标
                val zsz = -UNIT_SIZE * rows / 2 + j * UNIT_SIZE //左上角z坐标

                vertices[count++] = zsx
                vertices[count++] = 0f
                vertices[count++] = zsz

                vertices[count++] = zsx
                vertices[count++] = 0f
                vertices[count++] = zsz + UNIT_SIZE

                vertices[count++] = zsx + UNIT_SIZE
                vertices[count++] = 0f
                vertices[count++] = zsz

                vertices[count++] = zsx + UNIT_SIZE
                vertices[count++] = 0f
                vertices[count++] = zsz

                vertices[count++] = zsx
                vertices[count++] = 0f
                vertices[count++] = zsz + UNIT_SIZE

                vertices[count++] = zsx + UNIT_SIZE
                vertices[count++] = 0f
                vertices[count++] = zsz + UNIT_SIZE
            }
        }

        val texCoor = generateTexCoor(cols, rows)

        verticesBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(vertices).position(0) as FloatBuffer

        texCoorBuffer = ByteBuffer.allocateDirect(texCoor.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(texCoor).position(0) as FloatBuffer
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("terrain_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("terrain_fragment.glsl")
        )
    }

    override fun initShaderParams() {
        aPosition = GLES30.glGetAttribLocation(program, "aPosition")
        aTexCoor = GLES30.glGetAttribLocation(program, "aTexCoor")
        uMVPMatrix = GLES30.glGetUniformLocation(program, "uMVPMatrix")

        texCount = GLES30.glGetUniformLocation(program, "texCount")

        startY = GLES30.glGetUniformLocation(program, "startY")
        ySpan = GLES30.glGetUniformLocation(program, "ySpan")

        lowest = GLES30.glGetUniformLocation(program, "lowest")
        highest = GLES30.glGetUniformLocation(program, "highest")

        sTextureOne = GLES30.glGetUniformLocation(program, "sTextureOne")
        sTextureTwo = GLES30.glGetUniformLocation(program, "sTextureTwo")
        sTextureThree = GLES30.glGetUniformLocation(program, "sTextureThree")
    }

    override fun drawSelf() {
        GLES30.glUseProgram(program)
        MatrixState.setInitStack()
        MatrixState.translate(0f, 0f, 1f)
        MatrixState.rotate(yAngle, 0f, 1f, 0f)
        MatrixState.rotate(xAngle, 1f, 0f, 0f)
        GLES30.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES30.glVertexAttribPointer(aPosition, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, verticesBuffer)
        GLES30.glVertexAttribPointer(aTexCoor, texLen, GLES30.GL_FLOAT, false, texLen * FLOAT_SIZE, texCoorBuffer)
        GLES30.glEnableVertexAttribArray(aPosition)
        GLES30.glEnableVertexAttribArray(aTexCoor)
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texArray[0])
        GLES30.glUniform1i(sTextureOne, 0)

        GLES30.glActiveTexture(GLES30.GL_TEXTURE1)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texArray[1])
        GLES30.glUniform1i(sTextureTwo, 1)

        GLES30.glActiveTexture(GLES30.GL_TEXTURE2)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texArray[2])
        GLES30.glUniform1i(sTextureThree, 2)

        GLES30.glUniform1f(startY, 0f)
        GLES30.glUniform1f(ySpan, 30f)

        GLES30.glUniform1f(lowest, -2f)
        GLES30.glUniform1f(highest, 40f)

        GLES30.glUniform1f(texCount, 16f)

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCounts)
    }

    private fun generateTexCoor(rows: Int, cols: Int): FloatArray {
        val result = FloatArray(rows * cols * 6 * 2) //顶点数 * 2
        val sizew = 1f / rows
        val sizeh = 1f / cols
        var c = 0
        for (i in 0 until cols) {
            for (j in 0 until rows) {
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
}