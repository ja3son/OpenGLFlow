package com.ja3son.gllib.util

import android.opengl.Matrix

object MatrixState {
    private val projMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private var mvpMatrix = FloatArray(16)
    private val stack = Array(10) { FloatArray(16) }
    private lateinit var currentMatrix: FloatArray
    private var stackTop: Int = -1

    fun setInitStack() {
        currentMatrix = FloatArray(16)
        Matrix.setRotateM(currentMatrix, 0, 0f, 1f, 0f, 0f)
    }

    fun pushMatrix() {
        stackTop++
        for (i in 0 until 16) {
            stack[stackTop][i] = currentMatrix[i]
        }
    }

    fun popMatrix() {
        for (i in 0 until 16) {
            currentMatrix[i] = stack[stackTop][i]
        }
        stackTop--
    }

    fun translate(x: Float, y: Float, z: Float) {
        Matrix.translateM(currentMatrix, 0, x, y, z)
    }

    fun setCamera(
            cx: Float,
            cy: Float,
            cz: Float,
            tx: Float,
            ty: Float,
            tz: Float,
            upx: Float,
            upy: Float,
            upz: Float
    ) {
        Matrix.setLookAtM(
                viewMatrix,
                0,
                cx, cy, cz,
                tx, ty, tz,
                upx, upy, upz
        )
    }

    fun setProjectOrtho(
            left: Float,
            right: Float,
            bottom: Float,
            top: Float,
            near: Float,
            far: Float) {
        Matrix.orthoM(
                projMatrix,
                0,
                left, right,
                bottom, top,
                near, far
        )
    }

    fun setProjectFrustum(
            left: Float,
            right: Float,
            bottom: Float,
            top: Float,
            near: Float,
            far: Float) {
        Matrix.frustumM(projMatrix, 0, left, right, bottom, top, near, far)
    }

    fun getFinalMatrix(spec: FloatArray): FloatArray {
        mvpMatrix = FloatArray(16)
        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, spec, 0)
        Matrix.multiplyMM(mvpMatrix, 0, projMatrix, 0, mvpMatrix, 0)
        return mvpMatrix
    }

    fun getFinalMatrix(): FloatArray {
        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, currentMatrix, 0)
        Matrix.multiplyMM(mvpMatrix, 0, projMatrix, 0, mvpMatrix, 0)
        return mvpMatrix
    }
}