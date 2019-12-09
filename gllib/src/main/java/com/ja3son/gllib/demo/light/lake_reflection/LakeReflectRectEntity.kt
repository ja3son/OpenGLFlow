package com.ja3son.gllib.demo.light.lake_reflection

import android.opengl.GLES30
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.WIDTH_SPAN
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.lock
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.wave1PositionX
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.wave1PositionY
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.wave1PositionZ
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.wave2PositionX
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.wave2PositionY
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.wave2PositionZ
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.wave3PositionX
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.wave3PositionY
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.wave3PositionZ
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.waveAmplitude1
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.waveAmplitude2
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.waveAmplitude3
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.waveFrequency1
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.waveFrequency2
import com.ja3son.gllib.demo.light.lake_reflection.LakeReflectConstant.waveFrequency3
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import com.ja3son.gllib.util.ShaderUtils.Normal
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.sin
import kotlin.math.sqrt


class LakeReflectRectEntity : BaseEntity() {
    var uMVPMatrixMirror = 0
    var sTextureDY = 0
    var sTextureWater = 0
    var sTextureNormal = 0

    var mytime = 0f
    lateinit var zero1: FloatArray
    lateinit var zero2: FloatArray
    lateinit var zero3: FloatArray
    lateinit var vertices: FloatArray
    lateinit var normals: FloatArray
    lateinit var indices: IntArray

    lateinit var verticesForCal: FloatArray
    lateinit var normalsForCal: FloatArray

    init {
        init()
    }

    override fun initVertexData() {
        val cols = 64
        val rows = 64
        val UNIT_SIZE: Float = WIDTH_SPAN / (cols - 1)
        val alVertixIndice = ArrayList<Int>()
        val alVertixV = ArrayList<Float>()
        val alVertixN = ArrayList<Float>()
        val alVertixT = ArrayList<Float>()

        for (j in 0 until rows) {
            for (i in 0 until cols) {
                val zsx: Float = -WIDTH_SPAN / 2 + i * UNIT_SIZE
                val zsz: Float = -WIDTH_SPAN / 2 + j * UNIT_SIZE
                val zsy = 0f
                alVertixV.add(zsx)
                alVertixV.add(zsy)
                alVertixV.add(zsz)
                alVertixN.add(0.0f)
                alVertixN.add(1.0f)
                alVertixN.add(0.0f)
                val s: Float = zsx / WIDTH_SPAN + 0.5f
                val t: Float = zsz / WIDTH_SPAN + 0.5f
                alVertixT.add(s)
                alVertixT.add(t)
            }
        }

        for (i in 0 until rows - 1) {
            for (j in 0 until cols - 1) {
                val x = i * rows + j
                alVertixIndice.add(x)
                alVertixIndice.add(x + cols)
                alVertixIndice.add(x + 1)
                alVertixIndice.add(x + 1)
                alVertixIndice.add(x + cols)
                alVertixIndice.add(x + cols + 1)
            }
        }

        vCounts = alVertixV.size / 3

        vertices = FloatArray(vCounts * 3)
        normals = FloatArray(vCounts * 3)
        val texCoor = FloatArray(vCounts * 2)

        verticesForCal = FloatArray(vCounts * 3)
        normalsForCal = FloatArray(vCounts * 3)
        iCounts = alVertixIndice.size
        indices = IntArray(alVertixIndice.size)
        for (i in 0 until alVertixIndice.size) {
            indices[i] = alVertixIndice[i]
        }

        for (i in 0 until alVertixV.size) {
            vertices[i] = alVertixV[i]
        }

        for (i in 0 until alVertixN.size) {
            normals[i] = alVertixN[i]
        }

        for (i in 0 until alVertixT.size) {
            texCoor[i] = alVertixT[i]
        }

        zero1 = floatArrayOf(wave1PositionX, wave1PositionY, wave1PositionZ)
        zero2 = floatArrayOf(wave2PositionX, wave2PositionY, wave2PositionZ)
        zero3 = floatArrayOf(wave3PositionX, wave3PositionY, wave3PositionZ)

        verticesBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(vertices).position(0) as FloatBuffer

        texCoorBuffer = ByteBuffer.allocateDirect(texCoor.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(texCoor).position(0) as FloatBuffer

        normalBuffer = ByteBuffer.allocateDirect(normals.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(normals).position(0) as FloatBuffer

        indicesIntBuffer = ByteBuffer.allocateDirect(indices.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asIntBuffer()
                .put(indices).position(0) as IntBuffer
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("lake_reflection/shader/water_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("lake_reflection/shader/water_frag.glsl")
        )
    }

    override fun initShaderParams() {
        super.initShaderParams()
        uMVPMatrixMirror = GLES30.glGetUniformLocation(program, "uMVPMatrixMirror")
        sTextureDY = GLES30.glGetUniformLocation(program, "sTextureDY")
        sTextureWater = GLES30.glGetUniformLocation(program, "sTextureWater")
        sTextureNormal = GLES30.glGetUniformLocation(program, "sTextureNormal")
    }

    fun drawSelf(textureOne: Int, textureTwo: Int, textureThree: Int, viewProjMatrix: FloatArray) {
        synchronized(lock) {
            updateData()
        }

        GLES30.glUseProgram(program)

        GLES30.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES30.glUniformMatrix4fv(uMMatrix, 1, false, MatrixState.getModelMatrix(), 0)
        GLES30.glUniformMatrix4fv(uMVPMatrixMirror, 1, false, viewProjMatrix, 0)

        GLES30.glUniform3fv(uLightLocation, 1, MatrixState.lightPositionFB)
        GLES30.glUniform3fv(uCamera, 1, MatrixState.cameraFB)

        GLES30.glVertexAttribPointer(aPosition, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, verticesBuffer)
        GLES30.glVertexAttribPointer(aTexCoor, texLen, GLES30.GL_FLOAT, false, texLen * FLOAT_SIZE, texCoorBuffer)
        GLES30.glVertexAttribPointer(aNormal, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, normalBuffer)

        GLES30.glEnableVertexAttribArray(aPosition)
        GLES30.glEnableVertexAttribArray(aTexCoor)
        GLES30.glEnableVertexAttribArray(aNormal)

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureOne)
        GLES30.glActiveTexture(GLES30.GL_TEXTURE1)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureTwo)
        GLES30.glActiveTexture(GLES30.GL_TEXTURE2)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureThree)

        GLES30.glUniform1i(sTextureDY, 0)
        GLES30.glUniform1i(sTextureWater, 1)
        GLES30.glUniform1i(sTextureNormal, 2)

        GLES30.glDrawElements(GLES30.GL_TRIANGLES, iCounts, GLES30.GL_UNSIGNED_INT, indicesIntBuffer)

        GLES30.glDisableVertexAttribArray(aPosition)
        GLES30.glDisableVertexAttribArray(aTexCoor)
        GLES30.glDisableVertexAttribArray(aNormal)
    }

