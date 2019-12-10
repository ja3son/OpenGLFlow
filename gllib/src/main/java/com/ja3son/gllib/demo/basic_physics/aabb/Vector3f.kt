package com.ja3son.gllib.demo.basic_physics.aabb

class Vector3f(var x: Float, var y: Float, var z: Float) {

    fun add(temp: Vector3f) {
        x += temp.x
        y += temp.y
        z += temp.z
    }
}