package com.ja3son.gllib.entity

import android.opengl.GLES30
import android.opengl.Matrix
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.FloatBuffer

abstract class BaseEntity {
    protected val FLOAT_SIZE: Int = 4
    protected open val UNIT_SIZE: Float = 1f

    protected var program: Int = 0
    protected var aPosition: Int = 0
    protected var aTexCoor: Int = 0
    protected var aColor: Int = 0
    protected var uMVPMatrix: Int = 0

    protected var uRHandler: Int = 0
    protected var uMMatrix: Int = 0
    protected var aNormal: Int = 0
    protected var uLightDirection: Int = 0
    protected var uLightLocation: Int = 0
    protected var uCamera: Int = 0


    protected var modelMatrix: FloatArray = FloatArray(16)
    protected val posLen: Int = 3
    protected val pos4Len: Int = 4
    protected val texLen: Int = 2
    protected val colorLen: Int = 4
    protected var vCounts: Int = 0
    protected var iCounts: Int = 0
    var yAngle = 0f
    var xAngle = 0f
    var touchIndex = 0

    protected lateinit var verticesBuffer: FloatBuffer
    protected lateinit var texCoorBuffer: FloatBuffer
    protected lateinit var noiseCoorBuffer: FloatBuffer
    protected lateinit var normalBuffer: FloatBuffer
    protected lateinit var indicesBuffer: ByteBuffer
    protected lateinit var colorsBuffer: FloatBuffer

    fun init() {
        initData()

        initVertexData()
        initShader()
        initShaderParams()
    }

    private fun initData() {
        Matrix.setIdentityM(modelMatrix, 0)
    }

    open fun initVertexData() {

    }

    open fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("triangle_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("triangle_fragment.glsl")
        )
    }

    open fun initShaderParams() {
        aPosition = GLES30.glGetAttribLocation(program, "aPosition")
        aNormal = GLES30.glGetAttribLocation(program, "aNormal")
        aTexCoor = GLES30.glGetAttribLocation(program, "aTexCoor")
        aColor = GLES30.glGetAttribLocation(program, "aColor")

        uMVPMatrix = GLES30.glGetUniformLocation(program, "uMVPMatrix")
        uCamera = GLES30.glGetUniformLocation(program, "uCamera")
        uMMatrix = GLES30.glGetUniformLocation(program, "uMMatrix")
        uLightLocation = GLES30.glGetUniformLocation(program, "uLightLocation")
    }

    open fun drawSelf() {
        GLES30.glUseProgram(program)
        GLES30.glVertexAttribPointer(aPosition, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, verticesBuffer)
        GLES30.glVertexAttribPointer(aColor, colorLen, GLES30.GL_FLOAT, false, colorLen * FLOAT_SIZE, colorsBuffer)
        GLES30.glEnableVertexAttribArray(aPosition)
        GLES30.glEnableVertexAttribArray(aColor)
    }

    open fun drawSelf(textureId: Int) {
        MatrixState.rotate(xAngle, 1f, 0f, 0f)
        MatrixState.rotate(yAngle, 0f, 1f, 0f)

        GLES30.glUseProgram(program)
        GLES30.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES30.glUniformMatrix4fv(uMMatrix, 1, false, MatrixState.getModelMatrix(), 0)
        GLES30.glUniform3fv(uCamera, 1, MatrixState.cameraFB)
    }

    open fun drawSelf(textureBG: Int, textureNormal: Int) {
        MatrixState.rotate(xAngle, 1f, 0f, 0f)
        MatrixState.rotate(yAngle, 0f, 1f, 0f)

        GLES30.glUseProgram(program)
        GLES30.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES30.glUniformMatrix4fv(uMMatrix, 1, false, MatrixState.getModelMatrix(), 0)
        GLES30.glUniform3fv(uCamera, 1, MatrixState.cameraFB)
    }
}