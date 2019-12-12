package com.ja3son.libdemo.Sample7_3;
import android.opengl.GLES30;

import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.shapes.BvhTriangleMeshShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.TriangleIndexVertexArray;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.vecmath.Vector3f;

public class LandForm {
	
	int mProgram;//�Զ�����Ⱦ���߳���id 
    int muMVPMatrixHandle;//�ܱ任��������id   
    int muMMatrixHandle;//λ�á���ת�任����
    int uTexHandle;//���������������id
    
    int maCameraHandle; //�����λ����������id  
    int maPositionHandle; //����λ����������id  
    int maTexCoorHandle; //��������������������id  
    
    String mVertexShader;//������ɫ��    	 
    String mFragmentShader;//ƬԪ��ɫ��
	
	private FloatBuffer   mVertexBuffer;//�����������ݻ���
    private FloatBuffer   mTextureBuffer;//������ɫ���ݻ���
    
    int vCount;
    float yOffset;
    
    public LandForm(MySurfaceView mv,final float UNIT_SIZE, float yOffset,
    		DiscreteDynamicsWorld dynamicsWorld,
    		float[][] yArray,int rows,int cols,int mProgram)
    {	
    	this.mProgram=mProgram;//������ɫ����������
		//�����������ݵĳ�ʼ��================begin============================
        vCount=cols*rows*2*3;//ÿ���������������Σ�ÿ��������3������        
        float vertices[]=new float[vCount*3];//ÿ������xyz��������
        int count=0;//���������
        for(int j=0;j<rows;j++)
        {
        	for(int i=0;i<cols;i++)
        	{        		
        		//���㵱ǰ�������ϲ������ 
        		float zsx=-UNIT_SIZE*cols/2+i*UNIT_SIZE;
        		float zsz=-UNIT_SIZE*rows/2+j*UNIT_SIZE;        		
        		
        		vertices[count++]=zsx;
        		vertices[count++]=yArray[j][i]+yOffset;
        		vertices[count++]=zsz;
        		
        		vertices[count++]=zsx;
        		vertices[count++]=yArray[j+1][i]+yOffset;
        		vertices[count++]=zsz+UNIT_SIZE;
        		
        		vertices[count++]=zsx+UNIT_SIZE;
        		vertices[count++]=yArray[j][i+1]+yOffset;
        		vertices[count++]=zsz;
        		
        		vertices[count++]=zsx+UNIT_SIZE;
        		vertices[count++]=yArray[j][i+1]+yOffset;
        		vertices[count++]=zsz;
        		
        		vertices[count++]=zsx;
        		vertices[count++]=yArray[j+1][i]+yOffset;
        		vertices[count++]=zsz+UNIT_SIZE;
        		
        		vertices[count++]=zsx+UNIT_SIZE;
        		vertices[count++]=yArray[j+1][i+1]+yOffset;
        		vertices[count++]=zsz+UNIT_SIZE;
        	}
        }
		
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
    	//�Զ������������飬20��15��
    	float textures[]=generateTexCoor(cols,rows);
        
        //���������������ݻ���
        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length*4);
        tbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mTextureBuffer= tbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mTextureBuffer.put(textures);//�򻺳����з��붥����ɫ����
        mTextureBuffer.position(0);//���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
        //�����������ݵĳ�ʼ��================end============================
        
        //����Ϊ��͹������ײ��״����Ķ������ݻ���
    	ByteBuffer gVertices=ByteBuffer.allocateDirect(vCount*3*4).order(ByteOrder.nativeOrder()); 
    	for(int i=0;i<vertices.length;i++)
    	{
    		gVertices.putFloat(i*4,vertices[i]);//������������ӵ�������
    	} 
    	gVertices.position(0);//���û�������ʼλ��
    	//����Ϊ��͹������ײ��״����Ķ�����������
    	ByteBuffer gIndices=ByteBuffer.allocateDirect(vCount*4).order(ByteOrder.nativeOrder());
    	for(int i=0;i<vCount;i++)
    	{
    		gIndices.putInt(i);//������������ӵ�������
    	}
    	gIndices.position(0); //���û�������ʼλ��
    	//������ײ��������
    	int vertStride = 4*3;//�������ݼ��
		int indexStride = 4*3;//�������ݼ��
		//�����������鶥����������
    	TriangleIndexVertexArray indexVertexArrays=
		new TriangleIndexVertexArray
		(
			vCount/3,//�����εĸ���
			gIndices,//��������
			indexStride,//�������ݼ��
			vCount, //�������
			gVertices, //���㻺��
			vertStride//�������ݼ��
		);
    	//������͹���ζ�Ӧ����ײ��״
    	CollisionShape groundShape=new BvhTriangleMeshShape(indexVertexArrays,true,true);
        
        //��������ĳ�ʼ�任����
		Transform groundTransform = new Transform();
		groundTransform.setIdentity();//��ʼ���任
		groundTransform.origin.set(new Vector3f(0.f, 0.f, 0.f));//���ø��壨��͹���Σ��ĳ�ʼλ��		
		Vector3f localInertia = new Vector3f(0, 0, 0);//������Ź��Ե�����		
		//����������˶�״̬����
		DefaultMotionState myMotionState = new DefaultMotionState(groundTransform);
		//��������������Ϣ����
		RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(0, myMotionState, groundShape, localInertia);
		//��������
		RigidBody body = new RigidBody(rbInfo);
		//���÷���ϵ��
		body.setRestitution(0.4f);
		//����Ħ��ϵ��
		body.setFriction(0.8f);
		body.setCollisionFlags(body.getCollisionFlags() & ~CollisionFlags.KINEMATIC_OBJECT);
		body.forceActivationState(CollisionObject.ACTIVE_TAG);
		//��������ӽ���������
		dynamicsWorld.addRigidBody(body);
		//��ʼ����ɫ������
		initShader(mv);
    }

  //��ʼ��shader
    public void initShader(MySurfaceView mv)
    {
        //��ȡ�����ж���λ����������id  
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //��ȡ�����ж��㾭γ����������id   
        maTexCoorHandle=GLES30.glGetAttribLocation(mProgram, "aTexCoor");  
        //��ȡ�������ܱ任��������id 
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");   
        uTexHandle=GLES30.glGetUniformLocation(mProgram, "sTexture"); 
    }
    
    public void drawSelf(int texId) 
    {        
    	 //�ƶ�ʹ��ĳ��shader����
    	 GLES30.glUseProgram(mProgram);
         //�����ձ任������shader����
         GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
         
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
    
    //�Զ��з����������������ķ���
    public float[] generateTexCoor(int bw,int bh)
    {
    	float[] result=new float[bw*bh*6*2]; 
    	float sizew=16.0f/bw;//����
    	float sizeh=16.0f/bh;//����
    	int c=0;
    	for(int i=0;i<bh;i++)
    	{
    		for(int j=0;j<bw;j++)
    		{
    			//ÿ����һ�����Σ������������ι��ɣ��������㣬12����������
    			float s=j*sizew;
    			float t=i*sizeh;
    			
    			result[c++]=s;
    			result[c++]=t;
    			
    			result[c++]=s;
    			result[c++]=t+sizeh;
    			
    			result[c++]=s+sizew;
    			result[c++]=t;
    			
    			
    			result[c++]=s+sizew;
    			result[c++]=t;
    			
    			result[c++]=s;
    			result[c++]=t+sizeh;
    			
    			result[c++]=s+sizew;
    			result[c++]=t+sizeh;    			
    		}
    	}
    	return result;
    }
}
