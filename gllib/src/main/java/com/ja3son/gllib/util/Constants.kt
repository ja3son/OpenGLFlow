package com.ja3son.gllib.util

object Constants {

    var curDrawState: Constants.DrawState = Constants.DrawState.GL_POINTS

    enum class DrawState {
        GL_POINTS,
        GL_LINES,
        GL_LINE_STRIP,
        GL_LINE_LOOP
    }
}
