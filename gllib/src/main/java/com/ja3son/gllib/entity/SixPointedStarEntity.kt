package com.ja3son.gllib.entity

import android.opengl.GLES32
import android.opengl.Matrix
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class SixPointedStarEntity() : BaseEntity() {

    private var R: Float = 0f
    private var r: Float = 0f
    private var z: Float = 0f

    constructor(r: Float, R: Float, z: Float) : this() {
        this.r = r
        this.R = R
        this.z = z
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

        counts = first.size / 3
        val vertexArray = FloatArray(first.size)
        for (i in 0 until counts) {
            vertexArray[i * 3] = first[i * 3]
            vertexArray[i * 3 + 1] = first[i * 3 + 1]
            vertexArray[i * 3 + 2] = first[i * 3 + 2]
        }

        val colorArray = FloatArray(counts * 4)
        for (i in 0 until counts) {
            if (i % 3 == 0) {
                colorArray[i * 4] = 1f
                colorArray[i * 4 + 1] = 1f
                colorArray[i * 4 + 2] = 1f
                colorArray[i * 4 + 3] = 0f
            } else {
                colorArray[i * 4] = 0.45f
                colorArray[i * 4 + 1] = 0.75f
                colorArray[i * 4 + 2] = 0.75f
                colorArray[i * 4 + 3] = 0f
            }
        }

        verticesBuffer = ByteBuffer.allocateDirect(vertexArray.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(vertexArray).position(0) as FloatBuffer


        colorsBuffer = ByteBuffer.allocateDirect(colorArray.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(colorArray).position(0) as FloatBuffer
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("triangle_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("triangle_fragment.glsl")
        )
    }

    override fun initShaderParams() {
        aPosition = GLES32.glGetAttribLocation(program, "aPosition")
        aColor = GLES32.glGetAttribLocation(program, "aColor")
        uMVPMatrix = GLES32.glGetUniformLocation(program, "uMVPMatrix")
    }

    override fun drawSelf() {
        Matrix.setRotateM(modelMatrix, 0, 0.0f, 0f, 1f, 0f)
        Matrix.translateM(modelMatrix, 0, 0f, 0f, 1f)
        Matrix.rotateM(modelMatrix, 0, yAngle, 0f, 1f, 0f)
        Matrix.rotateM(modelMatrix, 0, xAngle, 1f, 0f, 0f)
        GLES32.glUseProgram(program)
        GLES32.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(modelMatrix), 0)
        GLES32.glVertexAttribPointer(aPosition, posLen, GLES32.GL_FLOAT, false, posLen * FLOAT_SIZE, verticesBuffer)
        GLES32.glVertexAttribPointer(aColor, colorLen, GLES32.GL_FLOAT, false, colorLen * FLOAT_SIZE, colorsBuffer)
        GLES32.glEnableVertexAttribArray(aPosition)
        GLES32.glEnableVertexAttribArray(aColor)
        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0, counts)
    }
}