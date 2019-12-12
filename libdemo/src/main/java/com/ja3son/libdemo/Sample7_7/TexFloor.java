package com.ja3son.libdemo.Sample7_7;
import android.opengl.GLES30;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.vecmath.Vector3f;

public class TexFloor {
	
	int mProgram;//�Զ�����Ⱦ���߳���id 
    int muMVPMatrixHandle;//�ܱ任��������id   
    int muMMatrixHandle;//λ�á���ת�任����
    int maTexCoorHandle; //��������������������id  
    int uTexHandle;//���������������id
    int maCameraHandle; //�����λ����������id  
    int maPositionHandle; //����λ����������id  
    
    String mVertexShader;//������ɫ��    	 
    String mFragmentShader;//ƬԪ��ɫ��
	
	private FloatBuffer   mVertexBuffer;//�����������ݻ���
    private FloatBuffer   mTextureBuffer;//������ɫ���ݻ���
    
    int vCount;
    float yOffset;
    
    public TexFloor(int mProgram,final float UNIT_SIZE, float yOffset,CollisionShape groundShape,DiscreteDynamicsWorld dynamicsWorld)
    {
    	this.mProgram=mProgram;
    	this.yOffset=yOffset;
		//��������ĳ�ʼ�任����
		Transform groundTransform = new Transform();
		groundTransform.setIdentity();
		groundTransform.origin.set(new Vector3f(0.f, yOffset, 0.f));		
		Vector3f localInertia = new Vector3f(0, 0, 0);//����		
		//����������˶�״̬����
		DefaultMotionState myMotionState = new DefaultMotionState(groundTransform);
		//����������Ϣ����
		RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(0, myMotionState, groundShape, localInertia);
		//��������
		RigidBody body = new RigidBody(rbInfo);
		//���÷���ϵ��
		body.setRestitution(0.4f);
		//����Ħ��ϵ��
		body.setFriction(0.8f);
		//��������ӽ���������
		dynamicsWorld.addRigidBody(body);
		initVertexData(UNIT_SIZE);
		intShader(mProgram);
    }

    public void initVertexData(final float UNIT_SIZE){
    	//�����������ݵĳ�ʼ��================begin============================
        vCount=6;
        float vertices[]=new float[]
        {        	
        	1*UNIT_SIZE,yOffset,1*UNIT_SIZE,
        	-1*UNIT_SIZE,yOffset,-1*UNIT_SIZE,
        	-1*UNIT_SIZE,yOffset,1*UNIT_SIZE,
        	
        	1*UNIT_SIZE,yOffset,1*UNIT_SIZE,
        	1*UNIT_SIZE,yOffset,-1*UNIT_SIZE,
        	-1*UNIT_SIZE,yOffset,-1*UNIT_SIZE,        	
        };
		
        //���������������ݻ���
        //vertices.length*4����Ϊһ�������ĸ��ֽ�
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mVertexBuffer = vbb.asFloatBuffer();//ת��Ϊint�ͻ���
        mVertexBuffer.put(vertices);//�򻺳����з��붥����������
        mVertexBuffer.position(0);//���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
        //�����������ݵĳ�ʼ��================end============================
        //�����������ݵĳ�ʼ��================begin============================
        float textures[]=new float[]
        {
        	UNIT_SIZE/2,UNIT_SIZE/2,  0,0,  0,UNIT_SIZE/2,
        	UNIT_SIZE/2,UNIT_SIZE/2,  UNIT_SIZE/2,0,  0,0
        };

        //���������������ݻ���
        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length*4);
        tbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mTextureBuffer= tbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mTextureBuffer.put(textures);//�򻺳����з��붥����ɫ����
        mTextureBuffer.position(0);//���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
        //�����������ݵĳ�ʼ��================end============================
    }
  //��ʼ��shader
    public void intShader(int mProgram)
    {
        //��ȡ�����ж���λ����������id  
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //��ȡ�����ж��㾭γ����������id   
        maTexCoorHandle=GLES30.glGetAttribLocation(mProgram, "aTexCoor");  
        //��ȡ�������ܱ任��������id 
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");   
        //��ȡλ�á���ת�任��������id
        muMMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMMatrix");  
        //��ȡ�����������λ������id
        maCameraHandle=GLES30.glGetUniformLocation(mProgram, "uCamera"); 
        uTexHandle=GLES30.glGetUniformLocation(mProgram, "sTexture"); 
    }
    
    public void drawSelf(int texId) 
    {        
    	 //�ƶ�ʹ��ĳ��shader����
    	 GLES30.glUseProgram(mProgram);
         //�����ձ任������shader����
         GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
       //��λ�á���ת�任������shader����
         GLES30.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);  
         //�������λ�ô���shader����   
         GLES30.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);
         
         //Ϊ����ָ������λ������    
         GLES30.glVertexAttribPointer        
         (
         		maPositionHandle,   
         		3, 
         		GLES30.GL_FLOAT, 
         		false,
                3*4, 
                mVertexBuffer   
         );       
         //Ϊ����ָ������������������
         GLES30.glVertexAttribPointer  
         (  
        		maTexCoorHandle,  
         		2, 
         		GLES30.GL_FLOAT, 
         		false,
                2*4,   
                mTextureBuffer
         );   
         //������λ����������
         GLES30.glEnableVertexAttribArray(maPositionHandle);  
         GLES30.glEnableVertexAttribArray(maTexCoorHandle);  
         //������
         GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
         GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);    
         GLES30.glUniform1i(uTexHandle, 0);
           
         //����������
         GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount); 
    }
}
