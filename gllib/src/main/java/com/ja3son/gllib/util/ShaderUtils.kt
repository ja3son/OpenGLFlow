package com.ja3son.gllib.util

import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.opengl.GLES32
import android.opengl.GLUtils
import com.ja3son.utils.log.LogUtils

object ShaderUtils {
    private lateinit var res: Resources

    fun register(context: Context) {
        this.res = context.resources
    }

    fun loadShader(shaderType: Int, source: String): Int {

        var shader: Int = GLES32.glCreateShader(shaderType)

        if (shader != 0) {
            GLES32.glShaderSource(shader, source)
            GLES32.glCompileShader(shader)
            val compiled = IntArray(1)
            GLES32.glGetShaderiv(shader, GLES32.GL_COMPILE_STATUS, compiled, 0)
            if (compiled[0] == 0) {
                LogUtils.eLog("Could not compile shader $shaderType : ")
                LogUtils.eLog(GLES32.glGetShaderInfoLog(shader))
                GLES32.glDeleteShader(shader)
                shader = 0
            }
        }

        return shader
    }

    fun createProgram(vertex: String, fragment: String): Int {
        var program = 0
        val vertexShader = loadShader(GLES32.GL_VERTEX_SHADER, vertex)
        val fragmentShader = loadShader(GLES32.GL_FRAGMENT_SHADER, fragment)
        if (vertexShader != 0 && fragmentShader != 0) {
            program = GLES32.glCreateProgram()
            if (program != 0) {
                GLES32.glAttachShader(program, vertexShader)
                checkGLError("glAttachShader")
                GLES32.glAttachShader(program, fragmentShader)
                checkGLError("glAttachShader")
                GLES32.glLinkProgram(program)
                val linkStatus = IntArray(1)
                GLES32.glGetProgramiv(program, GLES32.GL_LINK_STATUS, linkStatus, 0)
                if (linkStatus[0] != GLES32.GL_TRUE) {
                    LogUtils.eLog("Could not link program: ")
                    LogUtils.eLog(GLES32.glGetProgramInfoLog(program))
                    GLES32.glDeleteProgram(program)
                    program = 0
                }
            }
        }
        return program
    }

    fun loadFromAssetsFile(fName: String) =
            res.assets.open(fName).bufferedReader().use {
                it.readText()
            }


    fun checkGLError(op: String) {
        val error = GLES32.glGetError()
        if (error != GLES32.GL_NO_ERROR) {
            val log = "$op : glError $error"
            LogUtils.eLog(log)
            throw RuntimeException(log)
        }
    }

    fun initTexture(drawable: Int): Int {
        val textures = IntArray(1)
        GLES32.glGenTextures(1, textures, 0)
        val textureId = textures[0]
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, textureId)
        GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_MIN_FILTER, GLES32.GL_NEAREST)
        GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_MAG_FILTER, GLES32.GL_LINEAR)
        GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_WRAP_S, GLES32.GL_CLAMP_TO_EDGE)
        GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_WRAP_T, GLES32.GL_CLAMP_TO_EDGE)

        val bitmap = BitmapFactory.decodeResource(res, drawable)

        GLUtils.texImage2D(GLES32.GL_TEXTURE_2D, 0, bitmap, 0)
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, 0)
        bitmap.recycle()
        return textureId
    }
}