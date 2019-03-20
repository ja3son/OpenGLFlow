package com.ja3son.demo.objects.cone;


import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

//Բ׶����
public class ConeSide 
{	
	int mProgram;//�Զ�����Ⱦ������ɫ������id
    int muMVPMatrixHandle;//�ܱ任��������
    int maPositionHandle; //����λ���������� 
    int maTexCoorHandle; //��������������������
    
    int muMMatrixHandle;//λ�á���ת�����ű任����
    int maCameraHandle; //�����λ����������
    int maNormalHandle; //���㷨������������
    int maLightLocationHandle;//��Դλ���������� 
    
    String mVertexShader;//������ɫ������ű�	 
    String mFragmentShader;//ƬԪ��ɫ������ű�
	
	FloatBuffer   mVertexBuffer;//�����������ݻ���
	FloatBuffer   mTexCoorBuffer;//���������������ݻ���
	FloatBuffer   mNormalBuffer;//���㷨�������ݻ���
    int vCount=0;   
    float xAngle=0;//��x����ת�ĽǶ�
    float yAngle=0;//��y����ת�ĽǶ�
    float zAngle=0;//��z����ת�ĽǶ�
    
    public ConeSide(MySurfaceView mv,float scale,float r,float h,int n)
    {    	
    	//���ó�ʼ���������ݵ�initVertexData����
    	initVertexData(scale,r,h,n);
    	//���ó�ʼ����ɫ����intShader����   
    	initShader(mv);
    }
    
