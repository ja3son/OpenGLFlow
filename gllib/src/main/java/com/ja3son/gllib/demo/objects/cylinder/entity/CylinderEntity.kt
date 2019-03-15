package com.ja3son.gllib.demo.objects.cylinder.entity

import com.ja3son.gllib.entity.BaseEntity
import com.ja3son.gllib.util.MatrixState

class CylinderEntity(val r: Float, val hight: Float, val scale: Float, val count: Int, val topTexId: Int, val bottomTexId: Int, val sideTexID: Int) : BaseEntity() {
    lateinit var topCircle: CylinderCircleEntity
    lateinit var bottomCircle: CylinderCircleEntity
    lateinit var cylinderSide: CylinderSideEntity

    init {
        init()
    }

    override fun initVertexData() {
        topCircle = CylinderCircleEntity(r, scale, count, topTexId)
        bottomCircle = CylinderCircleEntity(r, scale, count, bottomTexId)
        cylinderSide = CylinderSideEntity(r, hight, scale, count, sideTexID)
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