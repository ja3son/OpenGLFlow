package com.ja3son.demo.objects.c60;


import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

/*
 * ����������֮���Բ��
 */
public class Stick 
{	
	int mProgram;//�Զ�����Ⱦ������ɫ������id
    int muMVPMatrixHandle;//�ܱ任��������
    int maPositionHandle; //����λ����������
    int maColorHandle; //������ɫ�������� 
    int muMMatrixHandle;
    
    int maCameraHandle; //�����λ����������
    int maNormalHandle; //���㷨������������
    int maLightLocationHandle;//��Դλ���������� 
    
    
    String mVertexShader;//������ɫ��    	 
    String mFragmentShader;//ƬԪ��ɫ��
	
	FloatBuffer   mVertexBuffer;//�����������ݻ���
	FloatBuffer   mColorBuffer;	//������ɫ���ݻ���
	FloatBuffer   mNormalBuffer;//���㷨�������ݻ���
    int vCount=0;   
    
	float length=10f;//Բ������
	float circle_radius=2f;//Բ�ػ��뾶
	float degreespan=18f;  //Բ�ػ�ÿһ�ݵĶ�����С
    
    public Stick(MySurfaceView mv,float length,float circle_radius,float degreespan,float[] colorValue)
    {
    	//���ó�ʼ���������ݵ�initVertexData����
    	initVertexData( length, circle_radius, degreespan,colorValue);
    	//���ó�ʼ����ɫ����intShader����
    	initShader(mv);
    }
	//��һ��������񻯵ķ���
	public static float[] normalizeVector(float x, float y, float z){
		float mod=module(x,y,z);
		mod=(mod==0)?1:mod;
		return new float[]{x/mod, y/mod, z/mod};//���ع�񻯺������
	}
	//��������ģ�ķ���
	public static float module(float x, float y, float z){
		return (float) Math.sqrt(x*x+y*y+z*z);
	}
    //�Զ���ĳ�ʼ���������ݵķ���
    @SuppressWarnings("static-access")
	public void initVertexData(float length,float circle_radius,float degreespan,float[] colorValue)
    {
    	//�����������ݵĳ�ʼ��
		ArrayList<Float> val=new ArrayList<Float>();//�������б�
		ArrayList<Float> ial=new ArrayList<Float>();//����������б�
    	
    	this.length = length;
    	this.circle_radius = circle_radius;
    	this.degreespan = degreespan;
    	
    	for(float circle_degree=360.0f;circle_degree>0.0f;circle_degree-=degreespan)//ѭ����
		{
				float x1 =(float)(-length/2);
				float y1=(float) (circle_radius*Math.sin(Math.toRadians(circle_degree)));
				float z1=(float) (circle_radius*Math.cos(Math.toRadians(circle_degree)));
				
				float a1=0;
				float b1=y1;
				float c1=z1;
				//�������
				float[] result=this.normalizeVector(a1,b1,c1);
				a1=result[0];
				b1=result[1];
				c1=result[2];
				
				float x2 =(float)(-length/2);
				float y2=(float) (circle_radius*Math.sin(Math.toRadians(circle_degree-degreespan)));
				float z2=(float) (circle_radius*Math.cos(Math.toRadians(circle_degree-degreespan)));
				
				float a2=0;
				float b2=y2;
				float c2=z2;
				//�������
				result=this.normalizeVector(a2,b2,c2);
				a2=result[0];
				b2=result[1];
				c2=result[2];
				
				float x3 =(float)(length/2);
				float y3=(float) (circle_radius*Math.sin(Math.toRadians(circle_degree-degreespan)));
				float z3=(float) (circle_radius*Math.cos(Math.toRadians(circle_degree-degreespan)));
				
				float a3=0;
				float b3=y3;
				float c3=z3;
				//�������
				result=this.normalizeVector(a3,b3,c3);
				a3=result[0];
				b3=result[1];
				c3=result[2];
				
				float x4 =(float)(length/2);
				float y4=(float) (circle_radius*Math.sin(Math.toRadians(circle_degree)));
				float z4=(float) (circle_radius*Math.cos(Math.toRadians(circle_degree)));
				
				float a4=0;
				float b4=y4;
				float c4=z4;
				//�������
				result=this.normalizeVector(a4,b4,c4);
				a4=result[0];
				b4=result[1];
				c4=result[2];
				
				val.add(x1);val.add(y1);val.add(z1);//���������Σ���6�����������
				val.add(x2);val.add(y2);val.add(z2);
				val.add(x4);val.add(y4);val.add(z4);
				
				val.add(x2);val.add(y2);val.add(z2);
				val.add(x3);val.add(y3);val.add(z3);
				val.add(x4);val.add(y4);val.add(z4);
				
				ial.add(a1);ial.add(b1);ial.add(c1);//�����Ӧ�ķ�����
				ial.add(a2);ial.add(b2);ial.add(c2);
				ial.add(a4);ial.add(b4);ial.add(c4);
				
				ial.add(a2);ial.add(b2);ial.add(c2);
				ial.add(a3);ial.add(b3);ial.add(c3);
				ial.add(a4);ial.add(b4);ial.add(c4);
		}   
    	
    	vCount=val.size()/3;//���������Ϊ����ֵ������1/3����Ϊһ��������3������
    	
        //��alVertix�е�����ֵת�浽һ��float������
        float vertices[]=new float[vCount*3];
        for(int i=0;i<val.size();i++)
    	{
    		vertices[i]=val.get(i);
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
        
        //��alVertix�е�����ֵת�浽һ��float������
        float normals[]=new float[ial.size()];
    	for(int i=0;i<ial.size();i++)
    	{
    		normals[i]=ial.get(i);
    	}
		
        //���������������ݻ���
        //vertices.length*4����Ϊһ�������ĸ��ֽ�
        ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length*4);
        nbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mNormalBuffer = nbb.asFloatBuffer();//ת��Ϊint�ͻ���
        mNormalBuffer.put(normals);//�򻺳����з��붥����������
        mNormalBuffer.position(0);//���û�������ʼλ��
        
        //������ɫ���ݵĳ�ʼ��
        float colors[]=new float[vCount*4];
        for(int i=0;i<vCount;i++){
        	colors[4*i]=colorValue[0];
        	colors[4*i+1]=colorValue[1];
        	colors[4*i+2]=colorValue[2];
        	colors[4*i+3]=colorValue[3];
        }
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
        cbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mColorBuffer = cbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mColorBuffer.put(colors);//�򻺳����з��붥����ɫ����
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
         //���Ͷ�����ɫ����
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
         
         //���ö���λ������
         GLES30.glEnableVertexAttribArray(maPositionHandle);
         //���ö�����ɫ����
         GLES30.glEnableVertexAttribArray(maColorHandle);  
         //���ö��㷨��������
         GLES30.glEnableVertexAttribArray(maNormalHandle);
         
         //���������Ĵ�ϸ
         GLES30.glLineWidth(2);
         //����
         GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount); 
    }
}
