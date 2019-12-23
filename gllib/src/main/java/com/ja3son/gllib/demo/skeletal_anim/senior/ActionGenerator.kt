package com.ja3son.gllib.demo.skeletal_anim.senior

internal class Action {
    lateinit var data: Array<FloatArray>
    var totalStep = 0
}

internal object ActionGenerator {
    var acArray: Array<Action?> = arrayOfNulls(4)

    init {
        acArray[0] = Action()
        acArray[1] = Action()
        acArray[2] = Action()
        acArray[3] = Action()
        val count = 20
        acArray[0]!!.totalStep = count
        acArray[0]!!.data = arrayOf(floatArrayOf(1f, 0f, 0f, 0f, 0f, 0f, 0f, 0f), floatArrayOf(1f, 1f, 10f, 10f, 1f, 0f, 0f), floatArrayOf(3f, 1f, -70f, 0f, 0.948f, 0f, 0.316f), floatArrayOf(5f, 1f, -70f, 0f, -0.948f, 0f, 0.316f), floatArrayOf(4f, 1f, -80f, -80f, 0.948f, 0f, 0.316f), floatArrayOf(6f, 1f, 80f, 80f, -0.948f, 0f, 0.316f), floatArrayOf(7f, 1f, -50f, 0f, 1f, 0f, 0f), floatArrayOf(9f, 1f, 20f, 0f, 1f, 0f, 0f), floatArrayOf(10f, 1f, 0f, 90f, 1f, 0f, 0f), floatArrayOf(11f, 1f, 10f, 0f, 1f, 0f, 0f))
        acArray[1]!!.totalStep = count
        acArray[1]!!.data = arrayOf(floatArrayOf(1f, 0f, 0f, 0f, 0f, 0f, 0f, 0f), floatArrayOf(1f, 1f, 10f, 10f, 1f, 0f, 0f), floatArrayOf(3f, 1f, 0f, 70f, 0.948f, 0f, 0.316f), floatArrayOf(5f, 1f, 0f, 70f, -0.948f, 0f, 0.316f), floatArrayOf(4f, 1f, -80f, -80f, 0.948f, 0f, 0.316f), floatArrayOf(6f, 1f, 80f, 80f, -0.948f, 0f, 0.316f), floatArrayOf(7f, 1f, 0f, 20f, 1f, 0f, 0f), floatArrayOf(9f, 1f, 0f, -50f, 1f, 0f, 0f), floatArrayOf(10f, 1f, 90f, 0f, 1f, 0f, 0f), floatArrayOf(12f, 1f, 0f, 10f, 1f, 0f, 0f))
        acArray[2]!!.totalStep = count
        acArray[2]!!.data = arrayOf(floatArrayOf(1f, 0f, 0f, 0f, 0f, 0f, 0f, 0f), floatArrayOf(1f, 1f, 10f, 10f, 1f, 0f, 0f), floatArrayOf(3f, 1f, 70f, 0f, 0.948f, 0f, 0.316f), floatArrayOf(5f, 1f, 70f, 0f, -0.948f, 0f, 0.316f), floatArrayOf(4f, 1f, -80f, -80f, 0.948f, 0f, 0.316f), floatArrayOf(6f, 1f, 80f, 80f, -0.948f, 0f, 0.316f), floatArrayOf(7f, 1f, 20f, 0f, 1f, 0f, 0f), floatArrayOf(9f, 1f, -50f, 0f, 1f, 0f, 0f), floatArrayOf(8f, 1f, 0f, 90f, 1f, 0f, 0f), floatArrayOf(12f, 1f, 10f, 0f, 1f, 0f, 0f))
        acArray[3]!!.totalStep = count
        acArray[3]!!.data = arrayOf(floatArrayOf(1f, 0f, 0f, 0f, 0f, 0f, 0f, 0f), floatArrayOf(1f, 1f, 10f, 10f, 1f, 0f, 0f), floatArrayOf(3f, 1f, 0f, -70f, 0.948f, 0f, 0.316f), floatArrayOf(5f, 1f, 0f, -70f, -0.948f, 0f, 0.316f), floatArrayOf(4f, 1f, -80f, -80f, 0.948f, 0f, 0.316f), floatArrayOf(6f, 1f, 80f, 80f, -0.948f, 0f, 0.316f), floatArrayOf(7f, 1f, 0f, -50f, 1f, 0f, 0f), floatArrayOf(9f, 1f, 0f, 20f, 1f, 0f, 0f), floatArrayOf(8f, 1f, 90f, 0f, 1f, 0f, 0f), floatArrayOf(11f, 1f, 0f, 10f, 1f, 0f, 0f))
    }
}