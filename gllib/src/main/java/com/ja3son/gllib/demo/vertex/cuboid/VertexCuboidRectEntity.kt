package com.ja3son.gllib.demo.vertex.cuboid


import android.opengl.GLES30
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class VertexCuboidRectEntity(
        private val flow: Int,
        private val yMin: Float,
        private val yMax: Float,
        private val ratio: Float
) : BaseEntity() {

    private val bufferIds = IntArray(2)
    private var vertexBufferId = 0
    private var texCoordBufferId = 0

    private var uAngleSpan = 0
    private var uYSpan = 0
    private var uYStart = 0

    internal var angleSpan = 0f
    internal var angleStep = 2f

    init {
        init()
    }

    override fun initVertexData() {
        vCounts = flow * 4 * 6

        val vertices = FloatArray(vCounts * 3)
        val texCoords = FloatArray(vCounts * 2)
        var yStart = yMin
        val ySpan = (yMax - yMin) / flow
        var count = 0
        var tCount = 0


        for (i in 0 until flow) {
            val x1 = -ratio
            val y1 = yStart
            val z1 = ratio

            val x2 = ratio
            val y2 = yStart
            val z2 = ratio

            val x3 = ratio
            val y3 = yStart
            val z3 = -ratio

            val x4 = -ratio
            val y4 = yStart
            val z4 = -ratio

            val x5 = -ratio
            val y5 = yStart + ySpan
            val z5 = ratio

            val x6 = ratio
            val y6 = yStart + ySpan
            val z6 = ratio

            val x7 = ratio
            val y7 = yStart + ySpan
            val z7 = -ratio

            val x8 = -ratio
            val y8 = yStart + ySpan
            val z8 = -ratio

            vertices[count++] = x5
            vertices[count++] = y5
            vertices[count++] = z5
            vertices[count++] = x1
            vertices[count++] = y1
            vertices[count++] = z1
            vertices[count++] = x2
            vertices[count++] = y2
            vertices[count++] = z2

            vertices[count++] = x5
            vertices[count++] = y5
            vertices[count++] = z5
            vertices[count++] = x2
            vertices[count++] = y2
            vertices[count++] = z2
            vertices[count++] = x6
            vertices[count++] = y6
            vertices[count++] = z6
            //
            vertices[count++] = x6
            vertices[count++] = y6
            vertices[count++] = z6
            vertices[count++] = x2
            vertices[count++] = y2
            vertices[count++] = z2
            vertices[count++] = x3
            vertices[count++] = y3
            vertices[count++] = z3

            vertices[count++] = x6
            vertices[count++] = y6
            vertices[count++] = z6
            vertices[count++] = x3
            vertices[count++] = y3
            vertices[count++] = z3
            vertices[count++] = x7
            vertices[count++] = y7
            vertices[count++] = z7

            vertices[count++] = x7
            vertices[count++] = y7
            vertices[count++] = z7
            vertices[count++] = x3
            vertices[count++] = y3
            vertices[count++] = z3
            vertices[count++] = x4
            vertices[count++] = y4
            vertices[count++] = z4

            vertices[count++] = x7
            vertices[count++] = y7
            vertices[count++] = z7
            vertices[count++] = x4
            vertices[count++] = y4
            vertices[count++] = z4
            vertices[count++] = x8
            vertices[count++] = y8
            vertices[count++] = z8

            vertices[count++] = x8
            vertices[count++] = y8
            vertices[count++] = z8
            vertices[count++] = x4
            vertices[count++] = y4
            vertices[count++] = z4
            vertices[count++] = x1
            vertices[count++] = y1
            vertices[count++] = z1

            vertices[count++] = x8
            vertices[count++] = y8
            vertices[count++] = z8
            vertices[count++] = x1
            vertices[count++] = y1
            vertices[count++] = z1
            vertices[count++] = x5
            vertices[count++] = y5
            vertices[count++] = z5

            yStart += ySpan

            texCoords[tCount++] = 0f
            texCoords[tCount++] = 0f
            texCoords[tCount++] = 0f
            texCoords[tCount++] = 1f
            texCoords[tCount++] = 1f
            texCoords[tCount++] = 1f
            texCoords[tCount++] = 0f
            texCoords[tCount++] = 0f
            texCoords[tCount++] = 1f
            texCoords[tCount++] = 1f
            texCoords[tCount++] = 1f
            texCoords[tCount++] = 0f

            texCoords[tCount++] = 0f
            texCoords[tCount++] = 0f
            texCoords[tCount++] = 0f
            texCoords[tCount++] = 1f
            texCoords[tCount++] = 1f
            texCoords[tCount++] = 1f
            texCoords[tCount++] = 0f
            texCoords[tCount++] = 0f
            texCoords[tCount++] = 1f
            texCoords[tCount++] = 1f
            texCoords[tCount++] = 1f
            texCoords[tCount++] = 0f

            texCoords[tCount++] = 0f
            texCoords[tCount++] = 0f
            texCoords[tCount++] = 0f
            texCoords[tCount++] = 1f
            texCoords[tCount++] = 1f
            texCoords[tCount++] = 1f
            texCoords[tCount++] = 0f
            texCoords[tCount++] = 0f
            texCoords[tCount++] = 1f
            texCoords[tCount++] = 1f
            texCoords[tCount++] = 1f
            texCoords[tCount++] = 0f

            texCoords[tCount++] = 0f
            texCoords[tCount++] = 0f
            texCoords[tCount++] = 0f
            texCoords[tCount++] = 1f
            texCoords[tCount++] = 1f
            texCoords[tCount++] = 1f
            texCoords[tCount++] = 0f
            texCoords[tCount++] = 0f
            texCoords[tCount++] = 1f
            texCoords[tCount++] = 1f
            texCoords[tCount++] = 1f
            texCoords[tCount++] = 0f
        }

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
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("anim_cuboid_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("triangle_tex_fragment.glsl")
        )
    }

    override fun initShaderParams() {
        aPosition = GLES30.glGetAttribLocation(program, "aPosition")
        aTexCoor = GLES30.glGetAttribLocation(program, "aTexCoor")
        uAngleSpan = GLES30.glGetUniformLocation(program, "uAngleSpan")
        uYSpan = GLES30.glGetUniformLocation(program, "uYSpan")
        uYStart = GLES30.glGetUniformLocation(program, "uYStart")
    }

    override fun drawSelf(textureId: Int) {
        MatrixState.rotate(xAngle, 1f, 0f, 0f)
        MatrixState.rotate(yAngle, 0f, 1f, 0f)
        GLES30.glUseProgram(program)

        GLES30.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)

        GLES30.glEnableVertexAttribArray(aPosition)
        GLES30.glEnableVertexAttribArray(aTexCoor)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBufferId)
        GLES30.glVertexAttribPointer(aPosition, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, 0)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, texCoordBufferId)
        GLES30.glVertexAttribPointer(aTexCoor, texLen, GLES30.GL_FLOAT, false, texLen * FLOAT_SIZE, 0)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId)

        angleSpan = (angleSpan + Math.toRadians(angleStep.toDouble())).toFloat()
        if (Math.toDegrees(angleSpan.toDouble()) > 90) {
            angleStep = -2f
        } else if (Math.toDegrees(angleSpan.toDouble()) < -90) {
            angleStep = 2f
        }

        GLES30.glUniform1f(uAngleSpan, angleSpan)
        GLES30.glUniform1f(uYStart, yMin)
        GLES30.glUniform1f(uYSpan, yMax - yMin)

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCounts)
    }
}