    //�Զ����ʼ�������������ݵķ���
    public void initVertexData(
    		float scale,	//�ߴ��С
    		float r,	//�뾶
    		float h,	//�߶�
    		int n		//�зֵķ���
    	)
    {
    	r=scale*r;
    	h=scale*h;
		float angdegSpan=360.0f/n;
		vCount=3*n*4;//�������������3*n*4�������Σ�ÿ�������ζ�����������
		//�������ݳ�ʼ��
		float[] vertices=new float[vCount*3];//������������
		float[] textures=new float[vCount*2];//������������ֵ����
		float[] normals=new float[vertices.length];//����������
		//�������ݳ�ʼ��
		int count=0;//�������������
		int stCount=0;//�������������
		int norCount=0;//������������
		for(float angdeg=0;Math.ceil(angdeg)<360;angdeg+=angdegSpan)
		{//����һ���ĽǶȿ���з�
			double angrad=Math.toRadians(angdeg);//��ǰ����
			double angradNext=Math.toRadians(angdeg+angdegSpan);//��һ����
			 //Բ׶��������ߵ�����
			vertices[count++]=0; 
			vertices[count++]=h; 
			vertices[count++]=0;
			//Բ׶��������ߵ���������
			textures[stCount++]=0.5f;
			textures[stCount++]=0;
			
			vertices[count++]=(float) (-r*Math.sin(angrad));//��ǰ���ȶ�Ӧ������
			vertices[count++]=0;
			vertices[count++]=(float) (-r*Math.cos(angrad));
			
			textures[stCount++]=(float) (angrad/(2*Math.PI));//��ǰ���ȶ�Ӧ����������
			textures[stCount++]=1;

			
			vertices[count++]=(float) (-r*Math.sin(angradNext));//��һ���ȶ�Ӧ������
			vertices[count++]=0;
			vertices[count++]=(float) (-r*Math.cos(angradNext));
			
			textures[stCount++]=(float) (angradNext/(2*Math.PI));//��һ���ȶ�Ӧ����������
			textures[stCount++]=1;
		}
		
		for(int i=0;i<vertices.length;i=i+3)
		{//���������ݵĳ�ʼ��
			//�����ǰ�Ķ���ΪԲ׶����ߵ�
			if(vertices[i]==0&&vertices[i+1]==h&&vertices[i+2]==0){//�����ǰ�Ķ���ΪԲ׶��������ߵ�
				normals[norCount++]=0;
				normals[norCount++]=1;
				normals[norCount++]=0;
			}else{//��ǰ�Ķ���Ϊ����Բ���ϵĵ�
				float [] norXYZ=VectorUtil.calConeNormal(//ͨ��3��������������������
						0, 0, 0, 						//����Բ�����ĵ�
						vertices[i], vertices[i+1], vertices[i+2], //��ǰ����Բ�ܶ�������
						0, h, 0);						//Բ׶������ߵ�����
				normals[norCount++]=norXYZ[0];//��������X��Y��Z 3��������ֵ
				normals[norCount++]=norXYZ[1];//���뷨������������
				normals[norCount++]=norXYZ[2];
			}
		}
		
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);//���������������ݻ���
        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��Ϊ���ز���ϵͳ˳��
        mVertexBuffer = vbb.asFloatBuffer();//ת��Ϊfloat�ͻ���
        mVertexBuffer.put(vertices);//�򻺳����з��붥����������
        mVertexBuffer.position(0);//���û�������ʼλ��
        //���������ݳ�ʼ�� 
        ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length*4);//�������㷨�������ݻ���
        nbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��Ϊ���ز���ϵͳ˳��
        mNormalBuffer = nbb.asFloatBuffer();//ת��Ϊfloat�ͻ���
        mNormalBuffer.put(normals);//�򻺳����з��붥�㷨��������
        mNormalBuffer.position(0);//���û�������ʼλ��
        //st�������ݳ�ʼ��
        ByteBuffer cbb = ByteBuffer.allocateDirect(textures.length*4);//���������������ݻ���
        cbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��Ϊ���ز���ϵͳ˳��
        mTexCoorBuffer = cbb.asFloatBuffer();//ת��Ϊfloat�ͻ���
        mTexCoorBuffer.put(textures);//�򻺳����з��붥����������
        mTexCoorBuffer.position(0);//���û�������ʼλ��
    }

    //�Զ����ʼ����ɫ����initShader����
    public void initShader(MySurfaceView mv)
    {
    	//���ض�����ɫ���Ľű�����
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex_tex_light.sh", mv.getResources());
        //����ƬԪ��ɫ���Ľű�����
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag_tex_light.sh", mv.getResources());  
        //���ڶ�����ɫ����ƬԪ��ɫ����������
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //��ȡ�����ж���λ����������id  
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //��ȡ�����ж�������������������id  
        maTexCoorHandle= GLES30.glGetAttribLocation(mProgram, "aTexCoor");
        //��ȡ�������ܱ任��������id
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
        
        //��ȡ�����ж��㷨������������id  
        maNormalHandle= GLES30.glGetAttribLocation(mProgram, "aNormal"); 
        //��ȡ�����������λ������id
        maCameraHandle=GLES30.glGetUniformLocation(mProgram, "uCamera"); 
        //��ȡ�����й�Դλ������id
        maLightLocationHandle=GLES30.glGetUniformLocation(mProgram, "uLightLocation"); 
        //��ȡλ�á���ת�任��������id
        muMMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMMatrix");  
        
        
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
         //����Դλ�ô���shader����   
         GLES30.glUniform3fv(maLightLocationHandle, 1, MatrixState.lightPositionFB);
         
         //���Ͷ���λ������
         GLES30.glVertexAttribPointer  
         (
         		maPositionHandle,   
         		3, 
         		GLES30.GL_FLOAT, 
         		false,
                3*4,   
                mVertexBuffer
         );       
         //���Ͷ���������������
         GLES30.glVertexAttribPointer  
         (
        		maTexCoorHandle, 
         		2, 
         		GLES30.GL_FLOAT, 
         		false,
                2*4,   
                mTexCoorBuffer
         ); 
         //���Ͷ��㷨��������
         GLES30.glVertexAttribPointer  
         (
        		maNormalHandle, 
         		4, 
         		GLES30.GL_FLOAT, 
         		false,
                3*4,   
                mNormalBuffer
         ); 
         
         //���ö���λ������
         GLES30.glEnableVertexAttribArray(maPositionHandle);
         //���ö�����������
         GLES30.glEnableVertexAttribArray(maTexCoorHandle);  
         //���ö��㷨��������
         GLES30.glEnableVertexAttribArray(maNormalHandle);
         //������
         GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
         GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);
         
         //�����������
         GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount); 
    }
}
