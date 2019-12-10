package com.ja3son.gllib.demo.basic_physics.aabb

import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState

class RigidBody() {
    private lateinit var renderObject: BaseEntity
    private lateinit var collObject: AABBEntity
    private var isStatic = false
    private lateinit var currLocation: Vector3f
    private lateinit var currV: Vector3f
    private val V_UNIT = 0.02f

    constructor(renderObject: BaseEntity, isStatic: Boolean, currLocation: Vector3f, currV: Vector3f) : this() {
        this.renderObject = renderObject
        collObject = AABBEntity(renderObject.vertices_data)
        this.isStatic = isStatic
        this.currLocation = currLocation
        this.currV = currV
    }

    fun drawSelf() {
        MatrixState.pushMatrix()
        MatrixState.translate(currLocation.x, currLocation.y, currLocation.z)
        renderObject.drawSelf()
        MatrixState.popMatrix()
    }

    fun go(al: ArrayList<RigidBody>) {
        if (isStatic) return
        currLocation.add(currV)
        for (i in 0 until al.size) {
            val rb = al[i]
            if (rb != this) {
                if (check(this, rb)) {
                    this.currV.x = -this.currV.x
                }
            }
        }
    }

    private fun check(ra: RigidBody, rb: RigidBody): Boolean {
        val over = calOverTotal(
                ra.collObject.getCurrAABBBox(ra.currLocation),
                rb.collObject.getCurrAABBBox(rb.currLocation)
        )
        return over[0] > V_UNIT && over[1] > V_UNIT && over[2] > V_UNIT
    }

    private fun calOverTotal(a: AABBEntity, b: AABBEntity): FloatArray {
        val xOver = calOverOne(a.maxX, a.minX, b.maxX, b.minX)
        val yOver = calOverOne(a.maxY, a.minY, b.maxY, b.minY)
        val zOver = calOverOne(a.maxZ, a.minZ, b.maxZ, b.minZ)
        return floatArrayOf(xOver, yOver, zOver)
    }

    private fun calOverOne(amax: Float, amin: Float, bmax: Float, bmin: Float): Float {
        val minMax: Float
        val maxMin: Float
        if (amax < bmax) {
            minMax = amax
            maxMin = bmin
        } else {
            minMax = bmax
            maxMin = amin
        }
        return if (minMax > maxMin) {
            minMax - maxMin
        } else {
            0f
        }
    }
}