package com.ja3son.gllib.util

import android.content.Context
import android.content.res.Resources
import com.ja3son.gllib.util.ShaderUtils.Normal
import java.io.BufferedReader
import java.io.InputStreamReader


object OBJUtils {
    private lateinit var res: Resources
    lateinit var vXYZ: FloatArray
    lateinit var nXYZ: FloatArray

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
                vXYZ[i] = alvResult[i]
            }

            nXYZ = FloatArray(alFaceIndex.size * 3)
            var c = 0
            for (i in alFaceIndex) {
                val hsn = hmn[i]
                val tn = Normal.getAverage(hsn!!)
                nXYZ[c++] = tn[0]
                nXYZ[c++] = tn[1]
                nXYZ[c++] = tn[2]
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
                vXYZ[i] = alvResult[i]
            }

            size = alnResult.size
            nXYZ = FloatArray(size)
            for (i in 0 until size) {
                nXYZ[i] = alnResult[i]
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}