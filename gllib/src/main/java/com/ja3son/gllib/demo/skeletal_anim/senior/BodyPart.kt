package com.ja3son.gllib.demo.skeletal_anim.senior

import android.opengl.Matrix
import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState

internal class BodyPart {
    var lovnt: BaseEntity?
    //部件索引
    var index: Int
    //此骨骼在父骨骼坐标系中的实时变换矩阵
    var mFather = FloatArray(16)
    //此骨骼在世界坐标系中的实时变换矩阵
    var mWorld = FloatArray(16)
    //此骨骼在父骨骼坐标系中的初始变换矩阵
    var mFatherInit = FloatArray(16)
    //此骨骼初始情况下在世界坐标系中的变换矩阵的逆矩阵
    var mWorldInitInver = FloatArray(16)
    //最终变换矩阵
    var finalMatrix = FloatArray(16)
    //此骨骼不动点在世界坐标系中的原始坐标
    var fx: Float
    var fy: Float
    var fz: Float
    //此骨骼自己的子骨骼列表
    var childs = ArrayList<BodyPart>()
    //指向父骨骼的引用
    var father: BodyPart? = null
    //是否有最低控制点
    var lowestFlag = false
    //最低控制点序列
    lateinit var lowestDots: Array<FloatArray>
    var robot: Robot

    constructor(fx: Float, fy: Float, fz: Float, lovnt: BaseEntity?, index: Int, robot: Robot) {
        this.index = index
        this.lovnt = lovnt
        this.fx = fx
        this.fy = fy
        this.fz = fz
        this.robot = robot
    }

    constructor(fx: Float, fy: Float, fz: Float, lovnt: BaseEntity?, index: Int,
                lowestFlag: Boolean, lowestDots: Array<FloatArray>, robot: Robot) {
        this.index = index
        this.lovnt = lovnt
        this.fx = fx
        this.fy = fy
        this.fz = fz
        this.lowestFlag = lowestFlag
        this.lowestDots = lowestDots
        this.robot = robot
    }

    //级联计算最低控制点
    fun calLowest() { //先计算自己控制点的最低点
        if (lowestFlag) {
            for (p in lowestDots) { //计算变化后的坐标
                val pqc = floatArrayOf(p[0], p[1], p[2], 1f)
                val resultP = floatArrayOf(0f, 0f, 0f, 1f)
                Matrix.multiplyMV(resultP, 0, finalMatrix, 0, pqc, 0)
                if (resultP[1] < robot.lowest) {
                    robot.lowest = resultP[1]
                }
            }
        }
        //计算所有的孩子
        for (bp in childs) {
            bp.calLowest()
        }
    }

    //将最终矩阵内容拷贝进绘制用的最终矩阵
    fun copyMatrixForDraw() {
        for (i in 0..15) {
            robot.fianlMatrixForDrawArray[index][i] = finalMatrix[i]
        }
    }

    fun drawSelf(tempMatrixArray: Array<FloatArray?>) { //如果自己的绘制者不为空，则绘制自己
        if (lovnt != null) {
            MatrixState.pushMatrix()
            //插入新矩阵
            MatrixState.setMatrix(tempMatrixArray[index])
            lovnt!!.drawSelf()
            MatrixState.popMatrix()
        }
        //绘制所有的孩子
        for (bp in childs) {
            bp.drawSelf(tempMatrixArray)
        }
    }

    //级联计算每个子骨骼在父骨骼坐标系中的原始坐标
    fun initFatherMatrix() {
        var tx = fx
        var ty = fy
        var tz = fz
        if (father != null) { //若父骨骼不为空，则子骨骼在父骨骼坐标系中的原始坐标
//为子骨骼在世界坐标系中的原始坐标减去父骨骼在在世界
//坐标系中的原始坐标
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
        //然后更新自己的所有孩子
        for (bc in childs) {
            bc.initFatherMatrix()
        }
    }

    //层次级联计算子骨骼初始情况下在世界坐标系中的变换矩阵的逆矩阵
    fun calMWorldInitInver() {
        Matrix.invertM(mWorldInitInver, 0, mWorld, 0)
        //然后更新自己的所有孩子
        for (bc in childs) {
            bc.calMWorldInitInver()
        }
    }

    fun updateBone() { //层次级联更新骨骼矩阵的方法
//首先根据父矩阵更新自己在世界坐标系中的变换矩阵
        if (father != null) { //若父骨骼不为空则此骨骼在世界坐标系中的变换矩阵
//为父骨骼在世界坐标系中的变换矩阵乘以自己在父骨骼
//坐标系中的变换矩阵
            Matrix.multiplyMM(mWorld, 0, father!!.mWorld, 0, mFather, 0)
        } else { //若父骨骼为空，则此骨骼在世界坐标系中的变换矩阵
//为自己在父骨骼坐标系中的变换矩阵
            for (i in 0..15) {
                mWorld[i] = mFather[i]
            }
        }
        calFinalMatrix()
        //然后更新自己的所有孩子
        for (bc in childs) {
            bc.updateBone()
        }
    }

    //获取最终的绑定到此骨骼的顶点受此骨骼影响的从Mesh坐标系到骨骼动画后位置的最终变换矩阵
    fun calFinalMatrix() {
        Matrix.multiplyMM(finalMatrix, 0, mWorld, 0, mWorldInitInver, 0)
    }

    //清除状态
    fun backToIInit() {
        for (i in 0..15) {
            mFather[i] = mFatherInit[i]
        }
        //然后更新自己的所有孩子
        for (bc in childs) {
            bc.backToIInit()
        }
    }

    //在父坐标系中平移自己
    fun transtate(x: Float, y: Float, z: Float) //设置沿xyz轴移动
    {
        Matrix.translateM(mFather, 0, x, y, z)
    }

    //在父坐标系中旋转自己
    fun rotate(angle: Float, x: Float, y: Float, z: Float) //设置绕xyz轴转动
    {
        Matrix.rotateM(mFather, 0, angle, x, y, z)
    }

    //添加子骨骼
    fun addChild(child: BodyPart) {
        childs.add(child)
        child.father = this
    }
}