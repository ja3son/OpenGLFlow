package com.ja3son.gllib.util

import android.opengl.Matrix
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

object MatrixState {
    private val projMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private var viewProjMatrix = FloatArray(16)
    private var mvpMatrix = FloatArray(16)
    private val stack = Array(10) { FloatArray(16) }
    private lateinit var currentMatrix: FloatArray
    private var stackTop: Int = -1


    var lightLocation = floatArrayOf(0f, 0f, 0f)
    var lightPositionFBCache = ByteBuffer.allocateDirect(3 * 4)
    var lightPositionFB: FloatBuffer? = null


    var cameraLocation = FloatArray(3)
    var cameraFBCache = ByteBuffer.allocateDirect(3 * 4)
    var cameraFB: FloatBuffer? = null


    var lightDirection = FloatArray(3)
    var lightDirectionFBCache = ByteBuffer.allocateDirect(3 * 4)
    var lightDirectionFB: FloatBuffer? = null

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

    fun matrix(mult: FloatArray) {
        Matrix.multiplyMM(currentMatrix, 0, currentMatrix, 0, mult, 0)
    }

    fun translate(x: Float, y: Float, z: Float) {
        Matrix.translateM(currentMatrix, 0, x, y, z)
    }

    fun rotate(angle: Float, x: Float, y: Float, z: Float) {
        Matrix.rotateM(currentMatrix, 0, angle, x, y, z)
    }

    fun scale(x: Float, y: Float, z: Float) {
        Matrix.scaleM(currentMatrix, 0, x, y, z)
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

        cameraLocation[0] = cx
        cameraLocation[1] = cy
        cameraLocation[2] = cz

        cameraFBCache.clear()
        cameraFBCache.order(ByteOrder.nativeOrder())
        cameraFB = cameraFBCache.asFloatBuffer()
        cameraFB?.put(cameraLocation)
        cameraFB?.position(0)
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

    fun getViewProjMatrix(): FloatArray {
        Matrix.multiplyMM(viewProjMatrix, 0, projMatrix, 0, viewMatrix, 0)
        return viewProjMatrix
    }

    fun getModelMatrix(): FloatArray {
        return currentMatrix
    }

    fun setLightLocation(x: Float, y: Float, z: Float) {
        lightPositionFBCache.clear()

        lightLocation[0] = x
        lightLocation[1] = y
        lightLocation[2] = z

        lightPositionFBCache.order(ByteOrder.nativeOrder())
        lightPositionFB = lightPositionFBCache.asFloatBuffer()
        lightPositionFB?.put(lightLocation)
        lightPositionFB?.position(0)
    }

    fun setLightDirection(x: Float, y: Float, z: Float) {
        lightDirectionFBCache.clear()

        lightDirection[0] = x
        lightDirection[1] = y
        lightDirection[2] = z

        lightDirectionFBCache.order(ByteOrder.nativeOrder())
        lightDirectionFB = lightDirectionFBCache.asFloatBuffer()
        lightDirectionFB?.put(lightDirection)
        lightDirectionFB?.position(0)
    }

    fun insertSelfMatrix(matrixTemp: FloatArray?) {
        val result = FloatArray(16)
        Matrix.multiplyMM(result, 0, currentMatrix, 0, matrixTemp, 0)
        currentMatrix = result
    }
}