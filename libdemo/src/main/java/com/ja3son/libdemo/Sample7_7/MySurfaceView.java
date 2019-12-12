package com.ja3son.libdemo.Sample7_7;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.ja3son.libdemo.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.vecmath.Vector3f;

import static com.ja3son.libdemo.Sample7_7.Constant.*;

class MySurfaceView extends GLSurfaceView {
    private SceneRenderer mRenderer;//������Ⱦ��
    //=============ʰȡ============
    //��Դ����Y��ľ���
    final float LIGHT_Y = 70;
    float mPreviousX;//�ϴεĴ���λ��X����
    float mPreviousY;
    float preNanoTime;
    //�Ƿ��ƶ��ı�־λ
    boolean isMoveFlag = false;
    float cx = 0;    //�����λ��x
    float cy = 1;
    float cz = 6;   //�����λ��z
    float tx = 0;   //�����Ŀ���x
    float tz = -10;   //�����Ŀ���z
    //�����ת���ĽǶ�
    double tempRadians = 0;
    //�洢̧���ֺ������ת���ĽǶ�
    double upRadians = 0;
    float left;
    float right;
    float top;
    float bottom;
    float near;
    float far;
    ArrayList<LoadedObjectVertexNormal> lovnList = new ArrayList<LoadedObjectVertexNormal>();
    //��ѡ�����������ֵ����id��û�б�ѡ��ʱ����ֵΪ-1
    int checkedIndex = -1;
    float xOffset = 0;//�������ƶ���λ��
    float yOffset = 0;
    float zOffset = 0;

    //===========����==========
    static enum Area {LU, RU, LD, RD, NONE}

    ;
    Area currArea = Area.NONE;
    boolean areaTouch = false;
    AreaTouchThread areaTouchThread;

    //========�����========
    Vector3f cameraCircleCenter = new Vector3f(0, 0, 0);

    boolean forward = false;
    //===============����ģ��====================
    DiscreteDynamicsWorld dynamicsWorld;//�������
    CollisionShape ballShape;//���õ�������
    CollisionShape planeShape;//���õ�ƽ����״
    Sample7_7_Activity activity;
    LoadedObjectVertexNormal[] loadedModels = new LoadedObjectVertexNormal[7];
    LoadedObjectVertexNormal[] bodyForDraws = new LoadedObjectVertexNormal[BodyPartIndex.BODYPART_COUNT.ordinal()];

    public MySurfaceView(Context context) {
        super(context);
        this.activity = (Sample7_7_Activity) context;
        this.setEGLContextClientVersion(3);
        //��ʼ����������
        initWorld();
        mRenderer = new SceneRenderer();    //����������Ⱦ��
        setRenderer(mRenderer);                //������Ⱦ��
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ   
    }

