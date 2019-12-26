package com.ja3son.gllib.demo.senior.computer

import android.opengl.GLES30
import android.opengl.GLES31
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer


class ComputerEntity : BaseEntity() {
    lateinit var mVertexBuffer: FloatBuffer
    lateinit var mNormalBuffer: FloatBuffer
    lateinit var mTexCoorBuffer: FloatBuffer
    lateinit var mIndexBuffer: IntBuffer

    var vertexDataBufferId = 0
    var normalDataBufferId = 0
    var utexCoorOffset = 0

    var wave_compute_program = 0
    var wave_normal_compute_program = 0

    var bx1 = 0
    var bc1 = 0
    var zf1 = 0
    var qsj1 = 0

    var bx2 = 0
    var bc2 = 0
    var zf2 = 0
    var qsj2 = 0

    var bx3 = 0
    var bc3 = 0
    var zf3 = 0
    var qsj3 = 0

    var qsj1_value = 0f
    var qsj2_value = 90f
    var qsj3_value = 45f

    var texCoorOffset = 0.0f

    init {
        init()
    }

    override fun initVertexData() {
        vCounts = (Constant.WATER_WIDTH + 1) * (Constant.WATER_HEIGHT + 1)
        val vertices = FloatArray(vCounts * 4)
        var tempCount = 0

        for (j in 0..Constant.WATER_HEIGHT) {
            for (i in 0..Constant.WATER_WIDTH) {
                val x = Constant.WATER_UNIT_SIZE * i
                val z = Constant.WATER_UNIT_SIZE * j
                val y = 0f
                vertices[tempCount * 4] = x
                vertices[tempCount * 4 + 1] = y
                vertices[tempCount * 4 + 2] = z
                vertices[tempCount * 4 + 3] = 1f
                tempCount++
            }
        }

        val vbb = ByteBuffer.allocateDirect(vertices.size * 4)
        vbb.order(ByteOrder.nativeOrder())
        mVertexBuffer = vbb.asFloatBuffer()
        mVertexBuffer.put(vertices)
        mVertexBuffer.position(0)

        val normals = FloatArray(vCounts * 4)
        tempCount = 0
        for (j in 0..Constant.WATER_HEIGHT) {
            for (i in 0..Constant.WATER_WIDTH) {
                normals[tempCount * 4] = 0f
                normals[tempCount * 4 + 1] = 1f
                normals[tempCount * 4 + 2] = 0f
                normals[tempCount * 4 + 3] = 1f
                tempCount++
            }
        }

        val nbb = ByteBuffer.allocateDirect(normals.size * 4)
        nbb.order(ByteOrder.nativeOrder())
        mNormalBuffer = nbb.asFloatBuffer()
        mNormalBuffer.put(normals)
        mNormalBuffer.position(0)

        val texCoor = FloatArray(vCounts * 2)
        tempCount = 0
        for (j in 0..Constant.WATER_HEIGHT) {
            for (i in 0..Constant.WATER_WIDTH) {
                val s = 3.0f / Constant.WATER_WIDTH * i
                val t = 3.0f / Constant.WATER_HEIGHT * j
                texCoor[tempCount * 2] = s
                texCoor[tempCount * 2 + 1] = t
                tempCount++
            }
        }

        val cbb = ByteBuffer.allocateDirect(texCoor.size * 4)
        cbb.order(ByteOrder.nativeOrder())
        mTexCoorBuffer = cbb.asFloatBuffer()
        mTexCoorBuffer.put(texCoor)
        mTexCoorBuffer.position(0)

        iCounts = Constant.WATER_WIDTH * Constant.WATER_HEIGHT * 6
        val indexs = IntArray(iCounts)
        tempCount = 0
        for (i in 0 until Constant.WATER_WIDTH) {
            for (j in 0 until Constant.WATER_HEIGHT) {
                val widthTemp = Constant.WATER_WIDTH + 1
                val index0 = j * widthTemp + i
                val index1 = index0 + 1
                val index2 = index0 + 1 + widthTemp
                val index3 = index0 + widthTemp
                //0-3-1
                indexs[tempCount * 6] = index0
                indexs[tempCount * 6 + 1] = index3
                indexs[tempCount * 6 + 2] = index1
                //1-3-2
                indexs[tempCount * 6 + 3] = index1
                indexs[tempCount * 6 + 4] = index3
                indexs[tempCount * 6 + 5] = index2
                tempCount++
            }
        }

        val ibb = ByteBuffer.allocateDirect(indexs.size * 4)
        ibb.order(ByteOrder.nativeOrder()) //设置字节顺序

        mIndexBuffer = ibb.asIntBuffer()
        mIndexBuffer.put(indexs)
        mIndexBuffer.position(0)

        val bufferIds = IntArray(2)
        GLES31.glGenBuffers(2, bufferIds, 0)

        vertexDataBufferId = bufferIds[0]
        normalDataBufferId = bufferIds[1]

        GLES31.glBindBuffer(GLES31.GL_SHADER_STORAGE_BUFFER, vertexDataBufferId)
        GLES31.glBufferData(GLES31.GL_SHADER_STORAGE_BUFFER, vCounts * 4 * 4, mVertexBuffer, GLES31.GL_STATIC_DRAW)
        GLES31.glBindBuffer(GLES31.GL_SHADER_STORAGE_BUFFER, normalDataBufferId)
        GLES31.glBufferData(GLES31.GL_SHADER_STORAGE_BUFFER, vCounts * 4 * 4, mNormalBuffer, GLES31.GL_STATIC_DRAW)
    }

