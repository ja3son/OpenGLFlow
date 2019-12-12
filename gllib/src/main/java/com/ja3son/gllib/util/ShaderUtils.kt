package com.ja3son.gllib.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.opengl.ETC1Util
import android.opengl.GLES30
import android.opengl.GLUtils
import android.os.Build
import android.support.annotation.RequiresApi
import com.ja3son.cgl.GL2JNILib
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
        GL2JNILib.nativeSetAssetManager(res.assets)
    }

    fun loadShader(shaderType: Int, source: String): Int {

        var shader: Int = GLES30.glCreateShader(shaderType)

        if (shader != 0) {
            GLES30.glShaderSource(shader, source)
            GLES30.glCompileShader(shader)
            val compiled = IntArray(1)
            GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compiled, 0)
            if (compiled[0] == 0) {
                LogUtils.eLog("Could not compile shader $shaderType : ")
                LogUtils.eLog(GLES30.glGetShaderInfoLog(shader))
                GLES30.glDeleteShader(shader)
                shader = 0
            }
        }

        return shader
    }

    fun createProgram(vertex: String, fragment: String): Int {
        var program = 0
        val vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, vertex)
        val fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, fragment)
        if (vertexShader != 0 && fragmentShader != 0) {
            program = GLES30.glCreateProgram()
            if (program != 0) {
                GLES30.glAttachShader(program, vertexShader)
                checkGLError("glAttachShader")
                GLES30.glAttachShader(program, fragmentShader)
                checkGLError("glAttachShader")
                GLES30.glLinkProgram(program)
                val linkStatus = IntArray(1)
                GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, linkStatus, 0)
                if (linkStatus[0] != GLES30.GL_TRUE) {
                    LogUtils.eLog("Could not link program: ")
                    LogUtils.eLog(GLES30.glGetProgramInfoLog(program))
                    GLES30.glDeleteProgram(program)
                    program = 0
                }
            }
        }
        return program
    }

    fun createProgramFeedback(vertex: String, fragment: String, attribute: String): Int {
        var program = 0
        val vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, vertex)
        val fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, fragment)
        if (vertexShader != 0 && fragmentShader != 0) {
            program = GLES30.glCreateProgram()
            if (program != 0) {
                GLES30.glAttachShader(program, vertexShader)
                checkGLError("glAttachShader")
                GLES30.glAttachShader(program, fragmentShader)
                checkGLError("glAttachShader")

                GLES30.glTransformFeedbackVaryings(program, arrayOf(attribute), GLES30.GL_INTERLEAVED_ATTRIBS)

                GLES30.glLinkProgram(program)
                val linkStatus = IntArray(1)
                GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, linkStatus, 0)
                if (linkStatus[0] != GLES30.GL_TRUE) {
                    LogUtils.eLog("Could not link program: ")
                    LogUtils.eLog(GLES30.glGetProgramInfoLog(program))
                    GLES30.glDeleteProgram(program)
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
        val error = GLES30.glGetError()
        if (error != GLES30.GL_NO_ERROR) {
            val log = "$op : glError $error"
            LogUtils.eLog(log)
            throw RuntimeException(log)
        }
    }

    fun genTexture(target: Int, width: Int, height: Int): Int {
        val textures = IntArray(1)
        GLES30.glGenTextures(1, textures, 0)
        val textureId = textures[0]
        GLES30.glBindTexture(target, textureId)
        GLES30.glTexParameteri(target, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR)
        GLES30.glTexParameteri(target, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR)
        GLES30.glTexParameteri(target, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE)
        GLES30.glTexParameteri(target, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE)
        GLES30.glTexImage2D(target, 0, GLES30.GL_RGBA, width, height, 0, GLES30.GL_RGBA, GLES30.GL_UNSIGNED_BYTE, null)
        GLES30.glBindTexture(target, 0)
        return textureId
    }

    fun genDepthTexture(target: Int, width: Int, height: Int): Int {
        val textures = IntArray(1)
        GLES30.glGenTextures(1, textures, 0)
        val textureId = textures[0]
        GLES30.glBindTexture(target, textureId)
        GLES30.glTexParameteri(target, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR)
        GLES30.glTexParameteri(target, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR)
        GLES30.glTexParameteri(target, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE)
        GLES30.glTexParameteri(target, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE)
        GLES30.glTexImage2D(target, 0, GLES30.GL_R16F, width, height, 0, GLES30.GL_RED, GLES30.GL_FLOAT, null)
        GLES30.glBindTexture(target, 0)
        return textureId
    }

    fun initTexture(drawable: Int): Int {
        val textures = IntArray(1)
        GLES30.glGenTextures(1, textures, 0)
        val textureId = textures[0]
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_REPEAT)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_REPEAT)

        val bitmap = BitmapFactory.decodeResource(res, drawable)

        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0)
        bitmap.recycle()
        return textureId
    }

    fun initCubmapTexture(drawables: IntArray): Int {
        val textures = IntArray(1)
        GLES30.glGenTextures(1, textures, 0)
        val textureId = textures[0]
        GLES30.glBindTexture(GLES30.GL_TEXTURE_CUBE_MAP, textureId)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_REPEAT)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_REPEAT)

        drawables.forEachIndexed { index, drawable ->
            run {
                val bitmap = BitmapFactory.decodeResource(res, drawable)
                GLUtils.texImage2D(GLES30.GL_TEXTURE_CUBE_MAP_POSITIVE_X + index, 0, bitmap, 0)
                bitmap.recycle()
            }
        }

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0)
        return textureId
    }

    fun initMipMapTexture(drawable: Int): Int {
        val textures = IntArray(1)
        GLES30.glGenTextures(1, textures, 0)
        val textureId = textures[0]
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR_MIPMAP_LINEAR)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_REPEAT)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_REPEAT)

        val bitmap = BitmapFactory.decodeResource(res, drawable)

        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0)
        GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0)
        bitmap.recycle()
        return textureId
    }

    fun initTextureEtc1(raw: Int): Int {
        val textures = IntArray(1)
        GLES30.glGenTextures(1, textures, 0)
        val textureId = textures[0]
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_REPEAT)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_REPEAT)

        val inputStream: InputStream = res.openRawResource(raw)

        ETC1Util.loadTexture(
                GLES30.GL_TEXTURE_2D,
                0,
                0,
                GLES30.GL_RGB,
                GLES30.GL_UNSIGNED_BYTE,
                inputStream
        )

        inputStream.close()
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0)
        return textureId
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun init3DTexture(texData: ByteArray, width: Int, height: Int, depth: Int)
            : Int {

        val textures = IntArray(1)
        GLES30.glGenTextures(
                1,
                textures,
                0
        )
        val textureId = textures[0]
        GLES30.glBindTexture(GLES30.GL_TEXTURE_3D, textureId)

        GLES30.glTexParameteri(GLES30.GL_TEXTURE_3D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST)

        GLES30.glTexParameteri(GLES30.GL_TEXTURE_3D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_NEAREST)

        GLES30.glTexParameteri(GLES30.GL_TEXTURE_3D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_REPEAT)

        GLES30.glTexParameteri(GLES30.GL_TEXTURE_3D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_REPEAT)

        GLES30.glTexParameteri(GLES30.GL_TEXTURE_3D, GLES30.GL_TEXTURE_WRAP_R, GLES30.GL_REPEAT)

        val texels = ByteBuffer.allocateDirect(texData.size)
        texels.put(texData)
        texels.position(0)

        GLES30.glTexImage3D(
                GLES30.GL_TEXTURE_3D,
                0,
                GLES30.GL_RGBA8,
                width,
                height,
                depth,
                0,
                GLES30.GL_RGBA,
                GLES30.GL_UNSIGNED_BYTE,
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
        GLES30.glGenTextures(1, textures, 0)
        val textureId = textures[0]
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D_ARRAY, textureId)
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D_ARRAY, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE.toFloat())
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D_ARRAY, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE.toFloat())
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D_ARRAY, GLES30.GL_TEXTURE_WRAP_R, GLES30.GL_CLAMP_TO_EDGE.toFloat())
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D_ARRAY, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST.toFloat())
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D_ARRAY, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR.toFloat())
        val texBuff = convertPicsToBuffer(picId, width, height)
        texBuff.position(0)
        GLES30.glTexImage3D(
                GLES30.GL_TEXTURE_2D_ARRAY,
                0,
                GLES30.GL_RGBA8,
                width,
                height,
                picId.size,
                0,
                GLES30.GL_RGBA,
                GLES30.GL_UNSIGNED_BYTE,
                texBuff
        )
        return textureId
    }

    fun getCrossProduct(x1: Float, y1: Float, z1: Float, x2: Float, y2: Float, z2: Float): FloatArray {
        val A = y1 * z2 - y2 * z1
        val B = z1 * x2 - z2 * x1
        val C = x1 * y2 - x2 * y1
        return floatArrayOf(A, B, C)
    }

    fun vectorNormal(vector: FloatArray): FloatArray {
        val module = Math.sqrt((vector[0] * vector[0] + vector[1] * vector[1] + vector[2] * vector[2]).toDouble()).toFloat()
        return floatArrayOf(vector[0] / module, vector[1] / module, vector[2] / module)
    }

    var vXYZ: FloatArray? = null
    var nXYZ: FloatArray? = null
    var tST: FloatArray? = null

    class Normal(var nx: Float, var ny: Float, var nz: Float) {
        override fun equals(other: Any?): Boolean {
            return if (other is Normal) {
                val tn = other as Normal?
                Math.abs(nx - tn!!.nx) < DIFF &&
                        Math.abs(ny - tn.ny) < DIFF &&
                        Math.abs(ny - tn.ny) < DIFF
            } else {
                false
            }
        }

        override fun hashCode(): Int {
            return 1
        }

        companion object {
            const val DIFF = 0.0000001f
            fun getAverage(sn: Set<Normal>): FloatArray {
                val result = FloatArray(3)
                for (n in sn) {
                    result[0] += n.nx
                    result[1] += n.ny
                    result[2] += n.nz
                }
                return vectorNormal(result)
            }
        }
    }

    fun loadObj(file: String) {
        val alv = ArrayList<Float>()
        val alvResult = ArrayList<Float>()
        val alFaceIndex = ArrayList<Int>()
        val hmn = HashMap<Int, HashSet<Normal>>()
        val alt = ArrayList<Float>()
        val altResult = ArrayList<Float>()

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
                    } else if (temps[0].trim() == "vt") {
                        alt.add(temps[1].toFloat())
                        alt.add(1 - temps[2].toFloat())
                    } else if (temps[0].trim { it <= ' ' } == "f") {
                        val index = IntArray(3)
                        index[0] = Integer.parseInt(temps[1].split("/")[0]) - 1

                        val x0 = alv[3 * index[0]]
                        val y0 = alv[3 * index[0] + 1]
                        val z0 = alv[3 * index[0] + 2]
                        alvResult.add(x0)
                        alvResult.add(y0)
                        alvResult.add(z0)


                        index[1] = Integer.parseInt(temps[2].split("/")[0]) - 1
                        val x1 = alv[3 * index[1]]
                        val y1 = alv[3 * index[1] + 1]
                        val z1 = alv[3 * index[1] + 2]
                        alvResult.add(x1)
                        alvResult.add(y1)
                        alvResult.add(z1)


                        index[2] = Integer.parseInt(temps[3].split("/")[0]) - 1
                        val x2 = alv[3 * index[2]]
                        val y2 = alv[3 * index[2] + 1]
                        val z2 = alv[3 * index[2] + 2]
                        alvResult.add(x2)
                        alvResult.add(y2)
                        alvResult.add(z2)


                        alFaceIndex.add(index[0])
                        alFaceIndex.add(index[1])
                        alFaceIndex.add(index[2])


                        val vxa = x1 - x0
                        val vya = y1 - y0
                        val vza = z1 - z0

                        val vxb = x2 - x0
                        val vyb = y2 - y0
                        val vzb = z2 - z0

                        val vNormal = vectorNormal(getCrossProduct(
                                vxa, vya, vza, vxb, vyb, vzb
                        ))

                        for (tempInxex in index) {
                            var hsn = hmn.get(tempInxex)
                            if (hsn == null) {
                                hsn = HashSet()
                            }
                            hsn.add(Normal(vNormal[0], vNormal[1], vNormal[2]))
                            hmn[tempInxex] = hsn
                        }

                        var indexTex = Integer.parseInt(temps[1].split("/")[1]) - 1
                        altResult.add(alt[indexTex * 2])
                        altResult.add(alt[indexTex * 2 + 1])

                        indexTex = Integer.parseInt(temps[2].split("/")[1]) - 1
                        altResult.add(alt[indexTex * 2])
                        altResult.add(alt[indexTex * 2 + 1])

                        indexTex = Integer.parseInt(temps[3].split("/")[1]) - 1
                        altResult.add(alt[indexTex * 2])
                        altResult.add(alt[indexTex * 2 + 1])
                    }
                }
            } while (temp != null)

            var size = alvResult.size
            vXYZ = FloatArray(size)
            for (i in 0 until size) {
                vXYZ!![i] = alvResult[i]
            }

            nXYZ = FloatArray(alFaceIndex.size * 3)
            var c = 0
            for (i in alFaceIndex) {
                val hsn = hmn[i]
                val tn = Normal.getAverage(hsn!!)
                nXYZ!![c++] = tn[0]
                nXYZ!![c++] = tn[1]
                nXYZ!![c++] = tn[2]
            }

            size = altResult.size
            tST = FloatArray(size)
            for (i in 0 until size) {
                tST!![i] = altResult[i]
            }
        } catch (e: Exception) {
            LogUtils.eLog("load error")
            e.printStackTrace()
        }
    }

    fun loadObjWithNormal(file: String) {
        val alv = ArrayList<Float>()
        val alvResult = ArrayList<Float>()
        val alt = ArrayList<Float>()
        val altResult = ArrayList<Float>()
        val aln = ArrayList<Float>()
        val alnResult = ArrayList<Float>()

        try {
            val inputStream = res.assets.open(file)
            val isr = InputStreamReader(inputStream)
            val br = BufferedReader(isr)
            var temp: String?

            do {
                temp = br.readLine()
                if (temp != null && temp.isNotEmpty() && !temp.startsWith("#")) {
                    val temps = temp.split("[ ]+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    if (temps[0].trim { it <= ' ' } == "v") {
                        alv.add(temps[1].toFloat())
                        alv.add(temps[2].toFloat())
                        alv.add(temps[3].toFloat())
                    } else if (temps[0].trim { it <= ' ' } == "vt") {
                        alt.add(temps[1].toFloat())
                        alt.add(1 - temps[2].toFloat())
                    } else if (temps[0].trim { it <= ' ' } == "vn") {
                        aln.add(temps[1].toFloat())
                        aln.add(temps[2].toFloat())
                        aln.add(temps[3].toFloat())
                    } else if (temps[0].trim { it <= ' ' } == "f") {
                        var index = Integer.parseInt(temps[1].split("/")[0]) - 1
                        val x0 = alv[3 * index]
                        val y0 = alv[3 * index + 1]
                        val z0 = alv[3 * index + 2]
                        alvResult.add(x0)
                        alvResult.add(y0)
                        alvResult.add(z0)

                        index = Integer.parseInt(temps[2].split("/")[0]) - 1
                        val x1 = alv[3 * index]
                        val y1 = alv[3 * index + 1]
                        val z1 = alv[3 * index + 2]
                        alvResult.add(x1)
                        alvResult.add(y1)
                        alvResult.add(z1)

                        index = Integer.parseInt(temps[3].split("/")[0]) - 1
                        val x2 = alv[3 * index]
                        val y2 = alv[3 * index + 1]
                        val z2 = alv[3 * index + 2]
                        alvResult.add(x2)
                        alvResult.add(y2)
                        alvResult.add(z2)

                        var indexTex = Integer.parseInt(temps[1].split("/")[1]) - 1
                        altResult.add(alt[indexTex * 2])
                        altResult.add(alt[indexTex * 2 + 1])

                        indexTex = Integer.parseInt(temps[2].split("/")[1]) - 1
                        altResult.add(alt[indexTex * 2])
                        altResult.add(alt[indexTex * 2 + 1])

                        indexTex = Integer.parseInt(temps[3].split("/")[1]) - 1
                        altResult.add(alt[indexTex * 2])
                        altResult.add(alt[indexTex * 2 + 1])

                        var indexN = Integer.parseInt(temps[1].split("/")[2]) - 1
                        val nx0 = aln[3 * indexN]
                        val ny0 = aln[3 * indexN + 1]
                        val nz0 = aln[3 * indexN + 2]
                        alnResult.add(nx0)
                        alnResult.add(ny0)
                        alnResult.add(nz0)

                        indexN = Integer.parseInt(temps[2].split("/")[2]) - 1
                        val nx1 = aln[3 * indexN]
                        val ny1 = aln[3 * indexN + 1]
                        val nz1 = aln[3 * indexN + 2]
                        alnResult.add(nx1)
                        alnResult.add(ny1)
                        alnResult.add(nz1)

                        indexN = Integer.parseInt(temps[3].split("/")[2]) - 1
                        val nx2 = aln[3 * indexN]
                        val ny2 = aln[3 * indexN + 1]
                        val nz2 = aln[3 * indexN + 2]
                        alnResult.add(nx2)
                        alnResult.add(ny2)
                        alnResult.add(nz2)
                    }
                }
            } while (temp != null)

            var size = alvResult.size
            vXYZ = FloatArray(size)
            for (i in 0 until size) {
                vXYZ!![i] = alvResult[i]
            }

            size = alnResult.size
            nXYZ = FloatArray(size)
            for (i in 0 until size) {
                nXYZ!![i] = alnResult[i]
            }

            size = altResult.size
            tST = FloatArray(size)
            for (i in 0 until size) {
                tST!![i] = altResult[i]
            }
        } catch (e: Exception) {
            LogUtils.eLog("load error")
            e.printStackTrace()
        }
    }

    fun loadObjOrigin(file: String) {
        val alv = ArrayList<Float>()
        val alFaceIndex = ArrayList<Int>()
        val alvResult = ArrayList<Float>()
        val hmn = HashMap<Int, HashSet<Normal>>()

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
                        val index = IntArray(3)

                        index[0] = Integer.parseInt(temps[1].split("/")[0]) - 1
                        val x0 = alv[3 * index[0]]
                        val y0 = alv[3 * index[0] + 1]
                        val z0 = alv[3 * index[0] + 2]
                        alvResult.add(x0)
                        alvResult.add(y0)
                        alvResult.add(z0)

                        index[1] = Integer.parseInt(temps[2].split("/")[0]) - 1
                        val x1 = alv[3 * index[1]]
                        val y1 = alv[3 * index[1] + 1]
                        val z1 = alv[3 * index[1] + 2]
                        alvResult.add(x1)
                        alvResult.add(y1)
                        alvResult.add(z1)

                        index[2] = Integer.parseInt(temps[3].split("/")[0]) - 1
                        val x2 = alv[3 * index[2]]
                        val y2 = alv[3 * index[2] + 1]
                        val z2 = alv[3 * index[2] + 2]
                        alvResult.add(x2)
                        alvResult.add(y2)
                        alvResult.add(z2)

                        alFaceIndex.add(index[0])
                        alFaceIndex.add(index[1])
                        alFaceIndex.add(index[2])

                        val vxa = x1 - x0
                        val vya = y1 - y0
                        val vza = z1 - z0

                        val vxb = x2 - x0
                        val vyb = y2 - y0
                        val vzb = z2 - z0

                        val vNormal = vectorNormal(getCrossProduct(
                                vxa, vya, vza, vxb, vyb, vzb
                        ))

                        for (tempInxex in index) {
                            var hsn = hmn[tempInxex]
                            if (hsn == null) {
                                hsn = HashSet()
                            }
                            hsn.add(Normal(vNormal[0], vNormal[1], vNormal[2]))
                            hmn[tempInxex] = hsn
                        }
                    }
                }
            } while (temp != null)

            var size = alvResult.size
            vXYZ = FloatArray(size)
            for (i in 0 until size) {
                vXYZ!![i] = alvResult[i]
            }

            nXYZ = FloatArray(alFaceIndex.size * 3)
            var c = 0
            for (i in alFaceIndex) {
                val hsn = hmn[i]
                val tn = Normal.getAverage(hsn!!)
                nXYZ!![c++] = tn[0]
                nXYZ!![c++] = tn[1]
                nXYZ!![c++] = tn[2]
            }
        } catch (e: Exception) {
            LogUtils.eLog("load error")
            e.printStackTrace()
        }
    }

    fun getWidth(resId: Int): Int {
        val option = BitmapFactory.Options()
        option.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, resId, option)
        return option.outWidth
    }

    fun getHeight(resId: Int): Int {
        val option = BitmapFactory.Options()
        option.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, resId, option)
        return option.outHeight
    }

    fun loadTex3D(fileName: String): Tex3D {
        val result = Tex3D()
        try {
            val fin = res.assets.open(fileName)
            var buf = ByteArray(4)
            fin.read(buf)
            result.width = buf[0].toInt()
            fin.read(buf)
            result.height = buf[0].toInt()
            fin.read(buf)
            result.depth = buf[0].toInt()
            buf = ByteArray(result.width * result.height * result.depth * 4)
            fin.read(buf)
            fin.close()
            result.data = buf
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }

    class Tex3D {
        var width: Int = 0
        var height: Int = 0
        var depth: Int = 0
        var data: ByteArray? = null
    }

    fun loadLandForms(index: Int): Array<FloatArray> {
        val LAND_HIGH_ADJUST = 2f
        val LAND_HIGHEST = 1.5f
        val bt = BitmapFactory.decodeResource(res, index)
        val colsPlusOne = bt.width
        val rowsPlusOne = bt.height
        val result = Array(rowsPlusOne) { FloatArray(colsPlusOne) }
        for (i in 0 until rowsPlusOne) {
            for (j in 0 until colsPlusOne) {
                val color = bt.getPixel(j, i)
                val r = Color.red(color)
                val g = Color.green(color)
                val b = Color.blue(color)
                val h = (r + g + b) / 3
                result[i][j] = h * LAND_HIGHEST / 255
            }
        }
        return result
    }
}