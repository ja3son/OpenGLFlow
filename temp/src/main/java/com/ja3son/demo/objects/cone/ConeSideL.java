package com.ja3son.demo.objects.cone;

import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

//Բ׶����Ǽ���
public class ConeSideL 
{	
	int mProgram;//�Զ�����Ⱦ������ɫ������id
    int muMVPMatrixHandle;//�ܱ任��������
    int maPositionHandle; //����λ���������� 
    int maColorHandle; //������ɫ�������� 
    
    int muMMatrixHandle;//λ�á���ת�����ű任����
    int maCameraHandle; //�����λ����������
    int maNormalHandle; //���㷨������������
    int maLightLocationHandle;//��Դλ���������� 
    
    
    String mVertexShader;//������ɫ������ű�   	 
    String mFragmentShader;//ƬԪ��ɫ������ű�
	
	FloatBuffer   mVertexBuffer;//�����������ݻ���
	FloatBuffer   mColorBuffer;	//������ɫ���ݻ���
	FloatBuffer   mNormalBuffer;//���㷨�������ݻ���
    int vCount=0;   
    float xAngle=0;//��x����ת�ĽǶ�
    float yAngle=0;//��y����ת�ĽǶ�
    float zAngle=0;//��z����ת�ĽǶ�
    
    public ConeSideL(MySurfaceView mv,float scale,float r,float h,int n)
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
		float[] vertices=new float[vCount*3];
		float[] colors=new float[vCount*4];//������ɫֵ����
		float[] normals=new float[vertices.length];//����������
		//�������ݳ�ʼ��
		int count=0;
		int colorCount=0;
		int norCount=0;
		for(float angdeg=0;Math.ceil(angdeg)<360;angdeg+=angdegSpan)//����
		{
			double angrad=Math.toRadians(angdeg);//��ǰ����
			double angradNext=Math.toRadians(angdeg+angdegSpan);//��һ����
			//���ĵ�
			vertices[count++]=0; 
			vertices[count++]=h; 
			vertices[count++]=0;
			
			colors[colorCount++]=1;
			colors[colorCount++]=1;
			colors[colorCount++]=1;
			colors[colorCount++]=1;
			//��ǰ��
			vertices[count++]=(float) (-r*Math.sin(angrad));
			vertices[count++]=0;
			vertices[count++]=(float) (-r*Math.cos(angrad));
			
			colors[colorCount++]=1;
			colors[colorCount++]=1;
			colors[colorCount++]=1;
			colors[colorCount++]=1;
			//��һ��
			vertices[count++]=(float) (-r*Math.sin(angradNext));
			vertices[count++]=0;
			vertices[count++]=(float) (-r*Math.cos(angradNext));
			
			colors[colorCount++]=1;
			colors[colorCount++]=1;
			colors[colorCount++]=1;
			colors[colorCount++]=1;
		}
		//���������ݵĳ�ʼ��
		for(int i=0;i<vertices.length;i=i+3)
		{
			//�����ǰ�Ķ���ΪԲ׶����ߵ�
			if(vertices[i]==0&&vertices[i+1]==h&&vertices[i+2]==0){
				normals[norCount++]=0;
				normals[norCount++]=1;
				normals[norCount++]=0;
			}else{//��ǰ�Ķ���Ϊ����Բ�ϵĶ���
				float [] norXYZ=VectorUtil.calConeNormal(//ͨ�������������������
						0, 0, 0, 						//����Բ�����ĵ�
						vertices[i], vertices[i+1], vertices[i+2], //��ǰ�Ķ�������
						0, h, 0);						//��������(Բ׶��ߵ�)
				normals[norCount++]=norXYZ[0];
				normals[norCount++]=norXYZ[1];
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
        //������ɫ���ݳ�ʼ��
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);//���������������ݻ���
        cbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��Ϊ���ز���ϵͳ˳��
        mColorBuffer = cbb.asFloatBuffer();//ת��Ϊfloat�ͻ���
        mColorBuffer.put(colors);//�򻺳����з��붥����������
        mColorBuffer.position(0);//���û�������ʼλ��
    }

    //��ʼ����ɫ��
    public void initShader(MySurfaceView mv)
    {
    	//���ض�����ɫ���Ľű�����
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex_color_light.sh", mv.getResources());
        //����ƬԪ��ɫ���Ľű�����
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag_color_light.sh", mv.getResources());  
        //���ڶ�����ɫ����ƬԪ��ɫ����������
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //��ȡ�����ж���λ����������id  
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //��ȡ�����ж�����ɫ��������id  
        maColorHandle= GLES30.glGetAttribLocation(mProgram, "aColor");
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
    
    public void drawSelf()
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
         //���Ͷ�����������
         GLES30.glVertexAttribPointer  
         (
        		maColorHandle, 
         		4, 
         		GLES30.GL_FLOAT, 
         		false,
                4*4,   
                mColorBuffer
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
         
         //������λ����������
         GLES30.glEnableVertexAttribArray(maPositionHandle);
         //��������ɫ��������
         GLES30.glEnableVertexAttribArray(maColorHandle);  
         //�����㷨������������
         GLES30.glEnableVertexAttribArray(maNormalHandle);
         //���������Ĵ�ϸ
         GLES30.glLineWidth(2);
         //����
         GLES30.glDrawArrays(GLES30.GL_LINES, 0, vCount); 
    }
}
