package com.ja3son.gllib.demo.skeletal_anim.basic

import android.opengl.GLES30
import com.ja3son.gllib.R
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.cos
import kotlin.math.sin

class BasicRenderer : BaseRenderer() {
    private lateinit var robot: Robot

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        entities.add(BasicEntity("skeletal_anim/basic/body.obj", ShaderUtils.initTexture(R.drawable.arm)))
        entities.add(BasicEntity("skeletal_anim/basic/head.obj", ShaderUtils.initTexture(R.drawable.head)))
        entities.add(BasicEntity("skeletal_anim/basic/left_top.obj", ShaderUtils.initTexture(R.drawable.arm)))
        entities.add(BasicEntity("skeletal_anim/basic/left_bottom.obj", ShaderUtils.initTexture(R.drawable.arm)))
        entities.add(BasicEntity("skeletal_anim/basic/right_top.obj", ShaderUtils.initTexture(R.drawable.arm)))
        entities.add(BasicEntity("skeletal_anim/basic/right_bottom.obj", ShaderUtils.initTexture(R.drawable.arm)))
        entities.add(BasicEntity("skeletal_anim/basic/right_leg_top.obj", ShaderUtils.initTexture(R.drawable.arm)))
        entities.add(BasicEntity("skeletal_anim/basic/right_leg_bottom.obj", ShaderUtils.initTexture(R.drawable.arm)))
        entities.add(BasicEntity("skeletal_anim/basic/left_leg_top.obj", ShaderUtils.initTexture(R.drawable.arm)))
        entities.add(BasicEntity("skeletal_anim/basic/left_leg_bottom.obj", ShaderUtils.initTexture(R.drawable.arm)))
        entities.add(BasicEntity("skeletal_anim/basic/left_foot.obj", ShaderUtils.initTexture(R.drawable.arm)))
        entities.add(BasicEntity("skeletal_anim/basic/right_foot.obj", ShaderUtils.initTexture(R.drawable.arm)))
        robot = Robot(entities.toTypedArray())
        DoActionThread(robot).start()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 2f, 100f)
        MatrixState.setCamera(
                2f, 0f, 2f,
                0f, 0f, 0f,
                0f, 1f, 0f)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

        MatrixState.setCamera(
                2.5f * sin(yAngle.toDouble()).toFloat(), 0f,
                2.5f * cos(yAngle.toDouble()).toFloat(),
                0f, 0.03f, 0f,
                0f, 1f, 0f)

        robot.drawSelf()
    }
}