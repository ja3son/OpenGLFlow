package com.ja3son.libdemo.Sample7_3;

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
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.ja3son.libdemo.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.vecmath.Vector3f;

import static com.ja3son.libdemo.Sample7_3.Constant.EYE_X;
import static com.ja3son.libdemo.Sample7_3.Constant.EYE_Y;
import static com.ja3son.libdemo.Sample7_3.Constant.EYE_Z;
import static com.ja3son.libdemo.Sample7_3.Constant.MAX_SUB_STEPS;
import static com.ja3son.libdemo.Sample7_3.Constant.TARGET_X;
import static com.ja3son.libdemo.Sample7_3.Constant.TARGET_Y;
import static com.ja3son.libdemo.Sample7_3.Constant.TARGET_Z;
import static com.ja3son.libdemo.Sample7_3.Constant.TIME_STEP;
import static com.ja3son.libdemo.Sample7_3.Constant.yArray;


class MySurfaceView extends GLSurfaceView 
{
	private SceneRenderer mRenderer;//������Ⱦ��	
	DiscreteDynamicsWorld dynamicsWorld;//�������
	ArrayList<TexCube> tca=new ArrayList<TexCube>();
	ArrayList<TexCube> tcaForAdd=new ArrayList<TexCube>();
	CollisionShape boxShape;//���õ�������
	CollisionShape planeShape;//���õ�ƽ����״
	Sample7_3_Activity activity;
	
	public MySurfaceView(Context context) 
	{
        super(context);
        this.activity=(Sample7_3_Activity) context;
        this.setEGLContextClientVersion(3);
        Constant.initConstant(this.getResources()); 
        //��ʼ����������
        initWorld();        
        mRenderer = new SceneRenderer();	//����������Ⱦ��
        setRenderer(mRenderer);				//������Ⱦ��		
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ   
    }
	
