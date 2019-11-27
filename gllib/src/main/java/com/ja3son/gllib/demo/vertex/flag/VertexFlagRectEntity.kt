package com.ja3son.gllib.demo.vertex.flag

import android.opengl.GLES30
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class VertexFlagRectEntity(private val type: Int) : BaseEntity() {

    val bufferIds = IntArray(2)
    var vertexBufferId = 0
    var texCoordBufferId = 0
    var currStartAngle = 0f

    var uWidthSpan = 0
    var uStartAngle = 0
    val WIDTH_SPAN = 3.3f

    init {
        init()
        object : Thread() {
            override fun run() {
                while (true) {
                    currStartAngle += (Math.PI / 16f).toFloat()
                    try {
                        sleep(50)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
        }.start()
    }

    override fun initVertexData() {
        val cols = 12
        val rows = cols * 3 / 4

        val UNIT_SIZE = WIDTH_SPAN / cols

        vCounts = cols * rows * 6
        val vertices = FloatArray(vCounts * 3)
        var count = 0
        for (j in 0 until rows) {
            for (i in 0 until cols) {
                val x = -UNIT_SIZE * cols / 2 + i * UNIT_SIZE
                val y = UNIT_SIZE * rows / 2 - j * UNIT_SIZE
                val z = 0f

                vertices[count++] = x
                vertices[count++] = y
                vertices[count++] = z

                vertices[count++] = x
                vertices[count++] = y - UNIT_SIZE
                vertices[count++] = z

                vertices[count++] = x + UNIT_SIZE
                vertices[count++] = y
                vertices[count++] = z

                vertices[count++] = x + UNIT_SIZE
                vertices[count++] = y
                vertices[count++] = z

                vertices[count++] = x
                vertices[count++] = y - UNIT_SIZE
                vertices[count++] = z

                vertices[count++] = x + UNIT_SIZE
                vertices[count++] = y - UNIT_SIZE
                vertices[count++] = z
            }
        }

        val texCoords = FloatArray(cols * rows * 6 * 2)
        val w = 1.0f / cols
        val h = 0.75f / rows
        count = 0
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                val s = j * w
                val t = i * h

                texCoords[count++] = s
                texCoords[count++] = t

                texCoords[count++] = s
                texCoords[count++] = t + h

                texCoords[count++] = s + w
                texCoords[count++] = t


                texCoords[count++] = s + w
                texCoords[count++] = t

                texCoords[count++] = s
                texCoords[count++] = t + h

                texCoords[count++] = s + w
                texCoords[count++] = t + h
            }
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
        when (type) {
            0 -> {
                program = ShaderUtils.createProgram(
                        ShaderUtils.loadFromAssetsFile("anim_x_flag_vertex.glsl"),
                        ShaderUtils.loadFromAssetsFile("triangle_tex_fragment.glsl")
                )
            }
            1 -> {
                program = ShaderUtils.createProgram(
                        ShaderUtils.loadFromAssetsFile("anim_y_flag_vertex.glsl"),
                        ShaderUtils.loadFromAssetsFile("triangle_tex_fragment.glsl")
                )
            }
            2 -> {
                program = ShaderUtils.createProgram(
                        ShaderUtils.loadFromAssetsFile("anim_xy_flag_vertex.glsl"),
                        ShaderUtils.loadFromAssetsFile("triangle_tex_fragment.glsl")
                )
            }
        }
    }

    override fun initShaderParams() {
        aPosition = GLES30.glGetAttribLocation(program, "aPosition")
        aTexCoor = GLES30.glGetAttribLocation(program, "aTexCoor")
        uStartAngle = GLES30.glGetUniformLocation(program, "uStartAngle")
        uWidthSpan = GLES30.glGetUniformLocation(program, "uWidthSpan")
    }

    override fun drawSelf(textureId: Int) {
        MatrixState.rotate(xAngle, 1f, 0f, 0f)
        MatrixState.rotate(yAngle, 0f, 1f, 0f)
        GLES30.glUseProgram(program)
        GLES30.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES30.glUniform1f(uStartAngle, currStartAngle)
        GLES30.glUniform1f(uWidthSpan, WIDTH_SPAN)

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
    }
}