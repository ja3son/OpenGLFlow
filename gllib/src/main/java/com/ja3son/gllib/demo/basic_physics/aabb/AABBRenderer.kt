package com.ja3son.gllib.demo.basic_physics.aabb

import android.opengl.GLES30
import com.ja3son.gllib.R
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class AABBRenderer : BaseRenderer() {
    var textureId = 0
    var aList = ArrayList<RigidBody>()

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        entities.add(ObjEntity("basic_physics/ch.obj"))
        aList.add(RigidBody(entities[0], true, Vector3f(-13f, 0f, 0f), Vector3f(0f, 0f, 0f)))
        aList.add(RigidBody(entities[0], true, Vector3f(13f, 0f, 0f), Vector3f(0f, 0f, 0f)))
        aList.add(RigidBody(entities[0], false, Vector3f(0f, 0f, 0f), Vector3f(0.1f, 0f, 0f)))

        textureId = ShaderUtils.initTexture(R.drawable.qhc)

        object : Thread() {
            var flag = true
            override fun run() {
                while (flag) {
                    for (i in 0 until aList.size) {
                        aList[i].go(aList)
                    }
                    try {
                        sleep(20)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }.start()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 4f, 100f)
        MatrixState.setCamera(0f, 13f, 40f, 0f, 0f, -10f, 0f, 1.0f, 0.0f)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

        for (i in aList.indices) {
            aList[i].drawSelf()
        }
    }
}