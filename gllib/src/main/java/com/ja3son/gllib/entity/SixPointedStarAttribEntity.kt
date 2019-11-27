package com.ja3son.gllib.entity

import android.opengl.GLES30
import android.opengl.Matrix
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class SixPointedStarAttribEntity() : BaseEntity() {

    private var R: Float = 0f
    private var r: Float = 0f
    private var z: Float = 0f
    private lateinit var color: FloatArray

    constructor(r: Float, R: Float, z: Float, color: FloatArray) : this() {
        this.r = r
        this.R = R
        this.z = z
        this.color = color
        init()
    }

    override fun initVertexData() {
        val unitSize = 1.0f
        val first = ArrayList<Float>()
        val tempAngle = (360 / 6).toFloat()

        var angle = 0f
        while (angle < 360) {
            first.add(0f)
            first.add(0f)
            first.add(z)
            first.add((R * unitSize * Math.cos(Math.toRadians(angle.toDouble()))).toFloat())
            first.add((R * unitSize * Math.sin(Math.toRadians(angle.toDouble()))).toFloat())
            first.add(z)
            first.add((r * unitSize * Math.cos(Math.toRadians((angle + tempAngle / 2).toDouble()))).toFloat())
            first.add((r * unitSize * Math.sin(Math.toRadians((angle + tempAngle / 2).toDouble()))).toFloat())
            first.add(z)

            first.add(0f)
            first.add(0f)
            first.add(z)
            first.add((r * unitSize * Math.cos(Math.toRadians((angle + tempAngle / 2).toDouble()))).toFloat())
            first.add((r * unitSize * Math.sin(Math.toRadians((angle + tempAngle / 2).toDouble()))).toFloat())
            first.add(z)
            first.add((R * unitSize * Math.cos(Math.toRadians((angle + tempAngle).toDouble()))).toFloat())
            first.add((R * unitSize * Math.sin(Math.toRadians((angle + tempAngle).toDouble()))).toFloat())
            first.add(z)
            angle += tempAngle
        }

        vCounts = first.size / 3
        val vertexArray = FloatArray(first.size)
        for (i in 0 until vCounts) {
            vertexArray[i * 3] = first[i * 3]
            vertexArray[i * 3 + 1] = first[i * 3 + 1]
            vertexArray[i * 3 + 2] = first[i * 3 + 2]
        }

        verticesBuffer = ByteBuffer.allocateDirect(vertexArray.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(vertexArray).position(0) as FloatBuffer
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("triangle_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("triangle_fragment.glsl")
        )
    }

    override fun initShaderParams() {
        aPosition = GLES30.glGetAttribLocation(program, "aPosition")
        aColor = GLES30.glGetAttribLocation(program, "aColor")
        uMVPMatrix = GLES30.glGetUniformLocation(program, "uMVPMatrix")
    }

    override fun drawSelf() {
        Matrix.setRotateM(modelMatrix, 0, 0.0f, 0f, 1f, 0f)
        Matrix.translateM(modelMatrix, 0, 0f, 0f, 1f)
        Matrix.rotateM(modelMatrix, 0, yAngle, 0f, 1f, 0f)
        Matrix.rotateM(modelMatrix, 0, xAngle, 1f, 0f, 0f)
        GLES30.glUseProgram(program)
        GLES30.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(modelMatrix), 0)
        GLES30.glVertexAttribPointer(aPosition, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, verticesBuffer)
        GLES30.glVertexAttrib4f(aColor, color[0], color[1], color[2], 1f)
        GLES30.glEnableVertexAttribArray(aPosition)
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCounts)
    }
}