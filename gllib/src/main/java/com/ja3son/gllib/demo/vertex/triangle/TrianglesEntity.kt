package com.ja3son.gllib.demo.vertex.triangle

import android.opengl.GLES30
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


class TrianglesEntity : BaseEntity() {

    val bufferIds = IntArray(2)
    var vertexBufferId = 0
    var texCoordBufferId = 0
    var edgeLength = 8f
    var levelNum = 40
    var uRatio = 0
    var twistingRatio: Float = 0f
    var symbol = 1
    var currRatio = 0.05f

    init {
        init()
        object : Thread() {
            override fun run() {
                while (true) {
                    twistingRatio += symbol * currRatio
                    if (twistingRatio > 1.0f) {
                        twistingRatio = 1.0f
                        symbol = -symbol
                    }
                    if (twistingRatio < -1.0f) {
                        twistingRatio = -1.0f
                        symbol = -symbol
                    }
                    try {
                        sleep(100)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }
        }.start()
    }

    override fun initVertexData() {
        GLES30.glGenBuffers(2, bufferIds, 0)
        vertexBufferId = bufferIds[0]
        texCoordBufferId = bufferIds[1]

        val al_vertex = ArrayList<Float>()
        val al_texture = ArrayList<Float>()
        val perLength = edgeLength / levelNum
        for (i in 0 until levelNum) {
            val currBottomEdgeNum = i + 1
            val currTrangleHeight = (perLength * Math.sin(Math.PI / 3)).toFloat()
            val topEdgeFirstPointX = -perLength * i / 2
            val topEdgeFirstPointY = -i * currTrangleHeight
            val topEdgeFirstPointZ = 0f

            val bottomEdgeFirstPointX = -perLength * currBottomEdgeNum / 2
            val bottomEdgeFirstPointY = -(i + 1) * currTrangleHeight
            val bottomEdgeFirstPointZ = 0f
            val horSpan = 1 / levelNum.toFloat()
            val verSpan = 1 / levelNum.toFloat()
            val topFirstS = 0.5f - i * horSpan / 2
            val topFirstT = i * verSpan
            val bottomFirstS = 0.5f - currBottomEdgeNum * horSpan / 2
            val bottomFirstT = (i + 1) * verSpan
            for (j in 0 until currBottomEdgeNum) {
                val topX = topEdgeFirstPointX + j * perLength
                val topS = topFirstS + j * horSpan
                val leftBottomX = bottomEdgeFirstPointX + j * perLength
                val leftBottomS = bottomFirstS + j * horSpan
                val rightBottomX = leftBottomX + perLength
                val rightBottomS = leftBottomS + horSpan
                al_vertex.add(topX)
                al_vertex.add(topEdgeFirstPointY)
                al_vertex.add(topEdgeFirstPointZ)
                al_vertex.add(leftBottomX)
                al_vertex.add(bottomEdgeFirstPointY)
                al_vertex.add(bottomEdgeFirstPointZ)
                al_vertex.add(rightBottomX)
                al_vertex.add(bottomEdgeFirstPointY)
                al_vertex.add(bottomEdgeFirstPointZ)
                al_texture.add(topS)
                al_texture.add(topFirstT)
                al_texture.add(leftBottomS)
                al_texture.add(bottomFirstT)
                al_texture.add(rightBottomS)
                al_texture.add(bottomFirstT)
            }
            for (k in 0 until i) {
                val leftTopX = topEdgeFirstPointX + k * perLength
                val leftTopS = topFirstS + k * horSpan

                val bottomX = bottomEdgeFirstPointX + (k + 1) * perLength
                val bottomS = bottomFirstS + (k + 1) * horSpan

                val rightTopX = leftTopX + perLength
                val rightTopS = leftTopS + horSpan
                al_vertex.add(leftTopX)
                al_vertex.add(topEdgeFirstPointY)
                al_vertex.add(topEdgeFirstPointZ)
                al_vertex.add(bottomX)
                al_vertex.add(bottomEdgeFirstPointY)
                al_vertex.add(bottomEdgeFirstPointZ)
                al_vertex.add(rightTopX)
                al_vertex.add(topEdgeFirstPointY)
                al_vertex.add(topEdgeFirstPointZ)

                al_texture.add(leftTopS)
                al_texture.add(topFirstT)
                al_texture.add(bottomS)
                al_texture.add(bottomFirstT)
                al_texture.add(rightTopS)
                al_texture.add(topFirstT)
            }
        }
        val vertexSize = al_vertex.size
        vCounts = vertexSize / 3
        val vertices = FloatArray(vertexSize)
        for (i in 0 until vertexSize) {
            vertices[i] = al_vertex[i]
        }
        val textureSize = al_texture.size

        val texCoords = FloatArray(textureSize)
        for (i in 0 until textureSize) {
            texCoords[i] = al_texture[i]
        }
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
                ShaderUtils.loadFromAssetsFile("triangle_anim_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("triangle_anim_fragment.glsl")
        )
    }

    override fun initShaderParams() {
        aPosition = GLES30.glGetAttribLocation(program, "aPosition")
        aTexCoor = GLES30.glGetAttribLocation(program, "aTexCoor")
        uRatio = GLES30.glGetUniformLocation(program, "ratio")
    }

    override fun drawSelf(textureId: Int) {
        MatrixState.rotate(xAngle, 1f, 0f, 0f)
        MatrixState.rotate(yAngle, 0f, 1f, 0f)
        GLES30.glUseProgram(program)
        GLES30.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES30.glUniform1f(uRatio, twistingRatio)

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