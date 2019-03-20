package com.ja3son.demo.objects.bezier;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES30;
/*
 * ����ƽ����
 */
public class Texture 
{	
	int mProgram;//�Զ�����Ⱦ������ɫ������id
    int muMVPMatrixHandle;//�ܱ任��������
    int maPositionHandle; //����λ����������
    int maTexCoorHandle; //��������������������
    
    String mVertexShader;//������ɫ������ű�  	 
    String mFragmentShader;//ƬԪ��ɫ������ű�
	
	FloatBuffer   mVertexBuffer;//�����������ݻ���
	FloatBuffer   mTexCoorBuffer;//���������������ݻ���

    int vCount=0;   
    float xAngle=0;//��x����ת�ĽǶ�
    float yAngle=0;//��y����ת�ĽǶ�
    float zAngle=0;//��z����ת�ĽǶ�
    
    public Texture(MySurfaceView mv,float scale,float a, float b, int nS, int nT)
    {
    	//���ó�ʼ���������ݵ�initVertexData����
    	initVertexData(scale,a,b,nS,nT);
    	//���ó�ʼ����ɫ����intShader����
    	initShader(mv);
    }
    
    //�Զ���ĳ�ʼ���������ݵķ���
    public void initVertexData(float scale,float a, float b, int nS, int nT) {
    	
		a*=scale;
		b*=scale;
		float xOffset=a/2;
		float yOffset=b/2;
		vCount=6;
		//�������ݳ�ʼ��
		float[] vertices=new float[]{
			-xOffset,-yOffset,0,
			xOffset,yOffset,0,
			-xOffset,yOffset,0,
        	
        	-xOffset,-yOffset,0,
        	xOffset,-yOffset,0,
        	xOffset,yOffset,0
		};
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);//���������������ݻ���
        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mVertexBuffer = vbb.asFloatBuffer();//ת��Ϊfloat�ͻ���
        mVertexBuffer.put(vertices);//�򻺳����з��붥����������
        mVertexBuffer.position(0);//���û�������ʼλ��
        
        float[] textures={
        		0,1, 1,0, 0,0,
        		0,1, 1,1, 1,0
        };
        //����������ɫ���ݻ���
        ByteBuffer cbb = ByteBuffer.allocateDirect(textures.length*4);
        cbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��Ϊ���ز���ϵͳ˳��
        mTexCoorBuffer = cbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mTexCoorBuffer.put(textures);//�򻺳����з��붥����ɫ����
        mTexCoorBuffer.position(0);//���û�������ʼλ��
	}

    //�Զ����ʼ����ɫ��initShader����
    public void initShader(MySurfaceView mv)
    {
    	//���ض�����ɫ���Ľű�����
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex_tex.sh", mv.getResources());
        //����ƬԪ��ɫ���Ľű�����
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag_tex.sh", mv.getResources());  
        //���ڶ�����ɫ����ƬԪ��ɫ����������
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //��ȡ�����ж���λ����������id  
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //��ȡ�����ж�������������������id  
        maTexCoorHandle= GLES30.glGetAttribLocation(mProgram, "aTexCoor");
        //��ȡ�������ܱ任��������id
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix"); 
    }
    
    public void drawSelf(int texId)
    {
    	 MatrixState.rotate(xAngle, 1, 0, 0);
    	 MatrixState.rotate(yAngle, 0, 1, 0);
    	 MatrixState.rotate(zAngle, 0, 0, 1);
    	
    	 //�ƶ�ʹ��ĳ��shader����
    	 GLES30.glUseProgram(mProgram);        
         
         //�����ձ任������shader����
         GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
         
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
         
         //���ö���λ������
         GLES30.glEnableVertexAttribArray(maPositionHandle);
         //���ö�����������
         GLES30.glEnableVertexAttribArray(maTexCoorHandle);  

         //������
         GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
         GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);
         
         //�����������
         GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount); 
    }
}
