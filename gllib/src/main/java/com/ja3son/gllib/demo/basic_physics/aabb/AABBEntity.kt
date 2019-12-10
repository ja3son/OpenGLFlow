package com.ja3son.gllib.demo.basic_physics.aabb

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
}