    //��ʼ����������ķ���
    public void initWorld() {
        //������ײ���������Ϣ����
        CollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
        //������ײ����㷨�����߶����书��Ϊɨ�����е���ײ���ԣ���ȷ�����õļ����Զ�Ӧ���㷨
        CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);
        //����������������ı߽���Ϣ
        Vector3f worldAabbMin = new Vector3f(-10000, -10000, -10000);
        Vector3f worldAabbMax = new Vector3f(10000, 10000, 10000);
        int maxProxies = 1024;
        //������ײ���ֲ�׶εļ����㷨����
        AxisSweep3 overlappingPairCache = new AxisSweep3(worldAabbMin, worldAabbMax, maxProxies);
        //�����ƶ�Լ������߶���
        SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();
        //���������������
        dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, overlappingPairCache, solver, collisionConfiguration);
        //�����������ٶ�
        dynamicsWorld.setGravity(new Vector3f(0, -30, 0));
        //�������õ�������
        ballShape = new SphereShape(1);
        //�������õ�ƽ����״
        planeShape = new StaticPlaneShape(new Vector3f(0, 1, 0), 0);
    }

    public void loadCapsules() {//���ؽ���
        loadedModels[0] = LoadUtil.loadFromFile("head.obj", activity.getResources(), MySurfaceView.this);//ͷ
        loadedModels[1] = LoadUtil.loadFromFile("spine.obj", activity.getResources(), MySurfaceView.this);//��׵
        loadedModels[2] = LoadUtil.loadFromFile("pelvis.obj", activity.getResources(), MySurfaceView.this);//����
        loadedModels[3] = LoadUtil.loadFromFile("upper_arm.obj", activity.getResources(), MySurfaceView.this);//���
        loadedModels[4] = LoadUtil.loadFromFile("lower_arm.obj", activity.getResources(), MySurfaceView.this);//С��
        loadedModels[5] = LoadUtil.loadFromFile("upper_leg.obj", activity.getResources(), MySurfaceView.this);//����
        loadedModels[6] = LoadUtil.loadFromFile("lower_leg.obj", activity.getResources(), MySurfaceView.this);//С��
    }

    public void initBodyForDraws() {
        bodyForDraws[BodyPartIndex.BODYPART_HEAD.ordinal()] = loadedModels[0];
        bodyForDraws[BodyPartIndex.BODYPART_SPINE.ordinal()] = loadedModels[1];
        bodyForDraws[BodyPartIndex.BODYPART_PELVIS.ordinal()] = loadedModels[2];
        bodyForDraws[BodyPartIndex.BODYPART_RIGHT_UPPER_ARM.ordinal()] = loadedModels[3];
        bodyForDraws[BodyPartIndex.BODYPART_LEFT_UPPER_ARM.ordinal()] = loadedModels[3].clone();
        bodyForDraws[BodyPartIndex.BODYPART_LEFT_LOWER_ARM.ordinal()] = loadedModels[4];
        bodyForDraws[BodyPartIndex.BODYPART_RIGHT_LOWER_ARM.ordinal()] = loadedModels[4].clone();
        bodyForDraws[BodyPartIndex.BODYPART_RIGHT_UPPER_LEG.ordinal()] = loadedModels[5];
        bodyForDraws[BodyPartIndex.BODYPART_LEFT_UPPER_LEG.ordinal()] = loadedModels[5].clone();
        bodyForDraws[BodyPartIndex.BODYPART_RIGHT_LOWER_LEG.ordinal()] = loadedModels[6];
        bodyForDraws[BodyPartIndex.BODYPART_LEFT_LOWER_LEG.ordinal()] = loadedModels[6].clone();
        for (int i = 0; i < bodyForDraws.length; i++) {
            lovnList.add(bodyForDraws[i]);
        }
    }

    float x;
    float y;

    @Override
    public boolean onTouchEvent(MotionEvent event)//�������¼��ķ���
    {
        x = event.getX();//��ȡ���ص��λ��
        y = event.getY();
        switch (event.getAction())//���ݲ�ͬ�Ķ�������ִ�в�ͬ�Ĳ���
        {
            case MotionEvent.ACTION_DOWN://���µĶ���
                mPreviousX = event.getX();//��¼���¶�����Ӧ��λ��
                mPreviousY = event.getY();
                preNanoTime = System.nanoTime();//��ȡ��ǰϵͳʱ��
                checkedIndex = -1;//����ѡ����������(-1��ʾû��ѡ������)

                //����ʰȡ�����ߵ�������յ�
                float[] AB = IntersectantUtil.calculateABPosition
                        (
                                x, //���ص�X����
                                y, //���ص�Y����
                                Sample7_7_Activity.screenWidth, //��Ļ���
                                Sample7_7_Activity.screenHeight, //��Ļ�߶�
                                left, //��ƽ��leftֵ
                                top,//��ƽ��topֵ
                                near, //��ƽ�����
                                far//Զƽ�����
                        );

                //����AB
                MyVector3f start = new MyVector3f(AB[0], AB[1], AB[2]);//ʰȡ���ߵ����A
                MyVector3f end = new MyVector3f(AB[3], AB[4], AB[5]);//ʰȡ���ߵ��յ�B
                MyVector3f dir = end.minus(start);//ʰȡ���ߵĳ��Ⱥͷ���
                /*
                 * ����AB�߶���ÿ�������Χ�е���ѽ���(��A������Ľ���)��
                 * ����¼����ѽ�����������б��е�����ֵ
                 */
                //��¼�б���ʱ����С������ֵ
                int tmpIndex = -1;//���ڼ�¼����A��������������ֵ
                float minTime = 1;//���ڼ�¼�б��������������ཻ�����ʱ��
                for (int i = 0; i < lovnList.size(); i++) {//�����б��е�����
                    AABB3 box = lovnList.get(i).getCurrBox(); //��������AABB��Χ��
                    float t = box.rayIntersect(start, dir, null);//�����ཻʱ��
                    if (t <= minTime) {
                        minTime = t;//�������ʱ��
                        tmpIndex = i;//���������������
                    }
                }
                checkedIndex = tmpIndex;//��¼���(ѡ��)��������
                changeObj(checkedIndex);//�ı䱻ѡ������

                break;
            case MotionEvent.ACTION_MOVE://�ƶ��Ķ���
                //�ƶ��������ָ������ֵ������Ϊ���������ƶ�
                if (x - mPreviousX >= 10.0f || x - mPreviousX <= -10.0f) {//�ƶ�������Ӧ��ֵ������Ϊ���������ƶ�
                    isMoveFlag = true;
                }
                if (isMoveFlag)//�������ƶ�
                {
                    if (checkedIndex != -1) {//����ѡ������������Ϊ-1
                        //��ȡѡ������
                        LoadedObjectVertexNormal lovo = lovnList.get(checkedIndex);
                        lovo.isPicked = true;//���ô������ѡ�б�־λΪtrue
                        //�����µĴ��ص�����������ϵ�е�λ��
                        float[] nearXY = IntersectantUtil.calculateABPosition(
                                x, y,
                                Sample7_7_Activity.screenWidth, Sample7_7_Activity.screenHeight,
                                left, top,
                                near, far
                        );
                        //������һ���ص�����������ϵ�е�λ��
                        float[] nearPreXY = IntersectantUtil.calculateABPosition(
                                mPreviousX, mPreviousY,
                                Sample7_7_Activity.screenWidth, Sample7_7_Activity.screenHeight,
                                left, top,
                                near, far
                        );
                        //��ȡ��ѡ������������Ե�ؽ��ڸ�������ϵ�е�λ��
                        Vector3f currPivot = lovo.p2p.getPivotInB(new Vector3f());
                        //������϶���������3��������ֵ
                        Vector3f dir1 = new Vector3f(nearXY[0] - nearPreXY[0], nearXY[1] - nearPreXY[1], nearXY[2] - nearPreXY[2]);
                        float vFactor = 0.5f;
                        dir1.set(dir1.x * vFactor, dir1.y * vFactor, dir1.z * vFactor);
                        currPivot.add(dir1);//������϶���Ĺؽ�λ��
                        lovo.p2p.setPivotB(currPivot);//�����϶���Ĺؽ�λ��
                    }
                }
                break;
            case MotionEvent.ACTION_UP://̧��Ķ���

                isMoveFlag = false;//���ƶ���־λ����Ϊfalse
                if (checkedIndex != -1) {//�������屻ѡ��
                    LoadedObjectVertexNormal lovo = lovnList.get(checkedIndex);
                    lovo.removePickedConstraint();//ɾ�������϶��ĵ�Ե�ؽ�
                    checkedIndex = -1;//ѡ��������������Ϊ-1����ʾ��ѡ������
                }
                if (checkedIndex == -1 && currArea != Area.NONE) {//��û��ѡ������
                    currArea = Area.NONE;
                    areaTouch = false;
                    upRadians = tempRadians;
                }
                break;
        }
        return true;
    }

    //�ı��б����±�Ϊindex������
    public void changeObj(int index) {//���������ı�ѡ������
        if (index != -1) {//��������屻ѡ��
            LoadedObjectVertexNormal lovo = lovnList.get(index);
            lovo.body.activate();
            lovo.addPickedConstraint();
        } else {
            //��ʾû��ʰȡ��
            if (0 < x && x < Sample7_7_Activity.screenWidth / 2 && 0 < y && y < Sample7_7_Activity.screenHeight / 2) {
                currArea = Area.LU;
            } else if (Sample7_7_Activity.screenWidth / 2 < x && 0 < y && y < Sample7_7_Activity.screenHeight / 2) {
                currArea = Area.RU;
            } else if (0 < x && x < Sample7_7_Activity.screenWidth / 2 && y > Sample7_7_Activity.screenHeight / 2) {
                currArea = Area.LD;
            } else if (x > Sample7_7_Activity.screenWidth / 2 && y > Sample7_7_Activity.screenHeight / 2) {
                currArea = Area.RD;
            }
            areaTouch = true;
            if (areaTouchThread == null || !areaTouchThread.isAlive()) {
                areaTouchThread = new AreaTouchThread();
                areaTouchThread.start();
            }
        }
    }

    private class SceneRenderer implements Renderer {
        int floorTextureId;//��������
        TexFloor floor;//�������1
        Doll doll;

        public void onDrawFrame(GL10 gl) {

            //�����Ȼ�������ɫ����
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);

            //��ʼ����Դλ��
            MatrixState.setLightLocation((float) (LIGHT_Y * Math.sin(tempRadians)), 30, (float) (LIGHT_Y * Math.cos(tempRadians)));

            //��ʼ���任����
            cx = (float) (25 * Math.sin(tempRadians) + cameraCircleCenter.x);
            cz = (float) (25 * Math.cos(tempRadians) + cameraCircleCenter.z);
            tx = (float) (10 * Math.sin(tempRadians) + cameraCircleCenter.x);
            tz = (float) (10 * Math.cos(tempRadians) + cameraCircleCenter.z);
            MatrixState.setCamera(cx, cy, cz, tx, 0f, tz, 0f, 1.0f, 0.0f);

            MatrixState.pushMatrix();
            MatrixState.copyMVMatrix();
            doll.drawSelf(checkedIndex);
            //���Ƶذ�
            MatrixState.pushMatrix();
            floor.drawSelf(floorTextureId);
            MatrixState.popMatrix();

            MatrixState.popMatrix();
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //�����Ӵ���С��λ�� 
            GLES30.glViewport(0, 0, width, height);
            //����͸��ͶӰ�ı���
            float ratio = (float) width / height;
            left = right = ratio;
            top = bottom = 1;
            near = 2;
            far = 100;
            MatrixState.setProjectFrustum(-left, right, -bottom, top, near, far);
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //������Ļ����ɫ��ɫRGBA
            GLES30.glClearColor(0, 0, 0, 0);
            //������Ȳ���
            GLES30.glEnable(GL10.GL_DEPTH_TEST);
            //����Ϊ�򿪱������
            GLES30.glEnable(GL10.GL_CULL_FACE);


            MatrixState.setInitStack();
            ShaderManager.loadCodeFromFile(activity.getResources());
            ShaderManager.compileShader();
            loadCapsules();
            initBodyForDraws();

            //��ʼ������
            floorTextureId = initTextureRepeat(R.drawable.f6);
            doll = new Doll(MySurfaceView.this, dynamicsWorld, bodyForDraws);

            //�����������
            floor = new TexFloor(ShaderManager.getTextureShaderProgram(), 80 * Constant.UNIT_SIZE, -Constant.UNIT_SIZE, planeShape, dynamicsWorld);
            new Thread() {
                public void run() {
                    while (true) {
                        try {
                            //��ʼģ��
                            dynamicsWorld.stepSimulation(TIME_STEP, MAX_SUB_STEPS);
                            Thread.sleep(20);    //��ǰ�߳�˯��20����
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();                    //�����߳�
        }
    }

    public int initTexture(int drawableId) {//textureId
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

    public int initTextureRepeat(int drawableId)//textureId
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
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_REPEAT);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_REPEAT);

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
                        GLES30.GL_TEXTURE_2D,   //�������ͣ���OpenGL ES�б���ΪGL20.GL_TEXTURE_2D
                        0,                      //����Ĳ�Σ�0��ʾ����ͼ��㣬�������Ϊֱ����ͼ
                        bitmapTmp,              //����ͼ��
                        0                      //����߿�ߴ�
                );
        bitmapTmp.recycle();          //������سɹ����ͷ�ͼƬ

        return textureId;
    }

    class AreaTouchThread extends Thread {
        float time = 0;
        float linearV = 0.1f;
        float angularV = 2;

        public void run() {
            while (areaTouch) {
                System.out.println("run");
                System.out.println(currArea.ordinal());

                time++;
                if (currArea == Area.LU) {//��ת
                    tempRadians = upRadians - Math.toRadians(time * angularV);
                }
                if (currArea == Area.RU) {//��ת
                    tempRadians = upRadians + Math.toRadians(time * angularV);
                }
                if (currArea == Area.LD) {//ǰ��
                    float moveLength = time * linearV;
                    cameraCircleCenter.x = (float) (cameraCircleCenter.x - moveLength * Math.sin(tempRadians));
                    cameraCircleCenter.z = (float) (cameraCircleCenter.z - moveLength * Math.cos(tempRadians));
                }
                if (currArea == Area.RD) {//����
                    float moveLength = time * linearV;
                    cameraCircleCenter.x = (float) (cameraCircleCenter.x + moveLength * Math.sin(tempRadians));
                    cameraCircleCenter.z = (float) (cameraCircleCenter.z + moveLength * Math.cos(tempRadians));
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
