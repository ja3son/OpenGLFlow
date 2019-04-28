package com.ja3son.libdemo.Sample11_5;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;

import com.ja3son.libdemo.R;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static com.ja3son.libdemo.Sample11_5.Constant.UNIT_SIZE;

class MySurfaceView extends GLSurfaceView {
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private SceneRenderer mRenderer;

    private float mPreviousY;
    private float mPreviousX;


    float cx = 0;
    float cy = 2;
    float cz = 24;
    float cr = 24;
    float cAngle = 0;
    int[] textureIdA = new int[6];

    public MySurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(3);
        mRenderer = new SceneRenderer();
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float y = e.getY();
        float x = e.getX();
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dy = y - mPreviousY;
                float dx = x - mPreviousX;
                cAngle += dx * TOUCH_SCALE_FACTOR;
                cx = (float) (Math.sin(Math.toRadians(cAngle)) * cr);
                cz = (float) (Math.cos(Math.toRadians(cAngle)) * cr);
                cy += dy / 10.0f;
        }
        mPreviousY = y;
        mPreviousX = x;
        return true;
    }

    private class SceneRenderer implements Renderer {
        TextureRect texRect;

        public void onDrawFrame(GL10 gl) {

            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);

            MatrixState.setCamera(cx, cy, cz, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

            final float tzz = 0.4f;

            MatrixState.pushMatrix();
            MatrixState.translate(0, 0, -UNIT_SIZE + tzz);
            texRect.drawSelf(textureIdA[0]);
            MatrixState.popMatrix();

            MatrixState.pushMatrix();
            MatrixState.translate(0, 0, UNIT_SIZE - tzz);
            MatrixState.rotate(180, 0, 1, 0);
            texRect.drawSelf(textureIdA[5]);
            MatrixState.popMatrix();

            MatrixState.pushMatrix();
            MatrixState.translate(-UNIT_SIZE + tzz, 0, 0);
            MatrixState.rotate(90, 0, 1, 0);
            texRect.drawSelf(textureIdA[1]);
            MatrixState.popMatrix();

            MatrixState.pushMatrix();
            MatrixState.translate(UNIT_SIZE - tzz, 0, 0);
            MatrixState.rotate(-90, 0, 1, 0);
            texRect.drawSelf(textureIdA[2]);
            MatrixState.popMatrix();

            MatrixState.pushMatrix();
            MatrixState.translate(0, -UNIT_SIZE + tzz, 0);
            MatrixState.rotate(-90, 1, 0, 0);
            texRect.drawSelf(textureIdA[3]);
            MatrixState.popMatrix();

            MatrixState.pushMatrix();
            MatrixState.translate(0, UNIT_SIZE - tzz, 0);
            MatrixState.rotate(90, 1, 0, 0);
            texRect.drawSelf(textureIdA[4]);
            MatrixState.popMatrix();
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {

            GLES30.glViewport(0, 0, width, height);

            float ratio = (float) width / height;

            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 2, 1000);
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {

            GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

            GLES30.glEnable(GLES30.GL_DEPTH_TEST);

            GLES30.glEnable(GLES30.GL_CULL_FACE);

            MatrixState.setInitStack();

            texRect = new TextureRect(MySurfaceView.this);

            textureIdA[0] = initTexture(R.raw.skycubemap_back);
            textureIdA[1] = initTexture(R.raw.skycubemap_left);
            textureIdA[2] = initTexture(R.raw.skycubemap_right);
            textureIdA[3] = initTexture(R.raw.skycubemap_down);
            textureIdA[4] = initTexture(R.raw.skycubemap_up);
            textureIdA[5] = initTexture(R.raw.skycubemap_front);
        }
    }

    public int initTexture(int drawableId) {

        int[] textures = new int[1];
        GLES30.glGenTextures
                (
                        1,
                        textures,
                        0
                );
        int textureId = textures[0];
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_REPEAT);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_REPEAT);


        InputStream is = this.getResources().openRawResource(drawableId);
        Bitmap bitmapTmp;
        try {
            bitmapTmp = BitmapFactory.decodeStream(is);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        GLUtils.texImage2D
                (
                        GLES30.GL_TEXTURE_2D,
                        0,
                        bitmapTmp,
                        0
                );
        bitmapTmp.recycle();
        return textureId;
    }
}
