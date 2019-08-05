package com.ja3son.gllib.demo.buffer.mbo

import android.opengl.GLES32
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan
import kotlin.collections.ArrayList

class MBOEntity(private val r: Float) : BaseEntity() {

    private val bufferIds = IntArray(3)
    private var vertexBufferId = 0
    private var indicesBufferId = 0
    private var texCoordBufferId = 0

    private lateinit var vertexBuffer: ByteBuffer

    private lateinit var vertices: FloatArray
    private lateinit var verticesCube: FloatArray
    private lateinit var curBallForDraw: FloatArray
    private lateinit var curBallForCal: FloatArray

    private var lock = Any()
    private val span = 60f
    private val updateThread = UpdateThread()

    init {
        init()
        updateThread.start()
    }

    inner class UpdateThread : Thread() {
        internal var count = 0
        private var isBallCube = true

        override fun run() {
            while (true) {
                calVertices(count, isBallCube)
                try {
                    count++
                    if (count % span == 0f) {
                        count = 0
                        isBallCube = !isBallCube
                    }
                    sleep(40)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun insertValue(start: Float, end: Float, span: Float, count: Int, isBallToCubeY: Boolean): Float {
        return if (isBallToCubeY) {
            start + count * (end - start) / span
        } else {
            end - count * (end - start) / span
        }
    }

    fun calVertices(count: Int, flag: Boolean) {
        for (i in 0 until vertices.size / 2) {
            curBallForCal[i] = insertValue(vertices[i], verticesCube[i], span, count, flag)
        }
        synchronized(lock) {
            curBallForDraw = curBallForCal.copyOf(curBallForCal.size)
        }
    }

    private fun updateMapping(currVertex: FloatArray) {
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, vertexBufferId)
        vertexBuffer = GLES32.glMapBufferRange(GLES32.GL_ARRAY_BUFFER, 0, currVertex.size * FLOAT_SIZE,
                GLES32.GL_MAP_WRITE_BIT or GLES32.GL_MAP_INVALIDATE_BUFFER_BIT) as ByteBuffer
        vertexBuffer.order(ByteOrder.nativeOrder()).asFloatBuffer().put(currVertex).position(0)
        if (!GLES32.glUnmapBuffer(GLES32.GL_ARRAY_BUFFER)) {
            return
        }
    }

    override fun initVertexData() {
        // generate data start ---------------
        val alVertex = ArrayList<Float>()
        val alVertex1 = ArrayList<Float>()
        val alVertexTexCoor = ArrayList<Float>()
        val alVertexIndice = ArrayList<Int>()
        val alVertex2 = ArrayList<Float>()
        val alVertexCube2 = ArrayList<Float>()
        val alVertexTexCoor2 = ArrayList<Float>()

        val unitSize = 0.5f
        val angleSpan = 5f
        val length = (r.toDouble() * unitSize.toDouble() * sin(Math.toRadians(45.0))).toFloat()
        val length2 = length * 2
        var vAngle = 90f
        while (vAngle >= -90) {
            var hAngle = 360f
            while (hAngle > 0) {
                val xozLength = r.toDouble() * unitSize.toDouble() * cos(Math.toRadians(vAngle.toDouble()))
                val x1 = (xozLength * cos(Math.toRadians(hAngle.toDouble()))).toFloat()
                val z1 = (xozLength * sin(Math.toRadians(hAngle.toDouble()))).toFloat()
                val y1 = (r.toDouble() * unitSize.toDouble() * sin(Math.toRadians(vAngle.toDouble()))).toFloat()

                var x10: Float
                var z10: Float
                var y10 = y1

                var s1: Float
                var t1: Float

                if (vAngle == 50f || vAngle == -45f) {
                    var x1Temp = 0f
                    var z1Temp = 0f
                    var y1Temp = 0f
                    var x10Temp: Float
                    var z10Temp: Float
                    var y10Temp = 0f
                    var s2 = 0f
                    var t2 = 0f
                    var xozLengthTemp = 0f
                    if (vAngle == 50f) {
                        xozLengthTemp = (r.toDouble() * unitSize.toDouble() * cos(Math.toRadians(45.0))).toFloat()
                        x1Temp = (xozLengthTemp * cos(Math.toRadians(hAngle.toDouble()))).toFloat()
                        z1Temp = (xozLengthTemp * sin(Math.toRadians(hAngle.toDouble()))).toFloat()
                        y1Temp = (r.toDouble() * unitSize.toDouble() * sin(Math.toRadians(45.0))).toFloat()
                        y10Temp = length
                    } else if (vAngle == -45f) {
                        xozLengthTemp = (r.toDouble() * unitSize.toDouble() * cos(Math.toRadians(-45.0))).toFloat()
                        x1Temp = (xozLengthTemp * cos(Math.toRadians(hAngle.toDouble()))).toFloat()
                        z1Temp = (xozLengthTemp * sin(Math.toRadians(hAngle.toDouble()))).toFloat()
                        y1Temp = (r.toDouble() * unitSize.toDouble() * sin(Math.toRadians(-45.0))).toFloat()
                        y10Temp = -length
                    }

                    if (abs(x1Temp) > abs(z1Temp)) {
                        x10Temp = if (x1Temp > 0) {
                            xozLengthTemp
                        } else {
                            -xozLengthTemp
                        }
                        z10Temp = (x10Temp * tan(Math.toRadians(hAngle.toDouble()))).toFloat()
                    } else {
                        z10Temp = if (z1Temp > 0) {
                            xozLengthTemp
                        } else {
                            -xozLengthTemp
                        }
                        x10Temp = (z10Temp / tan(Math.toRadians(hAngle.toDouble()))).toFloat()
                    }

                    if (vAngle == 50f) {
                        s2 = 0.5f + x10Temp / length2
                        t2 = 0.5f + z10Temp / length2
                    } else if (vAngle == -45f) {
                        s2 = 0.5f + x10Temp / length2
                        t2 = (-0.5f + z10Temp / length2) * -1
                    }

                    alVertex2.add(x1Temp)
                    alVertex2.add(y1Temp)
                    alVertex2.add(z1Temp)

                    alVertexCube2.add(x10Temp)
                    alVertexCube2.add(y10Temp)
                    alVertexCube2.add(z10Temp)

                    alVertexTexCoor2.add(s2)
                    alVertexTexCoor2.add(t2)
                }

                if (vAngle > 45) {
                    if (abs(x1) > abs(z1)) {
                        x10 = if (x1 > 0) {
                            xozLength.toFloat()
                        } else {
                            (-xozLength).toFloat()
                        }
                        z10 = (x10 * tan(Math.toRadians(hAngle.toDouble()))).toFloat()
                    } else {
                        z10 = if (z1 > 0) {
                            xozLength.toFloat()
                        } else {
                            (-xozLength).toFloat()
                        }
                        x10 = (z10 / tan(Math.toRadians(hAngle.toDouble()))).toFloat()
                    }
                    y10 = length
                    s1 = 0.5f + x10 / length2
                    t1 = 0.5f + z10 / length2
                } else if (vAngle < -45) {
                    if (abs(x1) > abs(z1)) {
                        x10 = if (x1 > 0) {
                            xozLength.toFloat()
                        } else {
                            (-xozLength).toFloat()
                        }
                        z10 = (x10 * tan(Math.toRadians(hAngle.toDouble()))).toFloat()
                    } else {
                        z10 = if (z1 > 0) {
                            xozLength.toFloat()
                        } else {
                            (-xozLength).toFloat()
                        }
                        x10 = (z10 / tan(Math.toRadians(hAngle.toDouble()))).toFloat()
                    }
                    y10 = -length
                    s1 = 0.5f + x10 / length2
                    t1 = 1 - (0.5f + z10 / length2)

                } else {
                    if (abs(x1) > abs(z1)) {
                        if (x1 > 0) {
                            x10 = length
                            z10 = (x10 * tan(Math.toRadians(hAngle.toDouble()))).toFloat()
                            s1 = 0.5f + z10 / length2
                        } else {
                            x10 = -length
                            z10 = (x10 * tan(Math.toRadians(hAngle.toDouble()))).toFloat()
                            s1 = (-0.5f + z10 / length2) * -1
                        }
                        t1 = 1 - (0.5f + y10 / length2)
                    } else {
                        if (z1 > 0) {
                            z10 = length
                            x10 = (z10 / tan(Math.toRadians(hAngle.toDouble()))).toFloat()
                            s1 = 0.5f + x10 / length2
                        } else {
                            z10 = -length
                            x10 = (z10 / tan(Math.toRadians(hAngle.toDouble()))).toFloat()
                            s1 = -1 * (-0.5f + x10 / length2)
                        }
                        t1 = 1 - (0.5f + y10 / length2)
                    }
                }

                alVertex.add(x1)
                alVertex.add(y1)
                alVertex.add(z1)

                alVertex1.add(x10)
                alVertex1.add(y10)
                alVertex1.add(z10)

                alVertexTexCoor.add(s1)
                alVertexTexCoor.add(t1)
                hAngle -= angleSpan
            }

            if (vAngle == 50f || vAngle == -45f) {
                for (i in 0 until alVertex2.size / 3) {
                    alVertex.add(alVertex2[i * 3])
                    alVertex.add(alVertex2[i * 3 + 1])
                    alVertex.add(alVertex2[i * 3 + 2])
                }
                for (i in 0 until alVertexCube2.size / 3) {
                    alVertex1.add(alVertexCube2[i * 3])
                    alVertex1.add(alVertexCube2[i * 3 + 1])
                    alVertex1.add(alVertexCube2[i * 3 + 2])
                }
                for (i in 0 until alVertexTexCoor2.size / 3) {
                    alVertexTexCoor.add(alVertexTexCoor2[i * 3])
                    alVertexTexCoor.add(alVertexTexCoor2[i * 3 + 1])
                    alVertexTexCoor.add(alVertexTexCoor2[i * 3 + 2])
                }

                alVertex2.clear()
                alVertexCube2.clear()
                alVertexTexCoor2.clear()
            }
            vAngle -= angleSpan
        }

        val w = (360 / angleSpan).toInt()
        for (i in 0 until w + 2) {
            for (j in 0 until w) {
                val x = i * w + j
                alVertexIndice.add(x)
                alVertexIndice.add(x + w)
                alVertexIndice.add(x + 1)
                alVertexIndice.add(x + 1)
                alVertexIndice.add(x + w)
                alVertexIndice.add(x + w + 1)
            }
        }

        vCounts = alVertex.size / 3
        iCounts = alVertexIndice.size

        val indices = IntArray(iCounts)
        val texCoors = FloatArray(alVertexTexCoor.size)

        vertices = FloatArray(vCounts * 3)
        verticesCube = FloatArray(vCounts * 3)

        curBallForDraw = FloatArray(vertices.size / 2)
        curBallForCal = FloatArray(vertices.size / 2)

        for (i in 0 until alVertex.size) {
            vertices[i] = alVertex[i]
        }

        for (i in 0 until iCounts) {
            indices[i] = alVertexIndice[i]
        }

        for (i in 0 until alVertex1.size) {
            verticesCube[i] = alVertex1[i]
        }

        for (i in 0 until alVertexTexCoor.size) {
            texCoors[i] = alVertexTexCoor[i]
        }

        // generate data end ---------------

        GLES32.glGenBuffers(3, bufferIds, 0)

        vertexBufferId = bufferIds[0]
        indicesBufferId = bufferIds[1]
        texCoordBufferId = bufferIds[2]

        val indexBuffer: IntBuffer = ByteBuffer.allocateDirect(indices.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asIntBuffer()
                .put(indices).position(0) as IntBuffer

        GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, indicesBufferId)
        GLES32.glBufferData(GLES32.GL_ELEMENT_ARRAY_BUFFER, indices.size * FLOAT_SIZE, indexBuffer, GLES32.GL_STATIC_DRAW)


        texCoorBuffer = ByteBuffer.allocateDirect(texCoors.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(texCoors).position(0) as FloatBuffer

        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, texCoordBufferId)
        GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER, texCoors.size * FLOAT_SIZE, texCoorBuffer, GLES32.GL_STATIC_DRAW)


        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, vertexBufferId)
        GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER, vertices.size * FLOAT_SIZE, null, GLES32.GL_STATIC_DRAW)

        vertexBuffer = GLES32.glMapBufferRange(GLES32.GL_ARRAY_BUFFER, 0, vertices.size * FLOAT_SIZE,
                GLES32.GL_MAP_WRITE_BIT or GLES32.GL_MAP_INVALIDATE_BUFFER_BIT) as ByteBuffer
        vertexBuffer.order(ByteOrder.nativeOrder()).asFloatBuffer().put(vertices).position(0)

        if (!GLES32.glUnmapBuffer(GLES32.GL_ARRAY_BUFFER)) {
            return
        }

        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, 0)
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("mbo_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("mbo_fragment.glsl")
        )
    }

    override fun initShaderParams() {
        aPosition = GLES32.glGetAttribLocation(program, "aPosition")
        aTexCoor = GLES32.glGetAttribLocation(program, "aTexCoor")
        uMVPMatrix = GLES32.glGetUniformLocation(program, "uMVPMatrix")
    }

    override fun drawSelf(textureId: Int) {
        MatrixState.rotate(xAngle, 1f, 0f, 0f)
        MatrixState.rotate(yAngle, 0f, 1f, 0f)
        GLES32.glUseProgram(program)
        GLES32.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)

        GLES32.glEnableVertexAttribArray(aPosition)
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, vertexBufferId)
        GLES32.glVertexAttribPointer(aPosition, posLen, GLES32.GL_FLOAT, false, posLen * FLOAT_SIZE, 0)

        GLES32.glEnableVertexAttribArray(aTexCoor)
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, texCoordBufferId)
        GLES32.glVertexAttribPointer(aTexCoor, texLen, GLES32.GL_FLOAT, false, texLen * FLOAT_SIZE, 0)

        GLES32.glActiveTexture(GLES32.GL_TEXTURE0)
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, textureId)

        synchronized(lock) {
            updateMapping(curBallForDraw)
        }

        GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, indicesBufferId)
        GLES32.glDrawElements(GLES32.GL_TRIANGLES, iCounts, GLES32.GL_UNSIGNED_INT, 0)
        GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, 0)

        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, 0)
    }
}