package com.ja3son.cgldemo;

import android.content.Context;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class GL2JNIView extends GLSurfaceView {
    Renderer renderer;

    public GL2JNIView(Context context) {
        super(context);
        this.setEGLContextClientVersion(3);
        renderer = new Renderer();
        this.setRenderer(renderer);
        this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    private static class Renderer implements GLSurfaceView.Renderer {
        public void onDrawFrame(GL10 gl) {
            GL2JNILib.step();
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GL2JNILib.init(width, height);
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        }
    }
}
