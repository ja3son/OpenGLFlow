package com.ja3son.demo.objects.c60;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.ja3son.demo.objects.c60.util.ZQTEdgeUtil;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class MySurfaceView extends GLSurfaceView {

	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;// �Ƕ����ű���
	private float mPreviousY;// �ϴεĴ���λ��Y����
	private float mPreviousX;// �ϴεĴ���λ��X����

	private SceneRenderer mRenderer;// ������Ⱦ��
	boolean lightFlag = true; // ������ת�ı�־λ

	float yAngle = 0;// ��y����ת�ĽǶ�
	float xAngle = 0;// ��x����ת�ĽǶ�
	float zAngle = 0;// ��z����ת�ĽǶ�
	// ������UtilTools���������
	UtilTools utilTools;
	// ���������
	Ball ball;
	// ����������
	Stick stick;
	// ��͹�λ����Ϣ����
	ResultData rusultData;

	public MySurfaceView(Context context) {
		super(context);
		this.setEGLContextClientVersion(3); // ����ʹ��OPENGL ES3.0
		mRenderer = new SceneRenderer(); // ����������Ⱦ��
		setRenderer(mRenderer); // ������Ⱦ��
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);// ������ȾģʽΪ������Ⱦ
	}

	// �����¼��ص�����
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		float y = e.getY();
		float x = e.getX();
		switch (e.getAction()) {
		case MotionEvent.ACTION_MOVE:
			float dy = y - mPreviousY;// ���㴥�ر�Yλ��
			float dx = x - mPreviousX;// ���㴥�ر�Xλ��
			yAngle += dx * TOUCH_SCALE_FACTOR;// ������y����ת�Ƕ�
			zAngle += dy * TOUCH_SCALE_FACTOR;// ������z����ת�Ƕ�
		}
		mPreviousY = y;// ��¼���ر�λ��
		mPreviousX = x;// ��¼���ر�λ��
		return true;
	}

	private class SceneRenderer implements Renderer {
		public void onDrawFrame(GL10 gl) {
			// �����Ȼ�������ɫ����
			GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT
					| GLES30.GL_COLOR_BUFFER_BIT);

			MatrixState.pushMatrix();
			MatrixState.translate(0, 0, -10f);
			MatrixState.rotate(yAngle, 0, 1, 0);
			MatrixState.rotate(zAngle, 0, 0, 1);
			// ���ݶ���ĸ����������� 
			for (int i = 0; i < rusultData.CAtomicPosition.length; i++) {
				MatrixState.pushMatrix();
				MatrixState.translate(rusultData.CAtomicPosition[i][0],
						rusultData.CAtomicPosition[i][1], rusultData.CAtomicPosition[i][2]);
				ball.drawSelf();
				MatrixState.popMatrix();
			}

			for (float[] ab : rusultData.ChemicalBondPoints) {
				float[] result = ZQTEdgeUtil.calTranslateRotateScale(ab);

				MatrixState.pushMatrix();
				MatrixState.translate(result[0], result[1], result[2]);
				MatrixState.rotate(result[3], result[4], result[5],result[6]);
				MatrixState.scale(result[7], result[8], result[9]);
				stick.drawSelf();
				MatrixState.popMatrix();
			}
			MatrixState.popMatrix();

		}
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			// �����Ӵ���С��λ��
			GLES30.glViewport(0, 0, width, height);
			// ����GLSurfaceView�Ŀ�߱�
			float ratio = (float) width / height;
			// ���ô˷����������͸��ͶӰ����
			MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 4f, 100);
			// ���ô˷������������9����λ�þ���
			MatrixState.setCamera(0, 0, 8.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
			// ��ʼ����Դ
			MatrixState.setLightLocation(10, 0, -10);
			// ����һ���̶߳�ʱ�޸ĵƹ��λ��
			new Thread() {
				public void run() {
					float redAngle = 0;
					while (lightFlag) {
						// ���ݽǶȼ���ƹ��λ��
						redAngle = (redAngle + 5) % 360;
						float rx = (float) (15 * Math.sin(Math
								.toRadians(redAngle)));
						float rz = (float) (15 * Math.cos(Math
								.toRadians(redAngle)));
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

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			// ������Ļ����ɫRGBA
			GLES30.glClearColor(1f, 1f, 1f, 1f);
			// ������Ȳ���
			GLES30.glEnable(GLES30.GL_DEPTH_TEST);
			// ����Ϊ�򿪱������
			GLES30.glEnable(GLES30.GL_CULL_FACE);
			utilTools = new UtilTools();
			// ��ʼ����Դ����
			rusultData = utilTools.initVertexData(Constant.TRIANGLE_SCALE,
					Constant.TRIANGLE_AHALF, Constant.SPLIT_COUNT);
			// ��ʼ���任����
			MatrixState.setInitStack();
            float[] colorValue = {1,0,0,1};	//������ɫ����
			ball = new Ball(MySurfaceView.this, Constant.BALL_R, colorValue);// ���������
			colorValue = new float[]{1,1,0,1};
			// �����������
			stick = new Stick(MySurfaceView.this, Constant.LENGTH, Constant.R,
					Constant.ANGLE_SPAN, colorValue);// ����Բ�ܶ���
		}
	}
}