	//��ʼ����������ķ���
	public void initWorld()
	{
		//������ײ���������Ϣ����
		CollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();		
		//������ײ����㷨�����߶����书��Ϊɨ�����е���ײ���ԣ���ȷ�����õļ����Զ�Ӧ���㷨
		CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);		
		//����������������ı߽���Ϣ
		Vector3f worldAabbMin = new Vector3f(-10000, -10000, -10000);
		Vector3f worldAabbMax = new Vector3f(10000, 10000, 10000);
		int maxProxies = 1024;
		//������ײ���ֲ�׶εļ����㷨����
		AxisSweep3 overlappingPairCache =new AxisSweep3(worldAabbMin, worldAabbMax, maxProxies);
		//�����ƶ�Լ������߶���
		SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();
		//���������������
		dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, overlappingPairCache, solver,collisionConfiguration);
		//�����������ٶ�
		dynamicsWorld.setGravity(new Vector3f(0, -10, 0));
		//�������õ�������
		boxShape=new BoxShape(new Vector3f(Constant.UNIT_SIZE,Constant.UNIT_SIZE,Constant.UNIT_SIZE));
		//�������õ�ƽ����״
		planeShape=new StaticPlaneShape(new Vector3f(0, 1, 0), 0);
	}

	private class SceneRenderer implements Renderer
    {
		int[] cubeTextureId=new int[2];//����������
		int floorTextureId;//��������
		LandForm floor;//�������1		
		
        public void onDrawFrame(GL10 gl) {  
        	//�����ɫ��������Ȼ���
        	GLES30.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);            
            //��������
            synchronized(tca)
			{
	            for(TexCube tc:tca)
	            {
	            	MatrixState.pushMatrix();
	                tc.drawSelf(cubeTextureId); 
	                MatrixState.popMatrix();         
	            }            
			}
            
            //���Ƶذ�
            MatrixState.pushMatrix();
            floor.drawSelf( floorTextureId);
            MatrixState.popMatrix();         
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //�����Ӵ���С��λ�� 
        	GLES30.glViewport(0, 0, width, height);
            float ratio = (float) width / height;
            //���ô˷����������͸��ͶӰ����
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 2, 100);
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //������Ļ����ɫ��ɫRGBA
            GLES30.glClearColor(0,0,0,0);            
            //������Ȳ���
            GLES30.glEnable(GL10.GL_DEPTH_TEST);  
            //����Ϊ�򿪱������
            GLES30.glEnable(GL10.GL_CULL_FACE);
            //��ʼ���任����
            MatrixState.setInitStack();
            MatrixState.setCamera( 
            		EYE_X,   //����λ�õ�X
            		EYE_Y, 	//����λ�õ�Y
            		EYE_Z,   //����λ�õ�Z
            		TARGET_X, 	//�����򿴵ĵ�X
            		TARGET_Y,   //�����򿴵ĵ�Y
            		TARGET_Z,   //�����򿴵ĵ�Z
            		0, 
            		1, 
            		0);
            //��ʼ�����õ���shader����
            ShaderManager.loadCodeFromFile(activity.getResources());
            ShaderManager.compileShader();
            //��ʼ������
            cubeTextureId[0]=initTexture(R.drawable.wood_bin2);
            cubeTextureId[1]=initTexture(R.drawable.wood_bin1);
            floorTextureId=initTextureRepeat(R.drawable.floor);            
            
            //��������
            floor=new LandForm(MySurfaceView.this,
            		Constant.UNIT_SIZE,
            		-Constant.GT_UNIT_SIZE,
            		dynamicsWorld,
            		yArray,
            		yArray.length-1,
            		yArray[0].length-1,
            		ShaderManager.getTextureShaderProgram());
           
            //����������       
            int size=2;   
            float xStart=(-size/2.0f+0.5f)*(2+0.4f)*Constant.GT_UNIT_SIZE;
            float yStart=1.52f;
            float zStart=(-size/2.0f+0.5f)*(2+0.4f)*Constant.GT_UNIT_SIZE-4f;
            for(int i=0;i<size;i++)
            {
            	for(int j=0;j<size;j++)
            	{
            		for(int k=0;k<size;k++)
            		{
            			TexCube tcTemp=new TexCube       //��������������
            			(
            					MySurfaceView.this,		//MySurfaceView������
            					Constant.GT_UNIT_SIZE,
                				boxShape,
                				dynamicsWorld,
                				1,
                				xStart+i*(2+0.4f)*Constant.GT_UNIT_SIZE,
                				yStart+j*(2.02f)*Constant.GT_UNIT_SIZE,         
                				zStart+k*(2+0.4f)*Constant.GT_UNIT_SIZE,
                				ShaderManager.getTextureShaderProgram()//��ɫ����������
                		);            			
            			tca.add(tcTemp);
            			//ʹ��������һ��ʼ�ǲ������
            			tcTemp.body.forceActivationState(RigidBody.WANTS_DEACTIVATION);
            		}
            	}
            }
            
            new Thread()
            {
            	public void run()
            	{
            		while(true)
            		{            			
            			try 
            			{
            				synchronized(tcaForAdd)//�������������ڼ���
            	            {
            					synchronized(tca)//������ǰ���ӵļ���
            					{
            						for(TexCube tc:tcaForAdd)
                	                {
                	            		tca.add(tc);  //�����Ӽ������������
                	                }
            					}            	            	
            	            	tcaForAdd.clear();		//�������ӵļ������
            	            }           
            				//��ʼģ��
                			dynamicsWorld.stepSimulation(TIME_STEP, MAX_SUB_STEPS);
							Thread.sleep(20);	//��ǰ�߳�˯��20����
						} catch (Exception e) 
						{
							e.printStackTrace();
						}
            		}
            	}
            }.start();					//�����߳�
        }
    }
	public void initShaders(){
		
	}
	
	//�����¼��ص�����
    @Override public boolean onTouchEvent(MotionEvent e) 
    {
        switch (e.getAction()) 
        {
           case MotionEvent.ACTION_DOWN:			//������Ļ�����µ��¼�
        	TexCube tcTemp=new TexCube				//����һ������������
   			(
   					this,							//MySurfaceView������
       				Constant.UNIT_SIZE,				//�ߴ�
       				boxShape,						//��ײ��״
       				dynamicsWorld,					//��������
       				1,								//��������
       				0,								//��ʼx����
       				2,         						//��ʼy���� 
       				4,								//��ʼz����
       				ShaderManager.getTextureShaderProgram()//��ɫ����������
       		);        
        	//�������ӵĳ�ʼ�ٶ�
        	tcTemp.body.setLinearVelocity(new Vector3f(0,2,-12));//����ֱ���˶����ٶ�--Vx,Vy,Vz��������
        	tcTemp.body.setAngularVelocity(new Vector3f(0,0,0)); //����������ת���ٶ�--�����������x,y,x������ת���ٶ�
        	//������������뵽�б���
        	synchronized(tcaForAdd)//��������
            {
        	   tcaForAdd.add(tcTemp);//�������
            }
           break;
        }
        return true;
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
		int textureId=textures[0];    
		GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER,GLES30.GL_NEAREST);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MAG_FILTER,GLES30.GL_LINEAR);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_CLAMP_TO_EDGE);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_CLAMP_TO_EDGE);
        
        //ͨ������������ͼƬ===============begin===================
        InputStream is = this.getResources().openRawResource(drawableId);
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
        
        //ʵ�ʼ�������
        GLUtils.texImage2D
        (
        		GLES30.GL_TEXTURE_2D,   //�������ͣ���OpenGL ES�б���ΪGL10.GL_TEXTURE_2D
        		0, 					  //����Ĳ�Σ�0��ʾ����ͼ��㣬�������Ϊֱ����ͼ
        		bitmapTmp, 			  //����ͼ��
        		0					  //����߿�ߴ�
        );
        bitmapTmp.recycle(); 		  //������سɹ����ͷ�ͼƬ
        
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
		int textureId=textures[0];    
		GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER,GLES30.GL_NEAREST);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MAG_FILTER,GLES30.GL_LINEAR);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_REPEAT);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_REPEAT);
        
        //ͨ������������ͼƬ===============begin===================
        InputStream is = this.getResources().openRawResource(drawableId);
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
        
        //ʵ�ʼ�������
        GLUtils.texImage2D
        (
        		GLES30.GL_TEXTURE_2D,   //�������ͣ���OpenGL ES�б���ΪGL10.GL_TEXTURE_2D
        		0, 					  //����Ĳ�Σ�0��ʾ����ͼ��㣬�������Ϊֱ����ͼ
        		bitmapTmp, 			  //����ͼ��
        		0					  //����߿�ߴ�
        );
        bitmapTmp.recycle(); 		  //������سɹ����ͷ�ͼƬ
        
        return textureId;
	}
}
