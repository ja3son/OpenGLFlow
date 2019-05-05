package com.ja3son.gllib.demo.skybox.font

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint

object FontUtil {
    var cIndex = 0
    private const val textSize = 40f
    var R = 255
    var G = 255
    var B = 255

    var content = arrayOf(
            "赵客缦胡缨，吴钩霜雪明。",
            "银鞍照白马，飒沓如流星。",
            "十步杀一人，千里不留行。",
            "事了拂衣去，深藏身与名。",
            "闲过信陵饮，脱剑膝前横。",
            "将炙啖朱亥，持觞劝侯嬴。",
            "三杯吐然诺，五岳倒为轻。",
            "眼花耳热后，意气素霓生。",
            "救赵挥金槌，邯郸先震惊。",
            "千秋二壮士，煊赫大梁城。",
            "纵死侠骨香，不惭世上英。",
            "谁能书閤下，白首太玄经。"
    )

    fun generateWLT(str: Array<String?>, width: Int, height: Int): Bitmap {
        val paint = Paint()
        paint.setARGB(255, R, G, B)
        paint.textSize = textSize
        paint.typeface = null
        paint.flags = Paint.ANTI_ALIAS_FLAG
        val bmTemp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvasTemp = Canvas(bmTemp)
        for (i in str.indices) {
            canvasTemp.drawText(str[i], 0f, textSize * i + (i - 1) * 5, paint)
        }
        return bmTemp
    }

    fun getContent(length: Int, content: Array<String>): Array<String?> {
        val result = arrayOfNulls<String>(length + 1)
        for (i in 0..length) {
            result[i] = content[i]
        }
        return result
    }

    fun updateRGB() {
        R = (255 * Math.random()).toInt()
        G = (255 * Math.random()).toInt()
        B = (255 * Math.random()).toInt()
    }
}