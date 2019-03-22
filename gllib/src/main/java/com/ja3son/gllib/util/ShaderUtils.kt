package com.ja3son.gllib.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.ETC1Util
import android.opengl.GLES32
import android.opengl.GLUtils
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import com.ja3son.utils.log.LogUtils
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.ByteBuffer


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
        GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_WRAP_S, GLES32.GL_REPEAT)
        GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_WRAP_T, GLES32.GL_REPEAT)

        val bitmap = BitmapFactory.decodeResource(res, drawable)

        GLUtils.texImage2D(GLES32.GL_TEXTURE_2D, 0, bitmap, 0)
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, 0)
        bitmap.recycle()
        return textureId
    }

    fun initTextureEtc1(raw: Int): Int {
        val textures = IntArray(1)
        GLES32.glGenTextures(1, textures, 0)
        val textureId = textures[0]
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, textureId)
        GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_MIN_FILTER, GLES32.GL_NEAREST)
        GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_MAG_FILTER, GLES32.GL_LINEAR)
        GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_WRAP_S, GLES32.GL_REPEAT)
        GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_WRAP_T, GLES32.GL_REPEAT)

        val inputStream: InputStream = res.openRawResource(raw)

        ETC1Util.loadTexture(
                GLES32.GL_TEXTURE_2D,
                0,
                0,
                GLES32.GL_RGB,
                GLES32.GL_UNSIGNED_BYTE,
                inputStream
        )

        inputStream.close()
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, 0)
        return textureId
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun init3DTexture(texData: ByteArray, width: Int, height: Int, depth: Int)
            : Int {

        val textures = IntArray(1)
        GLES32.glGenTextures(
                1,
                textures,
                0
        )
        val textureId = textures[0]
        GLES32.glBindTexture(GLES32.GL_TEXTURE_3D, textureId)

        GLES32.glTexParameteri(GLES32.GL_TEXTURE_3D, GLES32.GL_TEXTURE_MIN_FILTER, GLES32.GL_NEAREST)

        GLES32.glTexParameteri(GLES32.GL_TEXTURE_3D, GLES32.GL_TEXTURE_MAG_FILTER, GLES32.GL_NEAREST)

        GLES32.glTexParameteri(GLES32.GL_TEXTURE_3D, GLES32.GL_TEXTURE_WRAP_S, GLES32.GL_REPEAT)

        GLES32.glTexParameteri(GLES32.GL_TEXTURE_3D, GLES32.GL_TEXTURE_WRAP_T, GLES32.GL_REPEAT)

        GLES32.glTexParameteri(GLES32.GL_TEXTURE_3D, GLES32.GL_TEXTURE_WRAP_R, GLES32.GL_REPEAT)

        val texels = ByteBuffer.allocateDirect(texData.size)
        texels.put(texData)
        texels.position(0)

        GLES32.glTexImage3D(
                GLES32.GL_TEXTURE_3D,
                0,
                GLES32.GL_RGBA8,
                width,
                height,
                depth,
                0,
                GLES32.GL_RGBA,
                GLES32.GL_UNSIGNED_BYTE,
                texels
        )

        return textureId
    }

    fun convertPicsToBuffer(resIds: IntArray, width: Int, height: Int): ByteBuffer {
        val perPicByteCount = width * height * 4
        val buf = ByteBuffer.allocateDirect(perPicByteCount * resIds.size)

        for (i in 0 until resIds.size) {
            val id = resIds[i]
            val inputStream = res.openRawResource(id)
            val bitmapTmp: Bitmap
            try {
                bitmapTmp = BitmapFactory.decodeStream(inputStream)
            } finally {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            buf.position(i * perPicByteCount)
            bitmapTmp.copyPixelsToBuffer(buf)
            bitmapTmp.recycle()
        }
        return buf
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun initTextureArray(picId: IntArray, width: Int, height: Int): Int {
        val textures = IntArray(1)
        GLES32.glGenTextures(1, textures, 0)
        val textureId = textures[0]
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D_ARRAY, textureId)
        GLES32.glTexParameterf(GLES32.GL_TEXTURE_2D_ARRAY, GLES32.GL_TEXTURE_WRAP_S, GLES32.GL_CLAMP_TO_EDGE.toFloat())
        GLES32.glTexParameterf(GLES32.GL_TEXTURE_2D_ARRAY, GLES32.GL_TEXTURE_WRAP_T, GLES32.GL_CLAMP_TO_EDGE.toFloat())
        GLES32.glTexParameterf(GLES32.GL_TEXTURE_2D_ARRAY, GLES32.GL_TEXTURE_WRAP_R, GLES32.GL_CLAMP_TO_EDGE.toFloat())
        GLES32.glTexParameterf(GLES32.GL_TEXTURE_2D_ARRAY, GLES32.GL_TEXTURE_MIN_FILTER, GLES32.GL_NEAREST.toFloat())
        GLES32.glTexParameterf(GLES32.GL_TEXTURE_2D_ARRAY, GLES32.GL_TEXTURE_MAG_FILTER, GLES32.GL_LINEAR.toFloat())
        val texBuff = convertPicsToBuffer(picId, width, height)
        texBuff.position(0)
        GLES32.glTexImage3D(
                GLES32.GL_TEXTURE_2D_ARRAY,
                0,
                GLES32.GL_RGBA8,
                width,
                height,
                picId.size,
                0,
                GLES32.GL_RGBA,
                GLES32.GL_UNSIGNED_BYTE,
                texBuff
        )
        return textureId
    }

    fun loadObjVertex(file: String): FloatArray? {
        var vXYZ: FloatArray? = null

        val alv = ArrayList<Float>()
        val alvResult = ArrayList<Float>()

        try {
            val inputStream = res.assets.open(file)
            val isr = InputStreamReader(inputStream)
            val br = BufferedReader(isr)
            var temp: String?

            do {
                temp = br.readLine()
                if (temp != null && temp != "") {
                    val temps = temp.split("[ ]+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    if (temps[0].trim { it <= ' ' } == "v") {
                        alv.add(temps[1].toFloat())
                        alv.add(temps[2].toFloat())
                        alv.add(temps[3].toFloat())
                    } else if (temps[0].trim { it <= ' ' } == "f") {
                        var index = Integer.parseInt(temps[1].split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]) - 1

                        alvResult.add(alv[3 * index])
                        alvResult.add(alv[3 * index + 1])
                        alvResult.add(alv[3 * index + 2])

                        index = Integer.parseInt(temps[2].split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]) - 1

                        alvResult.add(alv[3 * index])
                        alvResult.add(alv[3 * index + 1])
                        alvResult.add(alv[3 * index + 2])

                        index = Integer.parseInt(temps[3].split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]) - 1

                        alvResult.add(alv[3 * index])
                        alvResult.add(alv[3 * index + 1])
                        alvResult.add(alv[3 * index + 2])
                    }
                }
            } while (temp != null)

            val size = alvResult.size
            vXYZ = FloatArray(size)
            for (i in 0 until size) {
                vXYZ[i] = alvResult[i]
            }
        } catch (e: Exception) {
            Log.e("ja333son", "load error" + e.message)
            e.printStackTrace()
        }

        return vXYZ
    }

}