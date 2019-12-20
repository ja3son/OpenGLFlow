package com.ja3son.gllib.demo.skeletal_anim.basic

import android.opengl.Matrix
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState


internal class BodyPart(val fx: Float, val fy: Float, val fz: Float,
                        val lovnt: BaseEntity?, var index: Int, val robot: Robot) {
    var mFather = FloatArray(16)
    var mWorld = FloatArray(16)
    var mFatherInit = FloatArray(16)
    var mWorldInitInver = FloatArray(16)
    var finalMatrix = FloatArray(16)
    var childs = ArrayList<BodyPart>()
    var father: BodyPart? = null

    fun copyMatrixForDraw() {
        for (i in 0..15) {
            robot.fianlMatrixForDrawArray[index][i] = finalMatrix[i]
        }
    }

    fun drawSelf(tempMatrixArray: Array<FloatArray?>) {
        MatrixState.pushMatrix()
        MatrixState.setMatrix(tempMatrixArray[index])
        lovnt?.drawSelf()
        MatrixState.popMatrix()
        for (bp in childs) {
            bp.drawSelf(tempMatrixArray)
        }
    }

    //级联计算每个子骨骼在父骨骼坐标系中的原始坐标
    fun initFatherMatrix() {
        var tx = fx
        var ty = fy
        var tz = fz
        //若父骨骼不为空，则子骨骼在父骨骼坐标系中的原始坐标
        if (father != null) {
            tx = fx - father!!.fx
            ty = fy - father!!.fy
            tz = fz - father!!.fz
        }
        //生成初始的此骨骼在父骨骼坐标系中的初始变换矩阵
        Matrix.setIdentityM(mFather, 0)
        Matrix.translateM(mFather, 0, tx, ty, tz)
        for (i in 0..15) {
            mFatherInit[i] = mFather[i]
        }
        for (bc in childs) { //然后更新自己的所有孩子
            bc.initFatherMatrix()
        }
    }

    //层次级联计算子骨骼初始情况下在世界坐标系中的变换矩阵的逆矩阵
    fun calMWorldInitInver() {
        Matrix.invertM(mWorldInitInver, 0, mWorld, 0)
        for (bc in childs) { //然后更新自己的所有孩子
            bc.calMWorldInitInver()
        }
    }

    fun updateBone() {
        //层次级联更新骨骼矩阵的方法
        //首先根据父矩阵更新自己在世界坐标系中的变换矩阵
        //若父骨骼不为空则此骨骼在世界坐标系中的变换矩阵
        if (father != null) {
            Matrix.multiplyMM(mWorld, 0, father!!.mWorld, 0, mFather, 0)
        } else { //若父骨骼为空，则此骨骼在世界坐标系中的变换矩阵为自己在父骨骼坐标系中的变换矩阵
            for (i in 0..15) {
                mWorld[i] = mFather[i]
            }
        }
        calFinalMatrix()
        for (bc in childs) { //然后更新自己的所有孩子
            bc.updateBone()
        }
    }

    //获取最终的绑定到此骨骼的顶点受此骨骼影响的从Mesh坐标系到骨骼动画后位置的最终变换矩阵
    fun calFinalMatrix() {
        Matrix.multiplyMM(finalMatrix, 0, mWorld, 0, mWorldInitInver, 0)
    }

    fun backToIInit() { //清除状态
        for (i in 0..15) {
            mFather[i] = mFatherInit[i]
        }
        for (bc in childs) { //然后更新自己的所有孩子
            bc.backToIInit()
        }
    }

    fun transtate(x: Float, y: Float, z: Float) { //在父坐标系中平移自己
        Matrix.translateM(mFather, 0, x, y, z)
    }

    fun rotate(angle: Float, x: Float, y: Float, z: Float) { //在父坐标系中旋转自己
        Matrix.rotateM(mFather, 0, angle, x, y, z)
    }

    fun addChild(child: BodyPart) { //添加子骨骼
        childs.add(child)
        child.father = this
    }
}