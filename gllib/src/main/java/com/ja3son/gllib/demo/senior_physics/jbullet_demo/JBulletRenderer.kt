package com.ja3son.gllib.demo.senior_physics.jbullet_demo

import android.opengl.GLES30
import android.view.MotionEvent
import com.bulletphysics.collision.broadphase.AxisSweep3
import com.bulletphysics.collision.dispatch.CollisionDispatcher
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration
import com.bulletphysics.collision.shapes.BoxShape
import com.bulletphysics.collision.shapes.CollisionShape
import com.bulletphysics.collision.shapes.StaticPlaneShape
import com.bulletphysics.dynamics.DiscreteDynamicsWorld
import com.bulletphysics.dynamics.RigidBody
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver
import com.ja3son.gllib.R
import com.ja3son.gllib.controller.BaseRenderer
import com.ja3son.gllib.demo.senior_physics.jbullet_demo.Constant.MAX_SUB_STEPS
import com.ja3son.gllib.demo.senior_physics.jbullet_demo.Constant.TIME_STEP
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import javax.vecmath.Vector3f


class JBulletRenderer : BaseRenderer() {
    lateinit var dynamics_world: DiscreteDynamicsWorld
    lateinit var box_shape: CollisionShape
    lateinit var plane_shape: CollisionShape
    var tca: ArrayList<JBulletCubeEntity> = ArrayList()
    var tcaForAdd: ArrayList<JBulletCubeEntity> = ArrayList()
    var runables: ArrayList<Runnable> = ArrayList()

    var cubeTextureId = IntArray(2)
    var floorTextureId = 0

    init {
        init_world()
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        MatrixState.setCamera(
                Constant.EYE_X,
                Constant.EYE_Y,
                Constant.EYE_Z,
                Constant.TARGET_X,
                Constant.TARGET_Y,
                Constant.TARGET_Z,
                0f,
                1f,
                0f)
//        entities.add(JBulletFloorEntity(80 * Constant.UNIT_SIZE, -Constant.UNIT_SIZE, plane_shape, dynamics_world))
        entities.add(JBulletLandFloorEntity(0.5f, -.6f, dynamics_world))

        val size = 2
        val xStart = (-size / 2.0f + 0.5f) * (2 + 0.4f) * Constant.GT_UNIT_SIZE
        val yStart = 1.52f
        val zStart = (-size / 2.0f + 0.5f) * (2 + 0.4f) * Constant.GT_UNIT_SIZE - 4f
        for (i in 0 until size) {
            for (j in 0 until size) {
                for (k in 0 until size) {
                    val tcTemp = JBulletCubeEntity(
                            Constant.GT_UNIT_SIZE,
                            box_shape,
                            dynamics_world,
                            1f,
                            xStart + i * (2 + 0.4f) * Constant.GT_UNIT_SIZE,
                            yStart + j * 2.02f * Constant.GT_UNIT_SIZE,
                            zStart + k * (2 + 0.4f) * Constant.GT_UNIT_SIZE
                    )
                    tca.add(tcTemp)
                    tcTemp.body.forceActivationState(RigidBody.WANTS_DEACTIVATION)
                }
            }
        }

        object : Thread() {
            override fun run() {
                while (true) {
                    try {
                        synchronized(tcaForAdd) {
                            synchronized(tca) {
                                for (tc in tcaForAdd) {
                                    tca.add(tc)
                                }
                            }
                            tcaForAdd.clear()
                        }
                        dynamics_world.stepSimulation(TIME_STEP, MAX_SUB_STEPS)
                        sleep(20)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }.start()

        floorTextureId = ShaderUtils.initTexture(R.drawable.f6)
        cubeTextureId[0] = ShaderUtils.initTexture(R.drawable.wood_bin2)
        cubeTextureId[1] = ShaderUtils.initTexture(R.drawable.wood_bin1)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        this.width = width
        this.height = height
        GLES30.glViewport(0, 0, width, height)
        ratio = width / height.toFloat()
        MatrixState.setProjectFrustum(-ratio, ratio, -1f, 1f, 2f, 100f)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)
        for (runable in runables) {
            runable.run()
            runables.remove(runable)
        }

        synchronized(tca) {
            for (tc in tca) {
                MatrixState.pushMatrix()
                tc.drawSelf(cubeTextureId)
                MatrixState.popMatrix()
            }
        }

        MatrixState.pushMatrix()
        entities[0].drawSelf(floorTextureId)
        MatrixState.popMatrix()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    synchronized(tcaForAdd) {
                        runables.add(Runnable {
                            val tcTemp = JBulletCubeEntity(
                                    Constant.UNIT_SIZE,
                                    box_shape,
                                    dynamics_world,
                                    1f,
                                    0f,
                                    2f,
                                    4f
                            )
                            tcTemp.body.setLinearVelocity(Vector3f(0f, 2f, -12f))
                            tcTemp.body.setAngularVelocity(Vector3f(0f, 0f, 0f))
                            tcaForAdd.add(tcTemp)
                        })
                    }
                }
            }
        }
        return true
    }

    private fun init_world() {
        val config = DefaultCollisionConfiguration()
        val dispatcher = CollisionDispatcher(config)

        val worldAABB_min = Vector3f(-10000f, -10000f, -10000f)
        val worldAABB_max = Vector3f(10000f, 10000f, 10000f)

        val max_proxies = 1024

        val overlapping_pair_cache = AxisSweep3(worldAABB_min, worldAABB_max, max_proxies)
        val solver = SequentialImpulseConstraintSolver()

        dynamics_world = DiscreteDynamicsWorld(dispatcher, overlapping_pair_cache, solver, config)
        dynamics_world.setGravity(Vector3f(0f, -10f, 0f))

        box_shape = BoxShape(Vector3f(Constant.UNIT_SIZE, Constant.UNIT_SIZE, Constant.UNIT_SIZE))
        plane_shape = StaticPlaneShape(Vector3f(0f, 1f, 0f), 0f)
    }
}