    override fun initShader() {
        program = ShaderUtils.createLocalProgram(
                ShaderUtils.loadFromAssetsFile("computer/wave_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("computer/wave_fragment.glsl"),
                "wave_shader"
        )

        wave_compute_program = ShaderUtils.createComputeProgram(
                ShaderUtils.loadFromAssetsFile("computer/compute_wave.glsl"),
                "compute_wave_shader"
        )

        wave_normal_compute_program = ShaderUtils.createComputeProgram(
                ShaderUtils.loadFromAssetsFile("computer/compute_wave_normal.glsl"),
                "compute_wave_normal_shader"
        )
    }

    override fun initShaderParams() {
        aPosition = GLES31.glGetAttribLocation(program, "aPosition")
        aNormal = GLES31.glGetAttribLocation(program, "aNormal")
        aTexCoor = GLES31.glGetAttribLocation(program, "aTexCoor")

        uMVPMatrix = GLES31.glGetUniformLocation(program, "uMVPMatrix")
        uMMatrix = GLES31.glGetUniformLocation(program, "uMMatrix")
        uCamera = GLES31.glGetUniformLocation(program, "uCamera")
        uLightLocation = GLES31.glGetUniformLocation(program, "uLightLocation")
        utexCoorOffset = GLES31.glGetUniformLocation(program, "utexCoorOffset")


        bx1 = GLES31.glGetUniformLocation(wave_compute_program, "bx1")
        bc1 = GLES31.glGetUniformLocation(wave_compute_program, "bc1")
        zf1 = GLES31.glGetUniformLocation(wave_compute_program, "zf1")
        qsj1 = GLES31.glGetUniformLocation(wave_compute_program, "qsj1")

        bx2 = GLES31.glGetUniformLocation(wave_compute_program, "bx2")
        bc2 = GLES31.glGetUniformLocation(wave_compute_program, "bc2")
        zf2 = GLES31.glGetUniformLocation(wave_compute_program, "zf2")
        qsj2 = GLES31.glGetUniformLocation(wave_compute_program, "qsj2")

        bx3 = GLES31.glGetUniformLocation(wave_compute_program, "bx3")
        bc3 = GLES31.glGetUniformLocation(wave_compute_program, "bc3")
        zf3 = GLES31.glGetUniformLocation(wave_compute_program, "zf3")
        qsj3 = GLES31.glGetUniformLocation(wave_compute_program, "qsj3")
    }

    override fun drawSelf(textureId: Int) {
        GLES31.glBindBufferBase(GLES31.GL_SHADER_STORAGE_BUFFER, 4, vertexDataBufferId)
        GLES31.glBindBufferBase(GLES31.GL_SHADER_STORAGE_BUFFER, 5, normalDataBufferId)
        GLES31.glUseProgram(wave_compute_program)


        GLES31.glUniform2f(bx1, 50f, 150f)
        GLES31.glUniform1f(bc1, 32f)
        GLES31.glUniform1f(zf1, 0.8f)
        qsj1_value = (qsj1_value + 9) % 360
        GLES31.glUniform1f(qsj1, Math.toRadians(qsj1_value.toDouble()).toFloat())

        GLES31.glUniform2f(bx2, 10f, 40f)
        GLES31.glUniform1f(bc2, 24f)
        GLES31.glUniform1f(zf2, 1f)
        qsj2_value = (qsj2_value + 9) % 360
        GLES31.glUniform1f(qsj2, Math.toRadians(qsj2_value.toDouble()).toFloat())

        GLES31.glUniform2f(bx3, 200f, 200f)
        GLES31.glUniform1f(bc3, 60f)
        GLES31.glUniform1f(zf3, 2.0f)
        qsj3_value = (qsj3_value + 4.0f) % 360
        GLES31.glUniform1f(qsj3, Math.toRadians(qsj3_value.toDouble()).toFloat())

        GLES31.glDispatchCompute(
                Constant.WATER_WIDTH + 1,
                Constant.WATER_HEIGHT + 1,
                1
        )
        GLES31.glMemoryBarrier(GLES31.GL_SHADER_STORAGE_BARRIER_BIT)


        GLES31.glBindBufferBase(GLES31.GL_SHADER_STORAGE_BUFFER, 4, vertexDataBufferId)
        GLES31.glBindBufferBase(GLES31.GL_SHADER_STORAGE_BUFFER, 5, normalDataBufferId)
        GLES31.glUseProgram(wave_normal_compute_program)
        GLES31.glDispatchCompute(
                Constant.WATER_WIDTH + 1,
                Constant.WATER_HEIGHT + 1,
                1
        )
        GLES31.glMemoryBarrier(GLES31.GL_SHADER_STORAGE_BARRIER_BIT)


        GLES31.glUseProgram(program)
        GLES31.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES31.glUniformMatrix4fv(uMMatrix, 1, false, MatrixState.getModelMatrix(), 0)
        GLES30.glUniform3fv(uLightLocation, 1, MatrixState.lightPositionFB)
        GLES30.glUniform3fv(uCamera, 1, MatrixState.cameraFB)

        texCoorOffset = (texCoorOffset + 0.001f) % 10.0f

        GLES31.glUniform1f(utexCoorOffset, texCoorOffset)

        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, vertexDataBufferId)
        GLES31.glVertexAttribPointer(aPosition, 4, GLES31.GL_FLOAT, false, 4 * 4, 0)

        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, normalDataBufferId)
        GLES31.glVertexAttribPointer(aNormal, 4, GLES31.GL_FLOAT, false, 4 * 4, 0)
        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, 0)

        GLES31.glVertexAttribPointer(
                aTexCoor,
                2,
                GLES31.GL_FLOAT,
                false,
                2 * 4,
                mTexCoorBuffer
        )
        GLES31.glEnableVertexAttribArray(aPosition)
        GLES31.glEnableVertexAttribArray(aNormal)
        GLES31.glEnableVertexAttribArray(aTexCoor)

        GLES31.glActiveTexture(GLES31.GL_TEXTURE0)
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textureId)

        GLES31.glDrawElements(GLES31.GL_TRIANGLES, iCounts, GLES31.GL_UNSIGNED_INT, mIndexBuffer)
    }
}