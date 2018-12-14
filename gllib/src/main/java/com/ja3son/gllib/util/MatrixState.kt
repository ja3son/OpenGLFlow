package com.ja3son.gllib.util

import android.opengl.Matrix

object MatrixState {
    private val projMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private lateinit var mvpMatrix: FloatArray

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
}