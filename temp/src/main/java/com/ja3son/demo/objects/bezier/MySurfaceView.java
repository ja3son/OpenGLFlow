package com.ja3son.demo.objects.bezier;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.ja3son.temp.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;

class MySurfaceView extends GLSurfaceView {

    private final float TOUCH_SCALE_FACTOR = 180.0f / 480;//�Ƕ����ű���

    private float cameraX = 0;//�������λ��
    private float cameraY = 0;
    private float cameraZ = 8;

    private float targetX = 0;//����
    private float targetY = -2;
    private float targetZ = -15;

    private float sightDis = 30;//�������Ŀ��ľ���
    private float angdegElevation = 45;//����
    private float angdegAzimuth = 90;//��λ��

    private float mPreviousY;//�ϴεĴ���λ��Y����
    private float mPreviousX;//�ϴεĴ���λ��X����

    private SceneRenderer mRenderer;//������Ⱦ��

    int textureId;      //ϵͳ���������id 

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
                angdegAzimuth += dx * TOUCH_SCALE_FACTOR;//������y����ת�Ƕ�
                angdegElevation += dy * TOUCH_SCALE_FACTOR;//������x����ת�Ƕ�

                //��λ��
                if (angdegAzimuth >= 360) {
                    angdegAzimuth = 0;
                } else if (angdegAzimuth <= 0) {
                    angdegAzimuth = 360;
                }
                //����
                if (angdegElevation >= 85) {
                    angdegElevation = 85;
                } else if (angdegElevation <= 0) {
                    angdegElevation = 0;
                }
        }
        mPreviousY = y;//��¼���ر�λ��
        mPreviousX = x;//��¼���ر�λ��
        return true;
    }

    private class SceneRenderer implements Renderer {

        Building building;

        public void onDrawFrame(GL10 gl) {
            //�����Ȼ�������ɫ����
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);

            double angradElevation = Math.toRadians(angdegElevation);//���ǣ����ȣ�
            double angradAzimuth = Math.toRadians(angdegAzimuth);//��λ��
            cameraX = (float) (targetX + sightDis * Math.cos(angradElevation) * Math.cos(angradAzimuth));
            cameraY = (float) (targetY + sightDis * Math.sin(angradElevation));
            cameraZ = (float) (targetZ + sightDis * Math.cos(angradElevation) * Math.sin(angradAzimuth));

            MatrixState.setCamera(//����cameraλ�� 
                    cameraX, //����λ�õ�X
                    cameraY, //����λ�õ�Y
                    cameraZ, //����λ�õ�Z

                    targetX, //�����򿴵ĵ�X
                    targetY, //�����򿴵ĵ�Y
                    targetZ, //�����򿴵ĵ�Z

                    0,  //ͷ�ĳ���
                    1, 0);

            //�����ֳ�
            MatrixState.pushMatrix();
            MatrixState.translate(0, 0, -15);
            building.drawSelf(textureId);
            MatrixState.popMatrix();

        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //�����Ӵ���С��λ�� 
            GLES30.glViewport(0, 0, width, height);
            //����GLSurfaceView�Ŀ�߱�
            float ratio = (float) width / height;
            //���ô˷����������͸��ͶӰ����
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 4f, 100);
            //            //���ô˷������������9����λ�þ���
            //            MatrixState.setCamera(0,0,8.0f,0f,0f,0f,0f,1.0f,0.0f);

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
            textureId = initTexture(R.drawable.white);

            //��������
            building = new Building(MySurfaceView.this, 0.8f, 8, 8);

        }
    }

    public int initTexture(int drawableId)//textureId
    {
        //��������ID
        int[] textures = new int[1];
        GLES30.glGenTextures(1,          //����������id������
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
        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D,   //�������ͣ���OpenGL ES�б���ΪGL10.GL_TEXTURE_2D
                0,                      //����Ĳ�Σ�0��ʾ����ͼ��㣬�������Ϊֱ����ͼ
                bitmapTmp,              //����ͼ��
                0                      //����߿�ߴ�
        );
        bitmapTmp.recycle();          //������سɹ����ͷ�ͼƬ

        return textureId;
    }

}
