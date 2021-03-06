package com.ja3son.libdemo.Sample11_6;

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

import static com.ja3son.libdemo.Sample11_6.Constant.loadLandforms;
import static com.ja3son.libdemo.Sample11_6.Constant.yArray;
import static com.ja3son.libdemo.Sample11_6.Sample11_6Activity.HEIGHT;
import static com.ja3son.libdemo.Sample11_6.Sample11_6Activity.WIDTH;

public class MySurfaceView extends GLSurfaceView {
    static float direction = 0;
    static float cx = 0;
    static float cz = 20;

    static float tx = 0;
    static float tz = 0;
    static final float DEGREE_SPAN = (float) (3.0 / 180.0f * Math.PI);

    boolean flag = true;
    float x;
    float y;
    float Offset = 20;
    SceneRenderer mRender;
    float preX;
    float preY;

    public MySurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(3);
        mRender = new SceneRenderer();
        setRenderer(mRender);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = event.getX();
        y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                flag = true;
                new Thread() {
                    @Override
                    public void run() {
                        while (flag) {
                            if (x > 0 && x < WIDTH / 2 && y > 0 && y < HEIGHT / 2) {
                                cx = cx - (float) Math.sin(direction) * 1.0f;
                                cz = cz - (float) Math.cos(direction) * 1.0f;
                            } else if (x > WIDTH / 2 && x < WIDTH && y > 0 && y < HEIGHT / 2) {
                                cx = cx + (float) Math.sin(direction) * 1.0f;
                                cz = cz + (float) Math.cos(direction) * 1.0f;
                            } else if (x > 0 && x < WIDTH / 2 && y > HEIGHT / 2 && y < HEIGHT) {
                                direction = direction + DEGREE_SPAN;
                            } else if (x > WIDTH / 2 && x < WIDTH && y > HEIGHT / 2 && y < HEIGHT) {
                                direction = direction - DEGREE_SPAN;
                            }
                            try {
                                Thread.sleep(100);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();
                break;
            case MotionEvent.ACTION_UP:
                flag = false;
                break;
        }


        tx = (float) (cx - Math.sin(direction) * Offset);
        tz = (float) (cz - Math.cos(direction) * Offset);
        return true;
    }

    private class SceneRenderer implements Renderer {
        Mountion mountion;

        int mountionId;
        int rockId;
        Sky sky;
        int skyId;

        @Override
        public void onDrawFrame(GL10 gl) {

            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);

            MatrixState.setCamera(cx, 5, cz, tx, 2, tz, 0, 1, 0);
            MatrixState.pushMatrix();
            mountion.drawSelf(mountionId, rockId);
            MatrixState.popMatrix();

            MatrixState.pushMatrix();
            MatrixState.translate(0, -2, 0);
            sky.drawSelf(skyId);
            MatrixState.popMatrix();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {

            GLES30.glViewport(0, 0, width, height);

            float ratio = (float) width / height;

            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 1, 3000);

            MatrixState.setCamera(cx, 5, cz, tx, 2, tz, 0, 1, 0);
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {

            GLES30.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            MatrixState.setInitStack();
            yArray = loadLandforms(MySurfaceView.this.getResources(), R.drawable.land);

            mountion = new Mountion(MySurfaceView.this, yArray, yArray.length - 1, yArray[0].length - 1);
            sky = new Sky(MySurfaceView.this);

            skyId = initTexture(R.drawable.sky, false);
            mountionId = initTexture(R.drawable.grass, true);
            rockId = initTexture(R.drawable.rock, true);
        }
    }

    public int initTexture(int drawableId, boolean isMipmap) {

        int[] textures = new int[1];
        GLES30.glGenTextures
                (
                        1,
                        textures,
                        0
                );
        int textureId = textures[0];
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);
        if (isMipmap) {

            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR_MIPMAP_LINEAR);

            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR_MIPMAP_NEAREST);
        } else {

            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);

            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
        }

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

        if (isMipmap) {
            GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D);
        }

        bitmapTmp.recycle();

        return textureId;
    }
}