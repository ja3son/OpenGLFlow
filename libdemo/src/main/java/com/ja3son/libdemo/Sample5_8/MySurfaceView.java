package com.ja3son.libdemo.Sample5_8;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static com.ja3son.libdemo.Sample5_8.Constant.CAM_X;
import static com.ja3son.libdemo.Sample5_8.Constant.CAM_Y;
import static com.ja3son.libdemo.Sample5_8.Constant.CAM_Z;
import static com.ja3son.libdemo.Sample5_8.Constant.H;
import static com.ja3son.libdemo.Sample5_8.Constant.LIGHT_X;
import static com.ja3son.libdemo.Sample5_8.Constant.LIGHT_Y;
import static com.ja3son.libdemo.Sample5_8.Constant.LIGHT_Z;
import static com.ja3son.libdemo.Sample5_8.Constant.W;
import static com.ja3son.libdemo.Sample5_8.Constant.nCols;
import static com.ja3son.libdemo.Sample5_8.Constant.nRows;

@SuppressLint("NewApi")
class MySurfaceView extends GLSurfaceView {
    private SceneRenderer mRenderer;

    public MySurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(2);
        mRenderer = new SceneRenderer();
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    private class SceneRenderer implements Renderer {
        Camera cam;
        Scene scn;
        Light light;
        ColorRect rect;

        public void onDrawFrame(GL10 gl) {
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            MatrixState.setProjectOrtho(-W, W, -H, H, 1, 2);
            MatrixState.setCamera(0, 0, 1, 0, 0, 0, 0, 1, 0);
            cam.setMyCamera(CAM_X, CAM_Y, CAM_Z, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
            scn.transform();
            cam.raytrace(scn, rect);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES30.glViewport(0, 0, (int) nCols, (int) nRows);
            MatrixState.setProjectOrtho(-W, W, -H, H, 1, 2);
            MatrixState.setCamera(0, 0, 1, 0, 0, 0, 0, 1, 0);
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES30.glClearColor(0, 0, 0, 1);
            GLES30.glEnable(GLES30.GL_CULL_FACE);
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            MatrixState.setInitStack();
            rect = new ColorRect(MySurfaceView.this);
            light = new Light(new Point3(LIGHT_X, LIGHT_Y, LIGHT_Z));
            cam = new Camera(light);
            scn = new Scene(cam, light);
        }
    }
}
