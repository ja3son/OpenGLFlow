package com.ja3son.gllib.util

import android.content.Context
import android.content.res.Resources
import com.ja3son.gllib.util.ShaderUtils.Normal
import java.io.BufferedReader
import java.io.InputStreamReader


object OBJUtils {
    private lateinit var res: Resources

    var vXYZ: FloatArray? = null
    var nXYZ: FloatArray? = null
    var tST: FloatArray? = null
    var tnXYZ: FloatArray? = null

    fun register(context: Context) {
        this.res = context.resources
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

    fun loadVertexOnlyAverage(fname: String) {
        val alv = ArrayList<Float>()
        val alFaceIndex = ArrayList<Int>()
        val alvResult = ArrayList<Float>()
        val hmn = HashMap<Int, HashSet<Normal>>()

        try {
            val inputStream = res.assets.open(fname)
            val isr = InputStreamReader(inputStream)
            val br = BufferedReader(isr)
            var temps: String?

            do {
                temps = br.readLine()
                if (temps != null && temps != "") {
                    val tempsa = temps!!.split("[ ]+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    if (tempsa[0].trim { it <= ' ' } == "v") {
                        alv.add(java.lang.Float.parseFloat(tempsa[1]))
                        alv.add(java.lang.Float.parseFloat(tempsa[2]))
                        alv.add(java.lang.Float.parseFloat(tempsa[3]))
                    } else if (tempsa[0].trim { it <= ' ' } == "f") {
                        val index = IntArray(3)
                        index[0] = Integer.parseInt(tempsa[1].split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]) - 1
                        val x0 = alv[3 * index[0]]
                        val y0 = alv[3 * index[0] + 1]
                        val z0 = alv[3 * index[0] + 2]
                        alvResult.add(x0)
                        alvResult.add(y0)
                        alvResult.add(z0)

                        index[1] = Integer.parseInt(tempsa[2].split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]) - 1
                        val x1 = alv[3 * index[1]]
                        val y1 = alv[3 * index[1] + 1]
                        val z1 = alv[3 * index[1] + 2]
                        alvResult.add(x1)
                        alvResult.add(y1)
                        alvResult.add(z1)

                        index[2] = Integer.parseInt(tempsa[3].split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]) - 1
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
            } while (temps != null)

            val size = alvResult.size
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
            e.printStackTrace()
        }
    }