    fun calVerticesNormalAndTangent() {
        for (i in 0 until vCounts) {
            verticesForCal[i * 3] = vertices[i * 3]
            verticesForCal[i * 3 + 1] = findHeight(vertices[i * 3], vertices[i * 3 + 2])
            verticesForCal[i * 3 + 2] = vertices[i * 3 + 2]
        }

        normalsForCal = calNormal(verticesForCal, indices)
        synchronized(lock) {
            vertices = verticesForCal.copyOf(verticesForCal.size)
            normals = normalsForCal.copyOf(normalsForCal.size)
        }
    }

    fun findHeight(x: Float, z: Float): Float {
        var result: Float

        val distance1 = sqrt(((x - zero1[0]) * (x - zero1[0]) + (z - zero1[2]) * (z - zero1[2])).toDouble()).toFloat()

        val distance2 = sqrt(((x - zero2[0]) * (x - zero2[0]) + (z - zero2[2]) * (z - zero2[2])).toDouble()).toFloat()

        val distance3 = sqrt(((x - zero3[0]) * (x - zero3[0]) + (z - zero3[2]) * (z - zero3[2])).toDouble()).toFloat()
        result = (sin(distance1 * waveFrequency1 * Math.PI + mytime) * waveAmplitude1).toFloat()
        result = (result + sin(distance2 * waveFrequency2 * Math.PI + mytime) * waveAmplitude2).toFloat()
        result = (result + sin(distance3 * waveFrequency3 * Math.PI + mytime) * waveAmplitude3).toFloat()
        return result
    }

    private fun updateData() {
        verticesBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(vertices).position(0) as FloatBuffer

        normalBuffer = ByteBuffer.allocateDirect(normals.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(normals).position(0) as FloatBuffer
    }

    fun calNormal(data: FloatArray, indices: IntArray): FloatArray {
        val hmn = HashMap<Int, HashSet<Normal>>()
        val answer = FloatArray(data.size)
        for (i in 0 until indices.size / 3) {
            val index = intArrayOf(indices[i * 3 + 0], indices[i * 3 + 1],
                    indices[i * 3 + 2])

            val x0 = data[index[0] * 3 + 0]
            val y0 = data[index[0] * 3 + 1]
            val z0 = data[index[0] * 3 + 2]
            val x1 = data[index[1] * 3 + 0]
            val y1 = data[index[1] * 3 + 1]
            val z1 = data[index[1] * 3 + 2]
            val x2 = data[index[2] * 3 + 0]
            val y2 = data[index[2] * 3 + 1]
            val z2 = data[index[2] * 3 + 2]

            val vxa = x1 - x0
            val vya = y1 - y0
            val vza = z1 - z0

            val vxb = x2 - x0
            val vyb = y2 - y0
            val vzb = z2 - z0

            val tempNormal: FloatArray = getCrossProduct(vxa, vya, vza, vxb, vyb, vzb)

            val vNormal: FloatArray = vectorNormal(tempNormal)
            for (tempInxex in index) {

                var hsn = hmn[tempInxex]
                if (hsn == null) {
                    hsn = HashSet()
                }
                hsn.add(Normal(vNormal[0], vNormal[1], vNormal[2]))
                hmn[tempInxex] = hsn
            }
        }

        var c = 0
        for (i in 0 until data.size / 3) {
            val hsn = hmn[i]
            val tn = Normal.getAverage(hsn!!)
            answer[c++] = tn[0]
            answer[c++] = tn[1]
            answer[c++] = tn[2]
        }
        return answer
    }

    fun vectorNormal(vector: FloatArray): FloatArray {
        val module = Math.sqrt(vector[0] * vector[0] + vector[1] * vector[1] + (vector[2] * vector[2]).toDouble()).toFloat()
        return floatArrayOf(vector[0] / module, vector[1] / module, vector[2] / module)
    }

    fun getCrossProduct(x1: Float, y1: Float, z1: Float, x2: Float, y2: Float, z2: Float): FloatArray {
        val A = y1 * z2 - y2 * z1
        val B = z1 * x2 - z2 * x1
        val C = x1 * y2 - x2 * y1
        return floatArrayOf(A, B, C)
    }
}