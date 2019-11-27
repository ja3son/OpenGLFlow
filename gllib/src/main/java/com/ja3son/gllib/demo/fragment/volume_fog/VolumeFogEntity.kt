package com.ja3son.gllib.demo.fragment.volume_fog

import android.opengl.GLES30
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class VolumeFogEntity(private val rows: Int, private val cols: Int,
                      private val texArray: IntArray, private val yArray: Array<FloatArray>) : BaseEntity() {
    override var UNIT_SIZE = 3.0f
    private var startAngleValue: Float = 0.0f

    private var uCameraLocation: Int = -1
    private var slabY: Int = -1
    private var startAngle: Int = -1

    private var sTextureGrass: Int = -1
    private var sTextureRock: Int = -1
    private var landStartY: Int = -1
    private var landYSpan: Int = -1

    init {
        init()
    }

    override fun initVertexData() {
        vCounts = cols * rows * 2 * 3
        val vertices = FloatArray(vCounts * 3)
        var count = 0
        for (j in 0 until rows) {
            for (i in 0 until cols) {
                val zsx = -UNIT_SIZE * cols / 2 + i * UNIT_SIZE
                val zsz = -UNIT_SIZE * rows / 2 + j * UNIT_SIZE

                vertices[count++] = zsx
                vertices[count++] = yArray[j][i]
                vertices[count++] = zsz

                vertices[count++] = zsx
                vertices[count++] = yArray[j + 1][i]
                vertices[count++] = zsz + UNIT_SIZE

                vertices[count++] = zsx + UNIT_SIZE
                vertices[count++] = yArray[j][i + 1]
                vertices[count++] = zsz

                vertices[count++] = zsx + UNIT_SIZE
                vertices[count++] = yArray[j][i + 1]
                vertices[count++] = zsz

                vertices[count++] = zsx
                vertices[count++] = yArray[j + 1][i]
                vertices[count++] = zsz + UNIT_SIZE

                vertices[count++] = zsx + UNIT_SIZE
                vertices[count++] = yArray[j + 1][i + 1]
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
                ShaderUtils.loadFromAssetsFile("volume_fog_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("volume_fog_fragment.glsl")
        )
    }

    override fun initShaderParams() {
        aPosition = GLES30.glGetAttribLocation(program, "aPosition")
        aTexCoor = GLES30.glGetAttribLocation(program, "aTexCoor")
        uMVPMatrix = GLES30.glGetUniformLocation(program, "uMVPMatrix")
        uMMatrix = GLES30.glGetUniformLocation(program, "uMMatrix")

        uCameraLocation = GLES30.glGetUniformLocation(program, "uCamaraLocation")
        slabY = GLES30.glGetUniformLocation(program, "slabY")
        startAngle = GLES30.glGetUniformLocation(program, "startAngle")

        sTextureGrass = GLES30.glGetUniformLocation(program, "sTextureGrass")
        sTextureRock = GLES30.glGetUniformLocation(program, "sTextureRock")
        landStartY = GLES30.glGetUniformLocation(program, "landStartY")
        landYSpan = GLES30.glGetUniformLocation(program, "landYSpan")
    }

    override fun drawSelf() {
        GLES30.glUseProgram(program)
        MatrixState.setInitStack()
        MatrixState.translate(0f, 0f, 1f)
        MatrixState.rotate(yAngle, 0f, 1f, 0f)
        MatrixState.rotate(xAngle, 1f, 0f, 0f)

        GLES30.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES30.glUniformMatrix4fv(uMMatrix, 1, false, MatrixState.getModelMatrix(), 0)
        GLES30.glUniform3fv(uCameraLocation, 1, MatrixState.cameraLocation, 0)

        GLES30.glUniform1f(slabY, 8f)
        GLES30.glUniform1f(startAngle, Math.toRadians(startAngleValue.toDouble()).toFloat())
        startAngleValue = (startAngleValue + 3f) % 360.0f


        //绑定纹理
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texArray[0])
        GLES30.glActiveTexture(GLES30.GL_TEXTURE1)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texArray[1])
        GLES30.glUniform1i(sTextureGrass, 0)
        GLES30.glUniform1i(sTextureRock, 1)

        //传送相应的x参数
        GLES30.glUniform1f(landStartY, 0f)
        GLES30.glUniform1f(landYSpan, 50f)

        GLES30.glVertexAttribPointer(aPosition, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, verticesBuffer)
        GLES30.glVertexAttribPointer(aTexCoor, texLen, GLES30.GL_FLOAT, false, texLen * FLOAT_SIZE, texCoorBuffer)
        GLES30.glEnableVertexAttribArray(aPosition)
        GLES30.glEnableVertexAttribArray(aTexCoor)

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCounts)
    }

    private fun generateTexCoor(rows: Int, cols: Int): FloatArray {
        val result = FloatArray(rows * cols * 6 * 2)
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