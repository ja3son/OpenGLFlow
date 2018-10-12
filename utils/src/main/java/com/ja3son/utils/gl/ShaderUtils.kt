package com.ja3son.utils.gl

import android.content.res.Resources
import android.opengl.GLES32
import com.ja3son.utils.log.LogUtils

object ShaderUtils {

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
        val fragmentShader = loadShader(GLES32.GL_FRAGMENT_SHADER, vertex)
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

    fun loadFromAssetsFile(fName: String, res: Resources) =
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
}