package com.ja3son.demo.objects.cone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;

import com.ja3son.temp.R;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class MySurfaceView extends GLSurfaceView {

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;//�Ƕ����ű���
    private float mPreviousY;//�ϴεĴ���λ��Y����
    private float mPreviousX;//�ϴεĴ���λ��X����

    private SceneRenderer mRenderer;//������Ⱦ��
    int textureId;      //ϵͳ���������id 

    boolean drawWhatFlag = true;    //��������䷽ʽ�ı�־λ
    boolean lightFlag = true;        //������ת�ı�־λ

    public MySurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(3); //����ʹ��OPENGL ES3.0
        mRenderer = new SceneRenderer();    //����������Ⱦ��
        setRenderer(mRenderer);                //������Ⱦ��
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ   
    }

    //�����¼��ص�����
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float y = e.getY();
        float x = e.getX();
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dy = y - mPreviousY;//���㴥�ر�Yλ��
                float dx = x - mPreviousX;//���㴥�ر�Xλ��
                mRenderer.cone.yAngle += dx * TOUCH_SCALE_FACTOR;//������y����ת�Ƕ�
                mRenderer.cone.zAngle += dy * TOUCH_SCALE_FACTOR;//������z����ת�Ƕ�

                mRenderer.conel.yAngle += dx * TOUCH_SCALE_FACTOR;//������y����ת�Ƕ�
                mRenderer.conel.zAngle += dy * TOUCH_SCALE_FACTOR;//������z����ת�Ƕ�
        }
        mPreviousY = y;//��¼���ر�λ��
        mPreviousX = x;//��¼���ر�λ��
        return true;
    }

    private class SceneRenderer implements Renderer {

        Cone cone;
        ConeL conel;

        public void onDrawFrame(GL10 gl) {
            //�����Ȼ�������ɫ����
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);

            //�����ֳ�
            MatrixState.pushMatrix();
            MatrixState.translate(0, 0, -10);
            if (drawWhatFlag) {
                cone.drawSelf();
            } else {
                conel.drawSelf();
            }
            MatrixState.popMatrix();

        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //�����Ӵ���С��λ�� 
            GLES30.glViewport(0, 0, width, height);
            //����GLSurfaceView�Ŀ�߱�
            float ratio = (float) width / height;
            //���ô˷����������͸��ͶӰ����
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 4f, 100);
            //���ô˷������������9����λ�þ���
            MatrixState.setCamera(0, 0, 8.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

            //��ʼ����Դ
            MatrixState.setLightLocation(10, 0, -10);

            //����һ���̶߳�ʱ�޸ĵƹ��λ��
            new Thread() {
                public void run() {
                    float redAngle = 0;
                    while (lightFlag) {
                        //���ݽǶȼ���ƹ��λ��
                        redAngle = (redAngle + 5) % 360;
                        float rx = (float) (15 * Math.sin(Math.toRadians(redAngle)));
                        float rz = (float) (15 * Math.cos(Math.toRadians(redAngle)));
                        MatrixState.setLightLocation(rx, 0, rz);

                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //������Ļ����ɫRGBA
            GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            //������Ȳ���
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            //����Ϊ�򿪱������
            GLES30.glEnable(GLES30.GL_CULL_FACE);
            //��ʼ���任����
            MatrixState.setInitStack();
            //��������
            textureId = initTexture(R.drawable.android_robot0);
            //����Բ׶����
            cone = new Cone(MySurfaceView.this, 1, 1.6f, 3.9f, 36, textureId, textureId);
            //����Բ׶�Ǽܶ���
            conel = new ConeL(MySurfaceView.this, 1, 1.6f, 3.9f, 36);

        }
    }

    public int initTexture(int drawableId)//textureId
    {
        //��������ID
        int[] textures = new int[1];
        GLES30.glGenTextures
                (
                        1,          //����������id������
                        textures,   //����id������
                        0           //ƫ����
                );
        int textureId = textures[0];
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);

        //ͨ������������ͼƬ===============begin===================
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
        //ͨ������������ͼƬ===============end=====================  

        //ʵ�ʼ�������
        GLUtils.texImage2D
                (
                        GLES30.GL_TEXTURE_2D,   //�������ͣ���OpenGL ES�б���ΪGL10.GL_TEXTURE_2D
                        0,                      //����Ĳ�Σ�0��ʾ����ͼ��㣬�������Ϊֱ����ͼ
                        bitmapTmp,              //����ͼ��
                        0                      //����߿�ߴ�
                );
        bitmapTmp.recycle();          //������سɹ����ͷ�ͼƬ

        return textureId;
    }
}
