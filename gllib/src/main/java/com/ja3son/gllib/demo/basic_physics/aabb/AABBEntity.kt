package com.ja3son.gllib.demo.basic_physics.aabb

import android.opengl.Matrix


class AABBEntity() {
    var minX = 0f
    var maxX = 0f
    var minY = 0f
    var maxY = 0f
    var minZ = 0f
    var maxZ = 0f

    constructor(vertices: FloatArray) : this() {
        init()
        findMinAndMax(vertices)
    }

    constructor(minX: Float, maxX: Float, minY: Float, maxY: Float, minZ: Float, maxZ: Float) : this() {
        this.minX = minX
        this.maxX = maxX
        this.minY = minY
        this.maxY = maxY
        this.minZ = minZ
        this.maxZ = maxZ
    }

    fun init() {
        minX = Float.POSITIVE_INFINITY
        maxX = Float.NEGATIVE_INFINITY
        minY = Float.POSITIVE_INFINITY
        maxY = Float.NEGATIVE_INFINITY
        minZ = Float.POSITIVE_INFINITY
        maxZ = Float.NEGATIVE_INFINITY
    }


    fun init(data: FloatArray) {
        data[0] = Float.POSITIVE_INFINITY
        data[1] = Float.NEGATIVE_INFINITY
        data[2] = Float.POSITIVE_INFINITY
        data[3] = Float.NEGATIVE_INFINITY
        data[4] = Float.POSITIVE_INFINITY
        data[5] = Float.NEGATIVE_INFINITY
    }

    private fun findMinAndMax(vertices: FloatArray) {
        for (i in 0 until vertices.size / 3) {
            if (vertices[i * 3] < minX) {
                minX = vertices[i * 3]
            }
            if (vertices[i * 3] > maxX) {
                maxX = vertices[i * 3]
            }
            if (vertices[i * 3 + 1] < minY) {
                minY = vertices[i * 3 + 1]
            }
            if (vertices[i * 3 + 1] > maxY) {
                maxY = vertices[i * 3 + 1]
            }
            if (vertices[i * 3 + 2] < minZ) {
                minZ = vertices[i * 3 + 2]
            }
            if (vertices[i * 3 + 2] > maxZ) {
                maxZ = vertices[i * 3 + 2]
            }
        }
    }

    fun getCurrAABBBox(currPosition: Vector3f): AABBEntity {
        return AABBEntity(
                minX + currPosition.x,
                maxX + currPosition.x,
                minY + currPosition.y,
                maxY + currPosition.y,
                minZ + currPosition.z,
                maxZ + currPosition.z
        )
    }

    fun findMinAndMaxRotate(vertices: FloatArray): FloatArray {
        val result = FloatArray(6)
        init(result)
        for (i in 0 until vertices.size / 3) {
            if (vertices[i * 3] < result[0]) {
                result[0] = vertices[i * 3]
            }
            if (vertices[i * 3] > result[1]) {
                result[1] = vertices[i * 3]
            }
            if (vertices[i * 3 + 1] < result[2]) {
                result[2] = vertices[i * 3 + 1]
            }
            if (vertices[i * 3 + 1] > result[3]) {
                result[3] = vertices[i * 3 + 1]
            }
            if (vertices[i * 3 + 2] < result[4]) {
                result[4] = vertices[i * 3 + 2]
            }
            if (vertices[i * 3 + 2] > result[5]) {
                result[5] = vertices[i * 3 + 2]
            }
        }
        return result
    }

    fun getCurrAABBBox(currPosition: Vector3f, currOrientation: Orientation): AABBEntity {
        val va = arrayOf(
                Vector3f(minX, minY, minZ),
                Vector3f(minX, maxY, minZ),
                Vector3f(maxX, minY, minZ),
                Vector3f(maxX, maxY, minZ),
                Vector3f(minX, minY, maxZ),
                Vector3f(minX, maxY, maxZ),
                Vector3f(maxX, minY, maxZ),
                Vector3f(maxX, maxY, maxZ))
        val boxVertices = FloatArray(24)
        var count = 0
        for (i in va.indices) {
            val result = FloatArray(4)
            val dot = floatArrayOf(va[i].x, va[i].y, va[i].z, 1f)
            Matrix.multiplyMV(result, 0, currOrientation.orientationData, 0, dot, 0)
            boxVertices[count++] = result[0]
            boxVertices[count++] = result[1]
            boxVertices[count++] = result[2]
        }
        val data: FloatArray = findMinAndMaxRotate(boxVertices)
        return AABBEntity(
                data[0] + currPosition.x,
                data[1] + currPosition.x,
                data[2] + currPosition.y,
                data[3] + currPosition.y,
                data[4] + currPosition.z,
                data[5] + currPosition.z
        )
    }
}