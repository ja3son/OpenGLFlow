package com.ja3son.gllib.demo.light.reflection_pro

object ReflectionProConstants {
    var WIDTH_SPAN = 2f * 63 //横向长度总跨度
    var threadFlag = true //水面换帧线程工作标志位
    var r = 60f //摄像机到目标点的距离，即摄像机旋转的半径
    var XANGLE_MIN = -55f //摄像机左右旋转的角度范围的最小值
    var XANGLE_MAX = 55f //摄像机左右旋转的角度范围的最大值
    var YANGLE_MIN = 15f //摄像机上下旋转的角度范围的最小值
    var YANGLE_MAX = 90f //摄像机上下旋转的角度范围的最大值
    const val UNIT_SIZE = 0.6f
    var SCREEN_WIDTH //屏幕宽度
            = 0
    var SCREEN_HEIGHT //屏幕高度
            = 0
    //设置投影矩阵的参数
    var left = 0f
    var right = 0f
    var bottom = 0f
    var top = 0f
    var near = 0f
    var far = 0f
    var ratio = 0f
    //注意start======主摄像与镜像摄像机的目标点和up向量是一致的
//摄像机目标点的坐标
    var targetX = 0f
    var targetY = 0f
    var targetZ = 0f
    //摄像机的up向量
    var upX = 0f
    var upY = 1f
    var upZ = 0f
    //注意end======主摄像与镜像摄像机的目标点和up向量是一致的
//主摄像机的观察者坐标
    var mainCameraX = 0f
    var mainCameraY = 0f
    var mainCameraZ = 0f
    //镜像摄像机的观察者坐标
    var mirrorCameraX = 0f
    var mirrorCameraY = 0f
    var mirrorCameraZ = 0f
    fun calculateMainAndMirrorCamera(xAngle: Float, yAngle: Float) {
        mainCameraX = (r * Math.cos(Math.toRadians(yAngle.toDouble())) * Math.sin(Math.toRadians(xAngle.toDouble()))).toFloat()
        mainCameraY = (r * Math.sin(Math.toRadians(yAngle.toDouble()))).toFloat()
        mainCameraZ = (r * Math.cos(Math.toRadians(yAngle.toDouble())) * Math.cos(Math.toRadians(xAngle.toDouble()))).toFloat()
        //计算镜像摄像机观察者的坐标
        mirrorCameraX = mainCameraX //镜像摄像机的x坐标与主摄像的z坐标一致
        mirrorCameraY = 2 * targetY - mainCameraY
        mirrorCameraZ = mainCameraZ //根据对称关系计算镜像摄像机的z坐标
    }

    //初始化投影矩阵的参数
    fun initProject(factor: Float) {
        left = -ratio * factor * 0.5f
        right = ratio * factor * 0.5f
        bottom = -1 * factor * 0.5f
        top = 1 * factor * 0.5f
        near = 1 * factor
        far = 500f
    }

    var waveFrequency1 = 0.19f //1号波波频
    var waveFrequency2 = 0.09f //2号波波频
    var waveFrequency3 = 0.01f //3号波波频
    var waveAmplitude1 = 0.126f //1号波振幅
    var waveAmplitude2 = 0.21f //2号波振幅
    var waveAmplitude3 = 0.35f //3号波振幅
    var wave1PositionX = 0f //1号波的位置
    var wave1PositionY = 0f
    var wave1PositionZ = 0f
    var wave2PositionX = -200f //2号波的位置
    var wave2PositionY = 0f
    var wave2PositionZ = -200f
    var wave3PositionX = 300f //3号波的位置
    var wave3PositionY = 0f
    var wave3PositionZ = 300f
    var lock = Any() //锁资源
}