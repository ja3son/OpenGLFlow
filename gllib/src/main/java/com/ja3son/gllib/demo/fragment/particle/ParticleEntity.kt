package com.ja3son.gllib.demo.fragment.particle

import android.opengl.GLES30
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState
import com.ja3son.gllib.util.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


class ParticleEntity(private val z_counts: Int) : BaseEntity() {

    private val bufferIds = IntArray(3)
    private var vertexBufferSpeedXId = 0
    private var vertexBufferSpeedYId = 0
    private var vertexBufferFeedbackId = 0

    private var uMaxLifeSpan = 0
    private var uRadius = 0
    private var uStartColor = 0
    private var uEndColor = 0

    private var program_feedback = 0
    private var speedx_pos_feedback = 0
    private var speedy_pos_feedback = 0
    private var count_feedback = 0
    private var group_count_feedback = 0
    private var life_span_step_feedback = 0

    private lateinit var speed_x_points: FloatArray
    private lateinit var speed_y_points: FloatArray

    private var pre_time: Long = 0

    private val X_RANGE: Float = 0.5f
    private val Y_RANGE: Float = 0.3f
    private val SPEED_Y: Float = 0.05f
    private val RADIUS: Float = 60 * 0.5f
    private val MAX_LIFE_SPAN: Float = 5.0f
    private val LIFE_SPAN_STEP: Float = 0.07f
    private val GROUP_COUNT: Int = 4
    private val THREAD_SLEEP: Int = 60
    private val START_COLOR = floatArrayOf(0.7569f, 0.2471f, 0.1176f, 1.0f)
    private val END_COLOR = floatArrayOf(0.0f, 0.0f, 0.0f, 0.0f)

    private var feedback_counts = 1

    var FLAG: Boolean = false

    init {
        pre_time = System.nanoTime()
        init()
    }

