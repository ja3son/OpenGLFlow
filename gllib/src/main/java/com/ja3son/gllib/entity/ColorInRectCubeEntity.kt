package com.ja3son.gllib.entity

import com.ja3son.gllib.util.MatrixState

class ColorInRectCubeEntity(unitSize: Float, color: FloatArray) : BaseEntity() {

    var rect: ColorInRectEntity = ColorInRectEntity(unitSize, color)
    var IN_UNIT_SIZE = unitSize

    init {
        init()
    }

    override fun initVertexData() {
    }

    override fun initShader() {
    }

    override fun initShaderParams() {
    }

    override fun drawSelf() {
        MatrixState.pushMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(0f, 0f, IN_UNIT_SIZE)
        rect.drawSelf()
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(0f, 0f, -IN_UNIT_SIZE)
        MatrixState.rotate(180f, 0f, 1f, 0f)
        rect.drawSelf()
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(0f, IN_UNIT_SIZE, 0f)
        MatrixState.rotate(-90f, 1f, 0f, 0f)
        rect.drawSelf()
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(0f, -IN_UNIT_SIZE, 0f)
        MatrixState.rotate(90f, 1f, 0f, 0f)
        rect.drawSelf()
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(IN_UNIT_SIZE, 0f, 0f)
        MatrixState.rotate(-90f, 1f, 0f, 0f)
        MatrixState.rotate(90f, 0f, 1f, 0f)
        rect.drawSelf()
        MatrixState.popMatrix()

        MatrixState.pushMatrix()
        MatrixState.translate(-IN_UNIT_SIZE, 0f, 0f)
        MatrixState.rotate(90f, 1f, 0f, 0f)
        MatrixState.rotate(-90f, 0f, 1f, 0f)
        rect.drawSelf()
        MatrixState.popMatrix()

        MatrixState.popMatrix()
    }
}