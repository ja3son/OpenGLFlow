package com.ja3son.gllib.util

import android.content.res.Resources
import android.opengl.GLES30
import android.os.Build
import android.support.annotation.RequiresApi
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

object ETC2Utils {

    val PKM_HEADER_SIZE = 16
    val PKM_HEADER_WIDTH_OFFSET = 12
    val PKM_HEADER_HEIGHT_OFFSET = 14

    private fun loadDataFromAssets(fname: String, r: Resources): ByteArray? {
        var data: ByteArray? = null
        var inputStream: InputStream? = null
        try {
            inputStream = r.assets.open(fname)
            var ch: Int
            val baos = ByteArrayOutputStream()
            do {
                ch = inputStream!!.read()
                baos.write(ch)
            } while (ch != -1)
            data = baos.toByteArray()
            baos.close()
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return data
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun initTextureETC2(pkmName: String, r: Resources): Int {
        val textures = IntArray(1)
        GLES30.glGenTextures(
                1,
                textures,
                0
        )
        val textureId = textures[0]
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId)
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST.toFloat())
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR.toFloat())
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE.toFloat())
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE.toFloat())

        val data = loadDataFromAssets(pkmName, r)
        val buffer = ByteBuffer.allocateDirect(data!!.size).order(ByteOrder.LITTLE_ENDIAN).put(data).position(PKM_HEADER_SIZE)

        val header = ByteBuffer.allocateDirect(PKM_HEADER_SIZE).order(ByteOrder.BIG_ENDIAN)
        header.put(data, 0, PKM_HEADER_SIZE).position(0)

        val width = header.getShort(PKM_HEADER_WIDTH_OFFSET).toInt()
        val height = header.getShort(PKM_HEADER_HEIGHT_OFFSET).toInt()

        GLES30.glCompressedTexImage2D(
                GLES30.GL_TEXTURE_2D,
                0,
                GLES30.GL_COMPRESSED_RGBA8_ETC2_EAC,
                width,
                height,
                0,
                data.size - PKM_HEADER_SIZE,
                buffer
        )
        return textureId
    }
}