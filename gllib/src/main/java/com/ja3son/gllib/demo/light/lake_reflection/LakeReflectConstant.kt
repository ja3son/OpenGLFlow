package com.ja3son.gllib.demo.light.lake_reflection

object LakeReflectConstant {

    var WIDTH_SPAN = 2f * 63

    var threadFlag = true

    var r = 60f


    var XANGLE_MIN = -55f

    var XANGLE_MAX = 55f

    var YANGLE_MIN = 15f

    var YANGLE_MAX = 90f


    const val UNIT_SIZE = 0.6f
    var SCREEN_WIDTH = 0
    var SCREEN_HEIGHT = 0


    var left = 0f
    var right = 0f
    var bottom = 0f
    var top = 0f
    var near = 0f
    var far = 0f
    var ratio = 0f


    var targetX = 0f
    var targetY = 0f
    var targetZ = 0f


    var upX = 0f
    var upY = 1f
    var upZ = 0f


    var mainCameraX = 0f
    var mainCameraY = 0f
    var mainCameraZ = 0f


    var mirrorCameraX = 0f
    var mirrorCameraY = 0f
    var mirrorCameraZ = 0f

    fun calculateMainAndMirrorCamera(xAngle: Float, yAngle: Float) {
        mainCameraX = ((r * Math.cos(Math.toRadians(yAngle.toDouble())) * Math.sin(Math.toRadians(xAngle.toDouble()))).toFloat())
        mainCameraY = ((r * Math.sin(Math.toRadians(yAngle.toDouble()))).toFloat())
        mainCameraZ = (r * Math.cos(Math.toRadians(yAngle.toDouble())) * Math.cos(Math.toRadians(xAngle.toDouble()))).toFloat()

        //计算镜像摄像机观察者的坐标
        mirrorCameraX = mainCameraX;//镜像摄像机的x坐标与主摄像的z坐标一致
        mirrorCameraY = 2 * targetY - mainCameraY;
        mirrorCameraZ = mainCameraZ;//根据对称关系计算镜像摄像机的z坐标
    }

    fun initProject(factor: Float) {
        left = -ratio * factor * 0.5f
        right = ratio * factor * 0.5f
        bottom = -1 * factor * 0.5f
        top = 1 * factor * 0.5f
        near = 1 * factor
        far = 500f
    }

    var waveFrequency1 = 0.19f

    var waveFrequency2 = 0.09f

    var waveFrequency3 = 0.01f


    var waveAmplitude1 = 0.126f

    var waveAmplitude2 = 0.21f

    var waveAmplitude3 = 0.35f


    var wave1PositionX = 0f

    var wave1PositionY = 0f
    var wave1PositionZ = 0f

    var wave2PositionX = -200f

    var wave2PositionY = 0f
    var wave2PositionZ = -200f

    var wave3PositionX = 300f

    var wave3PositionY = 0f
    var wave3PositionZ = 300f


    var lock = Any()

}