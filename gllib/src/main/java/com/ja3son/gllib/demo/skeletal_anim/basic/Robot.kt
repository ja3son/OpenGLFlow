package com.ja3son.gllib.demo.skeletal_anim.basic

import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState

internal class Robot(lovntArray: Array<BaseEntity>) {
    var bRoot: BodyPart
    var bBody: BodyPart
    var bHead: BodyPart
    var bLeftTop: BodyPart
    var bLeftBottom: BodyPart
    var bRightTop: BodyPart
    var bRightBottom: BodyPart
    var bRightLegTop: BodyPart
    var bRightLegBottom: BodyPart
    var bLeftLegTop: BodyPart
    var bLeftLegBottom: BodyPart
    var bLeftFoot: BodyPart
    var bRightFoot: BodyPart
    var bpArray: Array<BodyPart>
    //用于绘制的最终矩阵数组
    var fianlMatrixForDrawArray: Array<FloatArray>
    //用于绘制的临时矩阵数组
    var fianlMatrixForDrawArrayTemp: Array<FloatArray?>
    var lock = Any() //绘制数据锁

    init {
        bRoot = BodyPart(0f, 0f, 0f, null, 0, this)
        bBody = BodyPart(0.0f, 0.938f, 0.0f, lovntArray[0], 1, this)
        bHead = BodyPart(0.0f, 1.00f, 0.0f, lovntArray[1], 2, this)
        bLeftTop = BodyPart(0.107f, 0.938f, 0.0f, lovntArray[2], 3, this)
        bLeftBottom = BodyPart(0.105f, 0.707f, -0.033f, lovntArray[3], 4, this)
        bRightTop = BodyPart(-0.107f, 0.938f, 0.0f, lovntArray[4], 5, this)
        bRightBottom = BodyPart(-0.105f, 0.707f, -0.033f, lovntArray[5], 6, this)
        bRightLegTop = BodyPart(-0.068f, 0.6f, 0.02f, lovntArray[6], 7, this)
        bRightLegBottom = BodyPart(-0.056f, 0.312f, 0f, lovntArray[7], 8, this)
        bLeftLegTop = BodyPart(0.068f, 0.6f, 0.02f, lovntArray[8], 9, this)
        bLeftLegBottom = BodyPart(0.056f, 0.312f, 0f, lovntArray[9], 10, this)
        bLeftFoot = BodyPart(0.068f, 0.038f, 0.033f, lovntArray[10], 11, this)
        bRightFoot = BodyPart(-0.068f, 0.038f, 0.033f, lovntArray[11], 12, this)
        //所有的骨骼列表
        bpArray = arrayOf(bRoot, bBody, bHead, bLeftTop, bLeftBottom, bRightTop, bRightBottom,
                bRightLegTop, bRightLegBottom, bLeftLegTop, bLeftLegBottom, bLeftFoot, bRightFoot
        )
        //每个骨骼一个矩阵
        fianlMatrixForDrawArray = Array(bpArray.size) { FloatArray(16) }
        fianlMatrixForDrawArrayTemp = Array(bpArray.size) { FloatArray(16) }
        bRoot.addChild(bBody)
        bBody.addChild(bHead)
        bBody.addChild(bLeftTop)
        bBody.addChild(bRightTop)
        bLeftTop.addChild(bLeftBottom)
        bRightTop.addChild(bRightBottom)
        bBody.addChild(bRightLegTop)
        bRightLegTop.addChild(bRightLegBottom)
        bBody.addChild(bLeftLegTop)
        bLeftLegTop.addChild(bLeftLegBottom)
        bLeftLegBottom.addChild(bLeftFoot)
        bRightLegBottom.addChild(bRightFoot)
        //级联计算每个子骨骼在父骨骼坐标系中的原始坐标，并且将平移信息记录进矩阵
        bRoot.initFatherMatrix()
        //层次级联更新骨骼矩阵的方法真实的平移信息，相对于世界坐标系
        bRoot.updateBone()
        //层次级联计算子骨骼初始情况下在世界坐标系中的变换矩阵的逆矩阵
        bRoot.calMWorldInitInver()
    }

    fun updateState() { //在线程中调用此方法
        bRoot.updateBone()
    }

    fun backToInit() { //在线程中调用此方法
        bRoot.backToIInit()
    }

    fun flushDrawData() { //在线程中调用此方法
        synchronized(lock) {
            //加锁将主数据拷贝进绘制数据
            for (bp in bpArray) {
                bp.copyMatrixForDraw()
            }
        }
    }

    fun drawSelf() {
        synchronized(lock) {
            //绘制前将绘制数据拷贝进临时数据
            for (i in bpArray.indices) {
                for (j in 0..15) {
                    fianlMatrixForDrawArrayTemp[i]!![j] = fianlMatrixForDrawArray[i][j]
                }
            }
        }
        MatrixState.pushMatrix()
        //从根部件开始绘制
        bRoot.drawSelf(fianlMatrixForDrawArrayTemp)
        MatrixState.popMatrix()
    }
}