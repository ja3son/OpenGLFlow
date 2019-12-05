package com.ja3son.gllib.demo.light.shadow_texture

import android.opengl.GLES30
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.OBJUtils
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class ShadowTextureEntity(private val fName: String, private val useFace: Boolean) : BaseEntity() {

    val bufferIds = IntArray(2)
    var vertexBufferId = 0
    var normalBufferId = 0

    var uIsShadow = 0
    var uLightViewProjatrix = 0

    init {
        init()
    }

    override fun initVertexData() {
        if (useFace) OBJUtils.loadVertexOnlyFace(fName) else OBJUtils.loadFromFileVertexOnly(fName)
        val vertices = OBJUtils.vXYZ
        val normals = OBJUtils.nXYZ

        GLES30.glGenBuffers(2, bufferIds, 0)

        vertexBufferId = bufferIds[0]
        normalBufferId = bufferIds[1]

        if (vertices != null) {
            vCounts = vertices.size / 3
            verticesBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer()
                    .put(vertices).position(0) as FloatBuffer

            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBufferId)
            GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertices.size * FLOAT_SIZE, verticesBuffer, GLES30.GL_STATIC_DRAW)
        }

        if (normals != null) {
            normalBuffer = ByteBuffer.allocateDirect(normals.size * FLOAT_SIZE)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer()
                    .put(normals).position(0) as FloatBuffer

            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, normalBufferId)
            GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, normals.size * FLOAT_SIZE, normalBuffer, GLES30.GL_STATIC_DRAW)
        }

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)
    }

    override fun initShader() {
        program = if (useFace) {
            ShaderUtils.createProgram(
                    ShaderUtils.loadFromAssetsFile("shadow_light/shadow_tex_land_vertex.glsl"),
                    ShaderUtils.loadFromAssetsFile("shadow_light/shadow_tex_land_fragment.glsl")
            )
        } else {
            ShaderUtils.createProgram(
                    ShaderUtils.loadFromAssetsFile("shadow_light/shadow_tex_normal_vertex.glsl"),
                    ShaderUtils.loadFromAssetsFile("shadow_light/shadow_tex_normal_fragment.glsl")
            )
        }
    }

    override fun initShaderParams() {
        aPosition = GLES30.glGetAttribLocation(program, "aPosition")
        aNormal = GLES30.glGetAttribLocation(program, "aNormal")

        uMVPMatrix = GLES30.glGetUniformLocation(program, "uMVPMatrix")
        uMMatrix = GLES30.glGetUniformLocation(program, "uMMatrix")
        uLightLocation = GLES30.glGetUniformLocation(program, "uLightLocation")
        uCamera = GLES30.glGetUniformLocation(program, "uCamera")
        uLightViewProjatrix = GLES30.glGetUniformLocation(program, "uLightViewProjatrix")

        if (!useFace) {
            uViewProjatrix = GLES30.glGetUniformLocation(program, "uViewProjatrix")
            uIsShadow = GLES30.glGetUniformLocation(program, "uIsShadow")
        }
    }

    fun drawSelf(textureId: Int, lightViewProjMatrix: FloatArray, isShadow: Int) {
        GLES30.glUseProgram(program)
        GLES30.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES30.glUniformMatrix4fv(uMMatrix, 1, false, MatrixState.getModelMatrix(), 0)
        GLES30.glUniformMatrix4fv(uLightViewProjatrix, 1, false, lightViewProjMatrix, 0)

        if (!useFace) {
            GLES30.glUniformMatrix4fv(uViewProjatrix, 1, false, MatrixState.getViewProjMatrix(), 0)
            GLES30.glUniform1i(uIsShadow, isShadow)
        }

        GLES30.glUniform3fv(uLightLocation, 1, MatrixState.lightPositionFB)
        GLES30.glUniform3fv(uCamera, 1, MatrixState.cameraFB)

        GLES30.glEnableVertexAttribArray(aPosition)
        GLES30.glEnableVertexAttribArray(aNormal)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBufferId)
        GLES30.glVertexAttribPointer(aPosition, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, 0)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, normalBufferId)
        GLES30.glVertexAttribPointer(aNormal, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, 0)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId)

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCounts)

        GLES30.glDisableVertexAttribArray(aPosition)
        GLES30.glDisableVertexAttribArray(aNormal)
    }
}