    fun loadVertexOnlyFace(fname: String) {
        val alv = ArrayList<Float>()
        val alvResult = ArrayList<Float>()
        val alnResult = ArrayList<Float>()
        try {
            val inputStream = res.assets.open(fname)
            val isr = InputStreamReader(inputStream)
            val br = BufferedReader(isr)
            var temps: String? = null

            do {
                temps = br.readLine()
                if (temps != null && temps != "") {
                    val tempsa = temps!!.split("[ ]+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    if (tempsa[0].trim { it <= ' ' } == "v") {
                        alv.add(java.lang.Float.parseFloat(tempsa[1]))
                        alv.add(java.lang.Float.parseFloat(tempsa[2]))
                        alv.add(java.lang.Float.parseFloat(tempsa[3]))
                    } else if (tempsa[0].trim { it <= ' ' } == "f") {
                        var index = Integer.parseInt(tempsa[1].split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]) - 1
                        val x0 = alv[3 * index]
                        val y0 = alv[3 * index + 1]
                        val z0 = alv[3 * index + 2]
                        alvResult.add(x0)
                        alvResult.add(y0)
                        alvResult.add(z0)

                        index = Integer.parseInt(tempsa[2].split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]) - 1
                        val x1 = alv[3 * index]
                        val y1 = alv[3 * index + 1]
                        val z1 = alv[3 * index + 2]
                        alvResult.add(x1)
                        alvResult.add(y1)
                        alvResult.add(z1)

                        index = Integer.parseInt(tempsa[3].split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]) - 1
                        val x2 = alv[3 * index]
                        val y2 = alv[3 * index + 1]
                        val z2 = alv[3 * index + 2]
                        alvResult.add(x2)
                        alvResult.add(y2)
                        alvResult.add(z2)

                        val vxa = x1 - x0
                        val vya = y1 - y0
                        val vza = z1 - z0

                        val vxb = x2 - x0
                        val vyb = y2 - y0
                        val vzb = z2 - z0

                        val vNormal = vectorNormal(
                                getCrossProduct(
                                        vxa, vya, vza, vxb, vyb, vzb
                                )
                        )
                        for (i in 0..2) {
                            alnResult.add(vNormal[0])
                            alnResult.add(vNormal[1])
                            alnResult.add(vNormal[2])
                        }
                    }
                }
            } while (temps != null)
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

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadOBJ_vertex_st_normal_noise(fname: String?) {
        val alv = ArrayList<Float>()
        val alFaceIndex = ArrayList<Int>()
        val alvResult = ArrayList<Float>()
        val hmn = HashMap<Int, HashSet<Normal>>()
        val alt = ArrayList<Float>()
        val altResult = ArrayList<Float>()
        try {
            val inputStream = res.assets.open(fname)
            val isr = InputStreamReader(inputStream)
            val br = BufferedReader(isr)
            var temp: String? = null
            while (br.readLine().also { temp = it } != null) {
                val tempsa = temp!!.split("[ ]+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (tempsa[0].trim { it <= ' ' } == "v") {
                    alv.add(tempsa[1].toFloat())
                    alv.add(tempsa[2].toFloat())
                    alv.add(tempsa[3].toFloat())
                } else if (tempsa[0].trim { it <= ' ' } == "vt") {
                    alt.add(tempsa[1].toFloat())
                    alt.add(1.0f - tempsa[2].toFloat())
                } else if (tempsa[0].trim { it <= ' ' } == "f") {
                    val index = IntArray(3)
                    index[0] = tempsa[1].split("/").toTypedArray()[0].toInt() - 1
                    val x0 = alv[3 * index[0]]
                    val y0 = alv[3 * index[0] + 1]
                    val z0 = alv[3 * index[0] + 2]
                    alvResult.add(x0)
                    alvResult.add(y0)
                    alvResult.add(z0)
                    index[1] = tempsa[2].split("/").toTypedArray()[0].toInt() - 1
                    val x1 = alv[3 * index[1]]
                    val y1 = alv[3 * index[1] + 1]
                    val z1 = alv[3 * index[1] + 2]
                    alvResult.add(x1)
                    alvResult.add(y1)
                    alvResult.add(z1)
                    index[2] = tempsa[3].split("/").toTypedArray()[0].toInt() - 1
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
                    val vNormal = getCrossProduct(
                            vxa, vya, vza, vxb, vyb, vzb
                    )
                    for (tempInxex in index) {
                        var hsn = hmn[tempInxex]
                        if (hsn == null) {
                            hsn = HashSet()
                        }
                        hsn.add(Normal(vNormal[0], vNormal[1], vNormal[2]))
                        hmn[tempInxex] = hsn
                    }
                    var indexTex = tempsa[1].split("/").toTypedArray()[1].toInt() - 1
                    altResult.add(alt[indexTex * 2])
                    altResult.add(alt[indexTex * 2 + 1])
                    indexTex = tempsa[2].split("/").toTypedArray()[1].toInt() - 1
                    altResult.add(alt[indexTex * 2])
                    altResult.add(alt[indexTex * 2 + 1])
                    indexTex = tempsa[3].split("/").toTypedArray()[1].toInt() - 1
                    altResult.add(alt[indexTex * 2])
                    altResult.add(alt[indexTex * 2 + 1])
                }
            }
            var size: Int = alvResult.size
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
            tnXYZ = FloatArray(nXYZ!!.size)
            val triCount = nXYZ!!.size / (3 * 3)
            for (i in 0 until triCount) {
                val p0x = vXYZ!![i * 9 + 0].toDouble()
                val p0y = vXYZ!![i * 9 + 1].toDouble()
                val p0z = vXYZ!![i * 9 + 2].toDouble()
                val p1x = vXYZ!![i * 9 + 3].toDouble()
                val p1y = vXYZ!![i * 9 + 4].toDouble()
                val p1z = vXYZ!![i * 9 + 5].toDouble()
                val p2x = vXYZ!![i * 9 + 6].toDouble()
                val p2y = vXYZ!![i * 9 + 7].toDouble()
                val p2z = vXYZ!![i * 9 + 8].toDouble()
                val p0s = tST!![i * 6 + 0].toDouble()
                val p0t = tST!![i * 6 + 1].toDouble()
                val p1s = tST!![i * 6 + 2].toDouble()
                val p1t = tST!![i * 6 + 3].toDouble()
                val p2s = tST!![i * 6 + 4].toDouble()
                val p2t = tST!![i * 6 + 5].toDouble()
                val tangent: DoubleArray = calQKJ(
                        p0x, p0y, p0z,
                        p1x, p1y, p1z,
                        p2x, p2y, p2z,
                        p0s, p0t,
                        p1s, p1t,
                        p2s, p2t
                )
                tnXYZ!![i * 9 + 0] = tangent[0].toFloat()
                tnXYZ!![i * 9 + 1] = tangent[1].toFloat()
                tnXYZ!![i * 9 + 2] = tangent[2].toFloat()
                tnXYZ!![i * 9 + 3] = tangent[0].toFloat()
                tnXYZ!![i * 9 + 4] = tangent[1].toFloat()
                tnXYZ!![i * 9 + 5] = tangent[2].toFloat()
                tnXYZ!![i * 9 + 6] = tangent[0].toFloat()
                tnXYZ!![i * 9 + 7] = tangent[1].toFloat()
                tnXYZ!![i * 9 + 8] = tangent[2].toFloat()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun calQKJ(p0x: Double, p0y: Double, p0z: Double,
                       p1x: Double, p1y: Double, p1z: Double,
                       p2x: Double, p2y: Double, p2z: Double,
                       p0s: Double, p0t: Double,
                       p1s: Double, p1t: Double,
                       p2s: Double, p2t: Double): DoubleArray {
        var a0 = p1s - p0s
        var b0 = p1t - p0t
        var c0 = p0x - p1x
        var a1 = p2s - p0s
        var b1 = p2t - p0t
        var c1 = p0x - p2x
        val TBX: DoubleArray = solveEquation(a0, b0, c0, a1, b1, c1)
        a0 = p1s - p0s
        b0 = p1t - p0t
        c0 = p0y - p1y
        a1 = p2s - p0s
        b1 = p2t - p0t
        c1 = p0y - p2y
        val TBY: DoubleArray = solveEquation(a0, b0, c0, a1, b1, c1)
        a0 = p1s - p0s
        b0 = p1t - p0t
        c0 = p0z - p1z
        a1 = p2s - p0s
        b1 = p2t - p0t
        c1 = p0z - p2z
        val TBZ: DoubleArray = solveEquation(a0, b0, c0, a1, b1, c1)
        return doubleArrayOf(TBX[0], TBY[0], TBZ[0])
    }

    private fun solveEquation(a0: Double, b0: Double, c0: Double,
                              a1: Double, b1: Double, c1: Double): DoubleArray {
        val x = (c1 * b0 - c0 * b1) / (a0 * b1 - a1 * b0)
        val y = (-a0 * x - c0) / b0
        return doubleArrayOf(x, y)
    }
}