package com.ja3son.gllib.entity

import android.opengl.GLES32
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class TDTexEntity(val textureId: Int) : BaseEntity() {
    var xSize = 0.2f
    var y1Size = 0.1f
    var y2Size = 0.2f
    var y3Size = 0.3f
    var y4Size = 0.4f
    var z1Size = 0.4f
    var z2Size = 0.3f
    var z3Size = 0.2f
    var z4Size = 0.1f

    init {
        init()
    }

    override fun initVertexData() {
        val vertices = floatArrayOf(
                //第四个（最下面）立方体的下面
                xSize, 0f, 0f, xSize, 0f, z1Size, -xSize, 0f, z1Size, -xSize, 0f, z1Size, -xSize, 0f, 0f, xSize, 0f, 0f,
                //第四个（最下面）立方体的上面
                xSize, y1Size, 0f, -xSize, y1Size, 0f, -xSize, y1Size, z1Size, -xSize, y1Size, z1Size, xSize, y1Size, z1Size, xSize, y1Size, 0f,
                //第四个（最下面）立方体的前面
                xSize, y1Size, z1Size, -xSize, y1Size, z1Size, -xSize, 0f, z1Size, -xSize, 0f, z1Size, xSize, 0f, z1Size, xSize, y1Size, z1Size,
                //第四个（最下面）立方体的后面
                xSize, y1Size, 0f, xSize, 0f, 0f, -xSize, 0f, 0f, -xSize, 0f, 0f, -xSize, y1Size, 0f, xSize, y1Size, 0f,
                //第四个（最下面）立方体的左面
                -xSize, y1Size, z1Size, -xSize, y1Size, 0f, -xSize, 0f, 0f, -xSize, 0f, 0f, -xSize, 0f, z1Size, -xSize, y1Size, z1Size,
                //第四个（最下面）立方体的右面
                xSize, y1Size, z1Size, xSize, 0f, z1Size, xSize, 0f, 0f, xSize, 0f, 0f, xSize, y1Size, 0f, xSize, y1Size, z1Size,

                //第三个立方体的上面
                xSize, y2Size, 0f, -xSize, y2Size, 0f, -xSize, y2Size, z2Size, -xSize, y2Size, z2Size, xSize, y2Size, z2Size, xSize, y2Size, 0f,
                //第三个立方体的前面
                xSize, y2Size, z2Size, -xSize, y2Size, z2Size, -xSize, y1Size, z2Size, -xSize, y1Size, z2Size, xSize, y1Size, z2Size, xSize, y2Size, z2Size,
                //第三个立方体的后面
                xSize, y2Size, 0f, xSize, y1Size, 0f, -xSize, y1Size, 0f, -xSize, y1Size, 0f, -xSize, y2Size, 0f, xSize, y2Size, 0f,
                //第三个立方体的左面
                -xSize, y2Size, z2Size, -xSize, y2Size, 0f, -xSize, y1Size, 0f, -xSize, y1Size, 0f, -xSize, y1Size, z2Size, -xSize, y2Size, z2Size,
                //第三个立方体的右面
                xSize, y2Size, z2Size, xSize, y1Size, z2Size, xSize, y1Size, 0f, xSize, y1Size, 0f, xSize, y2Size, 0f, xSize, y2Size, z2Size,

                //第二个立方体的上面
                xSize, y3Size, 0f, -xSize, y3Size, 0f, -xSize, y3Size, z3Size, -xSize, y3Size, z3Size, xSize, y3Size, z3Size, xSize, y3Size, 0f,
                //第二个立方体的前面
                xSize, y3Size, z3Size, -xSize, y3Size, z3Size, -xSize, y2Size, z3Size, -xSize, y2Size, z3Size, xSize, y2Size, z3Size, xSize, y3Size, z3Size,
                //第二个立方体的后面
                xSize, y3Size, 0f, xSize, y2Size, 0f, -xSize, y2Size, 0f, -xSize, y2Size, 0f, -xSize, y3Size, 0f, xSize, y3Size, 0f,
                //第二个立方体的左面
                -xSize, y3Size, z3Size, -xSize, y3Size, 0f, -xSize, y2Size, 0f, -xSize, y2Size, 0f, -xSize, y2Size, z3Size, -xSize, y3Size, z3Size,
                //第二个立方体的右面
                xSize, y3Size, z3Size, xSize, y2Size, z3Size, xSize, y2Size, 0f, xSize, y2Size, 0f, xSize, y3Size, 0f, xSize, y3Size, z3Size,

                //第一个立方体的上面
                xSize, y4Size, 0f, -xSize, y4Size, 0f, -xSize, y4Size, z4Size, -xSize, y4Size, z4Size, xSize, y4Size, z4Size, xSize, y4Size, 0f,
                //第一个立方体的前面
                xSize, y4Size, z4Size, -xSize, y4Size, z4Size, -xSize, y3Size, z4Size, -xSize, y3Size, z4Size, xSize, y3Size, z4Size, xSize, y4Size, z4Size,
                //第一个立方体的后面
                xSize, y4Size, 0f, xSize, y3Size, 0f, -xSize, y3Size, 0f, -xSize, y3Size, 0f, -xSize, y4Size, 0f, xSize, y4Size, 0f,
                //第一个立方体的左面
                -xSize, y4Size, z4Size, -xSize, y4Size, 0f, -xSize, y3Size, 0f, -xSize, y3Size, 0f, -xSize, y3Size, z4Size, -xSize, y4Size, z4Size,
                //第一个立方体的右面
                xSize, y4Size, z4Size, xSize, y3Size, z4Size, xSize, y3Size, 0f, xSize, y3Size, 0f, xSize, y4Size, 0f, xSize, y4Size, z4Size)
        vCounts = vertices.size / 3


        val normals = floatArrayOf(
                //第四个（最下面）立方体的下面
                0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f,
                //第四个（最下面）立方体的上面
                0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f,
                //第四个（最下面）立方体的前面
                0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f,
                //第四个（最下面）立方体的后面
                0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f,
                //第四个（最下面）立方体的左面
                -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f,
                //第四个（最下面）立方体的右面
                1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f,

                //第三个立方体的上面
                0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f,
                //第三个立方体的前面
                0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f,
                //第三个立方体的后面
                0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f,
                //第三个立方体的左面
                -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f,
                //第三个立方体的右面
                1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f,

                //第二个立方体的上面
                0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f,
                //第二个立方体的前面
                0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f,
                //第二个立方体的后面
                0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f,
                //第二个立方体的左面
                -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f,
                //第二个立方体的右面
                1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f,

                //第一个立方体的上面
                0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f,
                //第一个立方体的前面
                0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f,
                //第一个立方体的后面
                0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f,
                //第一个立方体的左面
                -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f,
                //第一个立方体的右面
                1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f)

        verticesBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(vertices).position(0) as FloatBuffer

        normalBuffer = ByteBuffer.allocateDirect(normals.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(normals).position(0) as FloatBuffer
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("td_tex_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("td_tex_fragment.glsl")
        )
    }

    override fun initShaderParams() {
        aPosition = GLES32.glGetAttribLocation(program, "aPosition")
        aNormal = GLES32.glGetAttribLocation(program, "aNormal")
        uMVPMatrix = GLES32.glGetUniformLocation(program, "uMVPMatrix")
        uMMatrix = GLES32.glGetUniformLocation(program, "uMMatrix")
        uLightLocation = GLES32.glGetUniformLocation(program, "uLightLocation")
        uCamera = GLES32.glGetUniformLocation(program, "uCamera")
    }

    override fun drawSelf() {
        GLES32.glUseProgram(program)
        MatrixState.rotate(yAngle, 0f, 1f, 0f)
        MatrixState.rotate(xAngle, 1f, 0f, 0f)
        GLES32.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES32.glUniformMatrix4fv(uMMatrix, 1, false, MatrixState.getModelMatrix(), 0)
        GLES32.glUniform3fv(uLightLocation, 1, MatrixState.lightPositionFB)
        GLES32.glUniform3fv(uCamera, 1, MatrixState.cameraFB)
        GLES32.glVertexAttribPointer(aPosition, posLen, GLES32.GL_FLOAT, false, posLen * FLOAT_SIZE, verticesBuffer)
        GLES32.glVertexAttribPointer(aNormal, posLen, GLES32.GL_FLOAT, false, posLen * FLOAT_SIZE, normalBuffer)
        GLES32.glEnableVertexAttribArray(aPosition)
        GLES32.glEnableVertexAttribArray(aNormal)
        GLES32.glActiveTexture(GLES32.GL_TEXTURE0)
        GLES32.glBindTexture(GLES32.GL_TEXTURE_3D, textureId)
        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0, vCounts)
    }
}