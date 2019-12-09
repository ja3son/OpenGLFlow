package com.ja3son.gllib.demo.light.lake_reflection

import android.opengl.GLES30
import android.view.MotionEvent
import com.ja3son.gllib.R
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.XANGLE_MAX
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.XANGLE_MIN
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.YANGLE_MAX
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.YANGLE_MIN
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.bottom
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.calculateMainAndMirrorCamera
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.far
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.initProject
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.left
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.mainCameraX
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.mainCameraY
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.mainCameraZ
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.mirrorCameraX
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.mirrorCameraY
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.mirrorCameraZ
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.near
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.right
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.targetX
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.targetY
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.targetZ
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.top
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.upX
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.upY
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.upZ
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class LakeReflectRenderer : BaseRenderer() {
    var textureOne = 0
    var textureTwo = 0
    var textureThree = 0
    var textureFour = 0
    var textureFive = 0
    var textureSix = 0
    var textureSeven = 0
    var textureEight = 0
    var textureNine = 0

    private var viewProjMatrix = FloatArray(16)

    val SHADOW_TEX_WIDTH = 1024

    var frameTextureId = 0
    var frameBuffer = 0
    var renderBuffer = 0

    private var mPreviousY: Float = 0f
    private var mPreviousX: Float = 0f
    private val TOUCH_SCALE_FACTOR = 180.0f / 320.0f

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        entities.add(LakeReflectRectEntity())
        entities.add(LakeReflectEntity("lake_reflection/skybox.obj"))
        entities.add(LakeReflectEntity("lake_reflection/house.obj"))
        entities.add(LakeReflectEntity("lake_reflection/bridge.obj"))
        entities.add(LakeReflectEntity("lake_reflection/bucket.obj"))
        entities.add(LakeReflectEntity("lake_reflection/tree.obj"))
        entities.add(LakeReflectEntity("lake_reflection/table.obj"))
        entities.add(LakeReflectEntity("lake_reflection/woodpile.obj"))
        entities.add(LakeReflectEntity("lake_reflection/mushroom.obj"))
        entities.add(LakeReflectEntity("lake_reflection/tent.obj"))
        entities.add(LakeReflectEntity("lake_reflection/flower.obj"))

        textureOne = ShaderUtils.initTexture(R.drawable.house)
        textureTwo = ShaderUtils.initTexture(R.drawable.water)
        textureThree = ShaderUtils.initTexture(R.drawable.resultnt)
        textureFour = ShaderUtils.initTexture(R.drawable.sky)
        textureFive = ShaderUtils.initTexture(R.drawable.bridge)
        textureSix = ShaderUtils.initTexture(R.drawable.stuff02)
        textureSeven = ShaderUtils.initTexture(R.drawable.tree0)
        textureEight = ShaderUtils.initTexture(R.drawable.stuff01)
        textureNine = ShaderUtils.initTexture(R.drawable.vegetation01)

        calculateMainAndMirrorCamera(0f, 15f)

        object : Thread() {
            override fun run() {
                while (true) {
                    (entities[0] as LakeReflectRectEntity).calVerticesNormalAndTangent()
                    try {
                        (entities[0] as LakeReflectRectEntity).mytime += 0.1f
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }.start()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        this.width = width
        this.height = height
        GLES30.glViewport(0, 0, width, height)
        ratio = width / height.toFloat()
        LakeReflectConstant.ratio = ratio

        initFBO(SHADOW_TEX_WIDTH, SHADOW_TEX_WIDTH)
        initProject(1f)
    }

    override fun onDrawFrame(gl: GL10?) {
        draw_frame_buffer()
        draw_reflection_lake()
    }

    private fun draw_reflection_lake() {
        GLES30.glViewport(0, 0, width, height)
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0)

        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)
        MatrixState.setCamera(mainCameraX, mainCameraY, mainCameraZ, targetX, targetY, targetZ, upX, upY, upZ)
        MatrixState.setProjectFrustum(left, right, bottom, top, near, far)

        draw_things()

        MatrixState.pushMatrix()
        MatrixState.translate(0f, 0f, 22f)
        (entities[0] as LakeReflectRectEntity).drawSelf(frameTextureId, textureTwo, textureThree, viewProjMatrix)
        MatrixState.popMatrix()
    }

    private fun draw_frame_buffer() {
        GLES30.glViewport(0, 0, SHADOW_TEX_WIDTH, SHADOW_TEX_WIDTH)
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBuffer)
        MatrixState.setCamera(mirrorCameraX, mirrorCameraY, mirrorCameraZ, targetX, targetY, targetZ, upX, upY, upZ)
        MatrixState.setProjectFrustum(left, right, bottom, top, near, far)
        viewProjMatrix = MatrixState.getViewProjMatrix()
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT or GLES30.GL_COLOR_BUFFER_BIT)
        draw_things()
    }

    private fun draw_things() {
        MatrixState.pushMatrix()
        entities[1].drawSelf(textureFour)
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(0f, 0f, -66.5f)
        entities[2].drawSelf(textureOne)
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(-25f, 5f, -15f)
        entities[3].drawSelf(textureFive)
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(15f, -0.5f, 16f)
        entities[4].drawSelf(textureSix)
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(40f, 0f, -15f)
        entities[5].drawSelf(textureSeven)
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(-25f, -0.5f, 5f)
        entities[6].drawSelf(textureSeven)
        MatrixState.popMatrix()

        //绘制木头
        MatrixState.pushMatrix()
        MatrixState.translate(23f, -0.5f, 10f)
        entities[7].drawSelf(textureEight)
        MatrixState.popMatrix()

        //绘制蘑菇
        MatrixState.pushMatrix()
        MatrixState.translate(46f, 0f, -43f)
        entities[8].drawSelf(textureNine)
        MatrixState.popMatrix()

        //绘制帆布
        MatrixState.pushMatrix()
        MatrixState.translate(-50f, 0f, -50f)
        entities[9].drawSelf(textureEight)
        MatrixState.popMatrix()

        //绘制花朵
        MatrixState.pushMatrix()
        MatrixState.translate(30f, 0f, -43f)
        entities[10].drawSelf(textureNine)
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(35f, 0f, -43f)
        entities[10].drawSelf(textureNine)
        MatrixState.popMatrix()
    }

    private fun initFBO(width: Int, height: Int): Int {
        val buffers = IntArray(1)
        GLES30.glGenFramebuffers(1, buffers, 0)
        frameBuffer = buffers[0]
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBuffer)

        GLES30.glGenRenderbuffers(1, buffers, 0)
        renderBuffer = buffers[0]
        GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, renderBuffer)
        GLES30.glRenderbufferStorage(GLES30.GL_RENDERBUFFER, GLES30.GL_DEPTH_COMPONENT16, width, height)

        frameTextureId = ShaderUtils.genTexture(GLES30.GL_TEXTURE_2D, width, height)
        GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES30.GL_COLOR_ATTACHMENT0, GLES30.GL_TEXTURE_2D, frameTextureId, 0)
        GLES30.glFramebufferRenderbuffer(GLES30.GL_FRAMEBUFFER, GLES30.GL_DEPTH_ATTACHMENT, GLES30.GL_RENDERBUFFER, renderBuffer)

        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0)

        return frameTextureId
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            val x = event.x
            val y = event.y
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    val dx = x - mPreviousX
                    val dy = y - mPreviousY
                    xAngle += dx * TOUCH_SCALE_FACTOR
                    yAngle += dy * TOUCH_SCALE_FACTOR
                    if (xAngle < XANGLE_MIN) {
                        xAngle = XANGLE_MIN
                    } else if (xAngle > XANGLE_MAX) {
                        xAngle = XANGLE_MAX
                    }
                    if (yAngle < YANGLE_MIN) {
                        yAngle = YANGLE_MIN
                    } else if (yAngle > YANGLE_MAX) {
                        yAngle = YANGLE_MAX
                    }
                    calculateMainAndMirrorCamera(xAngle, yAngle)
                }
            }
            mPreviousX = x
            mPreviousY = y
        }
        return true
    }
}