    override fun initVertexData() {
        GLES30.glGenBuffers(3, bufferIds, 0)
        vertexBufferSpeedXId = bufferIds[0]
        vertexBufferSpeedYId = bufferIds[1]
        vertexBufferFeedbackId = bufferIds[2]

        initPoints(z_counts)

        vCounts = z_counts
        verticesBuffer = ByteBuffer.allocateDirect(speed_x_points.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(speed_x_points).position(0) as FloatBuffer

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBufferSpeedXId)
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, speed_x_points.size * FLOAT_SIZE, verticesBuffer, GLES30.GL_STATIC_DRAW)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBufferFeedbackId)
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, speed_x_points.size * FLOAT_SIZE, verticesBuffer, GLES30.GL_STATIC_DRAW)

        val originBuffer = ByteBuffer.allocateDirect(speed_y_points.size * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
                .put(speed_y_points).position(0) as FloatBuffer

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBufferSpeedYId)
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, speed_y_points.size * FLOAT_SIZE, originBuffer, GLES30.GL_STATIC_DRAW)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)
    }

    override fun initShader() {
        program = ShaderUtils.createProgram(
                ShaderUtils.loadFromAssetsFile("particle_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("particle_fragment.glsl")
        )

        program_feedback = ShaderUtils.createProgramFeedback(
                ShaderUtils.loadFromAssetsFile("particle_feedback_vertex.glsl"),
                ShaderUtils.loadFromAssetsFile("particle_feedback_fragment.glsl"),
                "vPosition"
        )
    }

    override fun initShaderParams() {
        super.initShaderParams()
        uMaxLifeSpan = GLES30.glGetUniformLocation(program, "uMaxLifeSpan")
        uRadius = GLES30.glGetUniformLocation(program, "uRadius")
        uStartColor = GLES30.glGetUniformLocation(program, "uStartColor")
        uEndColor = GLES30.glGetUniformLocation(program, "uEndColor")

        speedx_pos_feedback = GLES30.glGetAttribLocation(program_feedback, "speedx_pos_feedback")
        speedy_pos_feedback = GLES30.glGetAttribLocation(program_feedback, "speedy_pos_feedback")
        count_feedback = GLES30.glGetUniformLocation(program_feedback, "count_feedback")
        group_count_feedback = GLES30.glGetUniformLocation(program_feedback, "group_count_feedback")
        life_span_step_feedback = GLES30.glGetUniformLocation(program_feedback, "life_span_step_feedback")
    }

    override fun drawSelf(textureId: Int) {
        MatrixState.rotate(xAngle, 1f, 0f, 0f)
        MatrixState.rotate(yAngle, 0f, 1f, 0f)
        GLES30.glDisable(GLES30.GL_DEPTH_TEST)
        GLES30.glEnable(GLES30.GL_BLEND)
        GLES30.glBlendEquation(GLES30.GL_FUNC_ADD)
        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE)
        val curr_time = System.nanoTime()
        if (curr_time - pre_time > THREAD_SLEEP * 1000000) {
            draw_feedback(feedback_counts, GROUP_COUNT, LIFE_SPAN_STEP)
            FLAG = !FLAG
            pre_time = curr_time
            if (feedback_counts >= (speed_x_points.size / GROUP_COUNT / 4)) {
                feedback_counts = 0
            }
            feedback_counts++
        }
        draw_origin(textureId, MAX_LIFE_SPAN, START_COLOR, END_COLOR)
        GLES30.glDisable(GLES30.GL_BLEND)
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
    }

    private fun draw_feedback(feedback_counts: Int, group_count: Int, life_span_step: Float) {
        GLES30.glUseProgram(program_feedback)
        GLES30.glBindBufferBase(GLES30.GL_TRANSFORM_FEEDBACK_BUFFER, 0, (if (FLAG) vertexBufferSpeedXId else vertexBufferFeedbackId))
        // 开启禁止栅格化 顶点着色器中out变量不进入片元着色器 写入变换反馈缓冲对象
        GLES30.glEnable(GLES30.GL_RASTERIZER_DISCARD)
        GLES30.glUniform1i(count_feedback, feedback_counts)
        GLES30.glUniform1i(group_count_feedback, group_count)
        GLES30.glUniform1f(life_span_step_feedback, life_span_step)
        GLES30.glEnableVertexAttribArray(speedx_pos_feedback)
        GLES30.glEnableVertexAttribArray(speedy_pos_feedback)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, (if (FLAG) vertexBufferFeedbackId else vertexBufferSpeedXId))
        GLES30.glVertexAttribPointer(speedx_pos_feedback, pos4Len, GLES30.GL_FLOAT, false, pos4Len * FLOAT_SIZE, 0)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBufferSpeedYId)
        GLES30.glVertexAttribPointer(speedy_pos_feedback, pos4Len, GLES30.GL_FLOAT, false, pos4Len * FLOAT_SIZE, 0)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)
        // 启用变换反馈渲染-顶点结果按GL_POINTS（点）组织形式输出到指定的变换反馈缓冲区中
        GLES30.glBeginTransformFeedback(GLES30.GL_POINTS)
        GLES30.glDrawArrays(GLES30.GL_POINTS, 0, vCounts)
        GLES30.glDisableVertexAttribArray(speedx_pos_feedback)
        GLES30.glDisableVertexAttribArray(speedy_pos_feedback)
        GLES30.glEndTransformFeedback()
        GLES30.glDisable(GLES30.GL_RASTERIZER_DISCARD)
        GLES30.glUseProgram(0)
        GLES30.glBindBufferBase(GLES30.GL_TRANSFORM_FEEDBACK_BUFFER, 0, 0)
    }

    private fun draw_origin(textureId: Int, maxLifeSpan: Float, startColor: FloatArray, endColor: FloatArray) {
        GLES30.glUseProgram(program)
        GLES30.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0)
        GLES30.glUniform1f(uMaxLifeSpan, maxLifeSpan)
        GLES30.glUniform1f(uRadius, RADIUS)
        GLES30.glUniform4fv(uStartColor, 1, startColor, 0)
        GLES30.glUniform4fv(uEndColor, 1, endColor, 0)
        GLES30.glUniform3fv(uCamera, 1, MatrixState.cameraFB)
        GLES30.glUniformMatrix4fv(uMMatrix, 1, false, MatrixState.getModelMatrix(), 0)
        GLES30.glEnableVertexAttribArray(aPosition)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, (if (FLAG) vertexBufferSpeedXId else vertexBufferFeedbackId))
        GLES30.glVertexAttribPointer(aPosition, pos4Len, GLES30.GL_FLOAT, false, pos4Len * FLOAT_SIZE, 0)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId)
        GLES30.glDrawArrays(GLES30.GL_POINTS, 0, vCounts)
        GLES30.glDisableVertexAttribArray(aPosition)
    }

    private fun initPoints(z_counts: Int) {
        speed_x_points = FloatArray(z_counts * 4)
        speed_y_points = FloatArray(z_counts * 4)
        for (i in 0 until z_counts) {
            val fountain_center_x = (X_RANGE * (Math.random() * 2 - 1.0f)).toFloat()
            val fountain_center_y = (Y_RANGE * (Math.random() * 2 - 1.0f)).toFloat()
            val speed_x = (-fountain_center_x) / 150.0f
            speed_x_points[i * 4] = fountain_center_x
            speed_x_points[i * 4 + 1] = fountain_center_y
            speed_x_points[i * 4 + 2] = speed_x
            speed_x_points[i * 4 + 3] = 10.0f

            speed_y_points[i * 4] = fountain_center_x
            speed_y_points[i * 4 + 1] = fountain_center_y
            speed_y_points[i * 4 + 2] = SPEED_Y
            speed_y_points[i * 4 + 3] = MAX_LIFE_SPAN
        }

        for (j in 0 until GROUP_COUNT) {
            speed_x_points[4 * j + 3] = LIFE_SPAN_STEP
        }
    }
}