package com.ja3son.libdemo.Sample7_8;

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
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.CylinderShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import com.ja3son.libdemo.R;
import com.ja3son.libdemo.Sample7_8.util.LoadUtil;
import com.ja3son.libdemo.Sample7_8.util.LoadedObjectVertexNormalTexture;
import com.ja3son.libdemo.Sample7_8.util.MatrixState;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MySurfaceView extends GLSurfaceView {
	SenceRenderer mRenderer;
	int land;
	int vehicleTextureId;
	int boxId;
	int pailId;
	DiscreteDynamicsWorld dynamicsWorld;//�������\
	javax.vecmath.Vector3f carStartPoint=new javax.vecmath.Vector3f(0,0,0);
	Car car;
	Box box1;
	Box box2;
	Box box3;
	Box box4;
	Box box5;
	Box box6;
	Pail pail1;
	Pail pail2;
	Pail pail3;
	TextFloor tf;
	float Width;
	float Hight;
	boolean flag;
	float cam=0;
	
	float v_x = 1f; //����
    float v_y = 1f;//���Ӹ�
    float v_z = 1f;//���ӳ�
	float v_xp =1.9f;//pail��
    float v_yp = 1.9f;//pail��
    float v_zp = 1.9f;//pail��
    CollisionShape rbShape;
    CollisionShape rbShape_pail;
	public MySurfaceView(GameActivity context) {
		super(context);
		initWorld(); //��ʼ����������
		this.setEGLContextClientVersion(3);	//����ʹ��OpenGL ES3.0
		mRenderer=new SenceRenderer();//����������Ⱦ��
		this.setRenderer(mRenderer);//������Ⱦ��
		//������ȾģʽΪ������Ⱦ
		this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if(flag){//��������꣬���ؿ��Դ���
		float X=event.getX();
		float Y=event.getY();
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				if(X<Width/2)
				{
					car.carMove_go();
					if(Y<Hight/2)
					{
						cam++;
					}
				}else{
					car.carMove_back();
					if(Y<Hight/2)
					{
						car.clientResetScene();
						box1.clientResetScene();
						box2.clientResetScene();
						box3.clientResetScene();
						box4.clientResetScene();
						box5.clientResetScene();
						box6.clientResetScene();
						pail1.clientResetScene();
						pail2.clientResetScene();
						pail3.clientResetScene();
					}
				}
			break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				car.carKong();
			break;
		}
		}
		return true;
		
	}
	class SenceRenderer implements Renderer//������Ⱦ���ڲ���
	{
		LoadedObjectVertexNormalTexture pm;//���صĵ���
		LoadedObjectVertexNormalTexture vehicleBox;
		LoadedObjectVertexNormalTexture vehicleWheel;
		LoadedObjectVertexNormalTexture boxTexture;
		LoadedObjectVertexNormalTexture pailTexture;
		
		public void onDrawFrame(GL10 gl) {//����һ֡����ķ���
			//�����Ȼ�������ɫ����
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            if(cam%2==0)//ʹ�ó��������ӽ�
            car.updateCamera2();  
            else if(cam%2==1)
            car.updateCamera1();//ʹ�ø��ӵ��ӽ�
           // pm.drawSelf(land); 
            MatrixState.pushMatrix();//�����ֳ�
        	        	 
        	car.carTurn(Constant.Angle*60.0f*-1);//���ó���ǰ�ַ���
        	car.drawSelf();//��������
        	box1.drawSelf();//��������
        	box2.drawSelf();
        	box3.drawSelf();
        	box4.drawSelf();
        	box5.drawSelf();
        	box6.drawSelf();
        	pail1.drawSelf();
        	pail2.drawSelf();  
        	pail3.drawSelf();
        	
        	MatrixState.popMatrix(); //�ָ��ֳ�
        	tf.drawSelf(land);//���Ƶ���
        	//��������ģ�����
        	dynamicsWorld.stepSimulation(1.0f/60, 1);
		}
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			GLES30.glViewport(0,0, width, height);
			float ratio=(float)width/height;
			MatrixState.setProjectFrustum(-ratio,ratio,-1,1,1,1000);
			Width=width;
			Hight=height;
		}
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
	       	//������Ļ����ɫRGBA
            GLES30.glClearColor(0.0f,0.0f,0.0f,1.0f);    
            //����ȼ��
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            //�򿪱������   
            GLES30.glEnable(GLES30.GL_CULL_FACE);
            //��ʼ���任����
            MatrixState.setInitStack();            
			land=initTexture(R.drawable.grass,MySurfaceView.this);//���ز�Ƥ����
			vehicleTextureId=initTexture(R.drawable.car, MySurfaceView.this);
			boxId=initTexture(R.drawable.box, MySurfaceView.this);  
			pailId=initTexture(R.drawable.pail, MySurfaceView.this);
			//���س���objģ��
			vehicleBox=LoadUtil.loadFromFile("carBox.obj",MySurfaceView.this.getResources(),MySurfaceView.this);
			vehicleWheel=LoadUtil.loadFromFile("carWheel.obj",MySurfaceView.this.getResources(),MySurfaceView.this);
			boxTexture=LoadUtil.loadFromFile("cube.obj",MySurfaceView.this.getResources(),MySurfaceView.this);
			pailTexture= LoadUtil.loadFromFile("tong.obj",MySurfaceView.this.getResources(),MySurfaceView.this);
			//�½���������
			car=new Car(dynamicsWorld, vehicleBox, vehicleWheel, vehicleTextureId,carStartPoint);
			//�½����Ӷ���
			box1=new Box(1,   0.6f, 10,0,dynamicsWorld, boxTexture, boxId,rbShape);
			box2=new Box(0,   0.6f, 10,0, dynamicsWorld, boxTexture, boxId,rbShape);
			box3=new Box(2,   0.6f, 10,0, dynamicsWorld, boxTexture, boxId,rbShape);
			box4=new Box(0.5f,1.7f, 10,0,dynamicsWorld, boxTexture, boxId,rbShape);
			box5=new Box(1.5f,1.7f, 10,0, dynamicsWorld, boxTexture, boxId,rbShape);
			box6=new Box(1,   2.7f, 10,0, dynamicsWorld, boxTexture, boxId,rbShape);
			pail1=new Pail(-3,1.1f, 10,0,dynamicsWorld, pailTexture, pailId,rbShape_pail);
			pail2=new Pail(-5,1.1f, 10,0,dynamicsWorld, pailTexture, pailId,rbShape_pail);
			pail3=new Pail(-4,3.2f, 10, 0,dynamicsWorld, pailTexture, pailId,rbShape_pail);			
			flag=true;//���ñ�־λ���������Դ���
			tf=new TextFloor(30,0,MySurfaceView.this);//�½����Ƶ���Ķ���  
		}	
	}
  	public static int initTexture(int drawableId,MySurfaceView gsv)//textureId
	{
		//��������ID
		int[] textures = new int[1];
		GLES30.glGenTextures
		(
				1,          //����������id������
				textures,   //����id������
				0           //ƫ����
		);    
		int textureId=textures[0];    
		GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER,GLES30.GL_NEAREST);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MAG_FILTER,GLES30.GL_LINEAR);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_REPEAT);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_REPEAT);
   
        //ͨ������������ͼƬ===============begin===================
        InputStream is = gsv.getResources().openRawResource(drawableId);
        Bitmap bitmapTmp;
        try 
        {
        	bitmapTmp = BitmapFactory.decodeStream(is);
        } 
        finally 
        {
            try 
            {
                is.close();
            } 
            catch(IOException e) 
            {
                e.printStackTrace();
            }
        }
        //ͨ������������ͼƬ===============end===================== 
	   	GLUtils.texImage2D
	    (
	    		GLES30.GL_TEXTURE_2D, //��������
	     		0, 
	     		GLUtils.getInternalFormat(bitmapTmp), 
	     		bitmapTmp, //����ͼ��
	     		//GLUtils.getType(bitmapTmp), 
	     		0 //����߿�ߴ�
	     );

	    bitmapTmp.recycle(); 		  //������سɹ����ͷ�ͼƬ
        return textureId; 
	}
 	public void initWorld()//��ʼ����������ķ���
	{
 		
		CollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();		
		CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);		
		javax.vecmath.Vector3f worldAabbMin = new javax.vecmath.Vector3f(-10000, -10000, -10000);
		javax.vecmath.Vector3f worldAabbMax = new javax.vecmath.Vector3f(10000, 10000, 10000);
		int maxProxies = 102400;
		AxisSweep3 overlappingPairCache =new AxisSweep3(worldAabbMin, worldAabbMax, maxProxies);
		SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();
		dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, overlappingPairCache, solver,collisionConfiguration);
		dynamicsWorld.setGravity(new javax.vecmath.Vector3f(0,-10,0));
		
		//����������ײ��״
		CollisionShape groundShape = new BoxShape(new javax.vecmath.Vector3f(30f, 0f, 30f));
		Transform groundTransform = new Transform();//��������ĳ�ʼ�任����
		groundTransform.setIdentity();//�Գ�ʼ�任�����ʼ��
		groundTransform.origin.set(0, 0, 0);//�ƶ�����
		//����localCreateRigidBody�����������������󲢽��������������
		localCreateRigidBody(0, groundTransform, groundShape);
		//������������ĳ��������ײ��״
		rbShape = new BoxShape(new javax.vecmath.Vector3f(v_x/2, v_y/2, v_z/2));
		//������Ͱ�����Բ����ײ��״
		rbShape_pail = new CylinderShape(new javax.vecmath.Vector3f(v_xp/2, v_yp/2, v_zp/2));
	}
 	//�����������ķ���
	public RigidBody localCreateRigidBody(float mass, Transform startTransform, CollisionShape shape) {
		// rigidbody is dynamic if and only if mass is non zero, otherwise static
		boolean isDynamic = (mass != 0f);//�����Ƿ�����ƶ�
		//���Խ������
		javax.vecmath.Vector3f localInertia = new javax.vecmath.Vector3f(0f, 0f, 0f);
		if (isDynamic) {//���������ƶ�
			shape.calculateLocalInertia(mass, localInertia);//�������
		}
		// using motionstate is recommended, it provides interpolation capabilities, and only synchronizes 'active' objects
		//����������˶�״̬����
		DefaultMotionState myMotionState = new DefaultMotionState(startTransform);
		//��������������Ϣ����
		RigidBodyConstructionInfo cInfo = new RigidBodyConstructionInfo(mass, myMotionState, shape, localInertia);
		//�½��������
		RigidBody body = new RigidBody(cInfo);
		//����Ħ��ϵ��
		body.setFriction(0.8f);
		dynamicsWorld.addRigidBody(body);//�Ѵ˸�����뵽����������
		return body;
	}
}
