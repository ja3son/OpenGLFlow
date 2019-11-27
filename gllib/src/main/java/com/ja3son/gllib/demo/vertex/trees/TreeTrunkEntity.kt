package com.ja3son.gllib.demo.vertex.trees

import android.opengl.GLES30
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class TreeTrunkEntity : BaseEntity() {

    private val bottomRadius: Float = 15f
    private val jointHeight: Float = 15f
    private val jointNum: Int = 20
    private val availableNum: Int = 14

    private val bufferIds = IntArray(2)
    private var vertexBufferId = 0
    private var texCoordBufferId = 0

    private val vao = IntArray(1)
    private var vaoIndex = 0

    private val longitudeSpan = 12f
    private var uBendR = 0
    private var uDirectionDegree = 0

    init {
        init()
    }

    override fun initVertexData() {

        GLES30.glGenVertexArrays(1, vao, 0)
        vaoIndex = vao[0]

        GLES30.glGenBuffers(2, bufferIds, 0)
        vertexBufferId = bufferIds[0]
        texCoordBufferId = bufferIds[1]

        val vertex_List = ArrayList<Float>()
        val texture_List = ArrayList<FloatArray>()
        for (num in 0 until availableNum) {
            val temp_bottom_radius = bottomRadius * (jointNum - num) / jointNum.toFloat()
            val temp_top_radius = bottomRadius * (jointNum - (num + 1)) / jointNum.toFloat()
            val temp_bottom_height = num * jointHeight
            val temp_top_height = (num + 1) * jointHeight

            var hAngle = 0f
            while (hAngle < 360) {
                val x0 = (temp_top_radius * Math.cos(Math.toRadians(hAngle.toDouble()))).toFloat()
                val z0 = (temp_top_radius * Math.sin(Math.toRadians(hAngle.toDouble()))).toFloat()
                val x1 = (temp_bottom_radius * Math.cos(Math.toRadians(hAngle.toDouble()))).toFloat()
                val z1 = (temp_bottom_radius * Math.sin(Math.toRadians(hAngle.toDouble()))).toFloat()
                val x2 = (temp_top_radius * Math.cos(Math.toRadians((hAngle + longitudeSpan).toDouble()))).toFloat()
                val z2 = (temp_top_radius * Math.sin(Math.toRadians((hAngle + longitudeSpan).toDouble()))).toFloat()
                val x3 = (temp_bottom_radius * Math.cos(Math.toRadians((hAngle + longitudeSpan).toDouble()))).toFloat()
                val z3 = (temp_bottom_radius * Math.sin(Math.toRadians((hAngle + longitudeSpan).toDouble()))).toFloat()

                vertex_List.add(x0)
                vertex_List.add(temp_top_height)
                vertex_List.add(z0)
                vertex_List.add(x1)
                vertex_List.add(temp_bottom_height)
                vertex_List.add(z1)
                vertex_List.add(x2)
                vertex_List.add(temp_top_height)
                vertex_List.add(z2)

                vertex_List.add(x2)
                vertex_List.add(temp_top_height)
                vertex_List.add(z2)
                vertex_List.add(x1)
                vertex_List.add(temp_bottom_height)
                vertex_List.add(z1)
                vertex_List.add(x3)
                vertex_List.add(temp_bottom_height)
                vertex_List.add(z3)
                hAngle += longitudeSpan
            }
            val texcoor = generateTexCoor((360 / longitudeSpan).toInt(), 1)
            texture_List.add(texcoor)
        }
        val vertices = FloatArray(vertex_List.size)
        for (i in vertex_List.indices) {
            vertices[i] = vertex_List[i]
        }

        val al_temp = ArrayList<Float>()
        for (temp in texture_List) {
            for (tem in temp) {
                al_temp.add(tem)
            }
        }
        val texCoords = FloatArray(al_temp.size)
        for ((num, temp) in al_temp.withIndex()) {
            texCoords[num] = temp
        }

        vCounts = vertex_List.size / 3

        verticesBuffer = ByteBuffer.allocateDirect(vertices.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(vertices).position(0) as FloatBuffer

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBufferId)
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertices.size * FLOAT_SIZE, verticesBuffer, GLES30.GL_STATIC_DRAW)

        texCoorBuffer = ByteBuffer.allocateDirect(texCoords.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(texCoords).position(0) as FloatBuffer

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, texCoordBufferId)
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, texCoords.size * FLOAT_SIZE, texCoorBuffer, GLES30.GL_STATIC_DRAW)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("vertex_anim_tree_trunk_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("vertex_anim_tree_trunk_fragment.glsl")
        )
    }

    override fun initShaderParams() {
        aPosition = GLES30.glGetAttribLocation(program, "aPosition")
        aTexCoor = GLES30.glGetAttribLocation(program, "aTexCoor")
        uMVPMatrix = GLES30.glGetUniformLocation(program, "uMVPMatrix")
        uBendR = GLES30.glGetUniformLocation(program, "uBendR")
        uDirectionDegree = GLES30.glGetUniformLocation(program, "uDirectionDegree")
    }

    var bend: Int = 0
    var index: Int = 0
    override fun drawSelf(textureId: Int) {
//        index += 10
//        index %= 10000
        bend += 1
        bend %= 500
        MatrixState.rotate(xAngle, 1f, 0f, 0f)
        MatrixState.rotate(yAngle, 0f, 1f, 0f)
        GLES30.glUseProgram(program)

        GLES30.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES30.glUniform1f(uBendR, bend.toFloat())
        GLES30.glUniform1f(uDirectionDegree, index.toFloat())

        GLES30.glEnableVertexAttribArray(aPosition)
        GLES30.glEnableVertexAttribArray(aTexCoor)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBufferId)
        GLES30.glVertexAttribPointer(aPosition, posLen, GLES30.GL_FLOAT, false, posLen * FLOAT_SIZE, 0)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, texCoordBufferId)
        GLES30.glVertexAttribPointer(aTexCoor, texLen, GLES30.GL_FLOAT, false, texLen * FLOAT_SIZE, 0)

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId)

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCounts)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)
    }

    private fun generateTexCoor(bw: Int, bh: Int): FloatArray {
        val result = FloatArray(bw * bh * 6 * 2)
        val sizew = 1.0f / bw
        val sizeh = 1.0f / bh
        var c = 0
        for (i in 0 until bh) {
            for (j in 0 until bw) {
                val s = j * sizew
                val t = i * sizeh

                result[c++] = s
                result[c++] = t

                result[c++] = s
                result[c++] = t + sizeh

                result[c++] = s + sizew
                result[c++] = t

                result[c++] = s + sizew
                result[c++] = t

                result[c++] = s
                result[c++] = t + sizeh

                result[c++] = s + sizew
                result[c++] = t + sizeh
            }
        }
        return result
    }
}