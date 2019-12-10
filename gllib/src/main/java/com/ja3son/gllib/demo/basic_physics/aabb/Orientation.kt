package com.ja3son.gllib.demo.basic_physics.aabb

import android.opengl.Matrix

class Orientation(angle: Float, zx: Float, zy: Float, zz: Float) {
    var orientationData = FloatArray(16)

    init {
        Matrix.setRotateM(orientationData, 0, angle, zx, zy, zz)
    }
}
