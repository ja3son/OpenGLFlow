package com.ja3son.gllib.demo.objects.cylinder.entity

import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState

class CylinderLEntity(val r: Float, val hight: Float, val scale: Float, val count: Int) : BaseEntity() {
    lateinit var topCircle: CylinderCircleLEntity
    lateinit var bottomCircle: CylinderCircleLEntity
    lateinit var cylinderSide: CylinderSideLEntity

    init {
        init()
    }

    override fun initVertexData() {
        topCircle = CylinderCircleLEntity(r, scale, count)
        bottomCircle = CylinderCircleLEntity(r, scale, count)
        cylinderSide = CylinderSideLEntity(r, hight, scale, count)
    }

    override fun initShader() {
    }

    override fun initShaderParams() {
    }

    override fun drawSelf() {
        MatrixState.rotate(xAngle, 1f, 0f, 0f)
        MatrixState.rotate(yAngle, 0f, 1f, 0f)

        MatrixState.pushMatrix()
        MatrixState.translate(0f, hight / 2 * scale, 0f)
        MatrixState.rotate(-90.0f, 1f, 0f, 0f)
        topCircle.drawSelf()
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(0f, -hight / 2 * scale, 0f)
        MatrixState.rotate(90f, 1f, 0f, 0f)
        MatrixState.rotate(180f, 0f, 0f, 1f)
        bottomCircle.drawSelf()
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(0f, -hight / 2 * scale, 0f)
        cylinderSide.drawSelf()
        MatrixState.popMatrix()
    }
}