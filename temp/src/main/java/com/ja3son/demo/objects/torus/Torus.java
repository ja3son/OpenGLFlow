package com.ja3son.demo.objects.torus;


import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

/*
 * Բ��
 */
public class Torus 
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
    
    public Torus(MySurfaceView mv,float rBig, float rSmall,int nCol ,int nRow)
    {
    	//���ó�ʼ���������ݵ�initVertexData����
    	initVertexData(rBig,rSmall,nCol,nRow);
    	//���ó�ʼ����ɫ����intShader����
    	initShader(mv);
    }
    
    //�Զ���ĳ�ʼ���������ݵķ���
    public void initVertexData(
			float rBig, float rSmall,//Բ���⾶���ھ�
			int nCol ,int nRow) {//СԲ�ܺʹ�Բ���зֵķ���
	
		float angdegColSpan=360.0f/nCol;//СԲ��ÿ�ݵĽǶȿ��
		float angdegRowSpan=360.0f/nRow;//��Բ��ÿ�ݵĽǶȿ��
		float A=(rBig-rSmall)/2;//������ת��СԲ�뾶
		float D=rSmall+A;//��ת�켣�γɵĴ�Բ�ܰ뾶
		vCount=3*nCol*nRow*2;//�������
		
		ArrayList<Float> alVertix=new ArrayList<Float>();//ԭʼ�����б�δ���ƣ�
		ArrayList<Integer> alFaceIndex=new ArrayList<Integer>();//������֯��������Ķ������б�		
		
		for(float angdegCol=0;Math.ceil(angdegCol)<360+angdegColSpan;
		angdegCol+=angdegColSpan)	{//��СԲ���յȽǶȼ��ѭ��
			double a=Math.toRadians(angdegCol);//��ǰСԲ����
			for(float angdegRow=0;Math.ceil(angdegRow)<360+angdegRowSpan;angdegRow+=angdegRowSpan)
			{//�Դ�Բ���յȽǶȼ��ѭ��
				double u=Math.toRadians(angdegRow);//��ǰ��Բ����
				float y=(float) (A*Math.cos(a));//���չ�ʽ���㵱ǰ����
				float x=(float) ((D+A*Math.sin(a))*Math.sin(u));	//��X��Y��Z����
				float z=(float) ((D+A*Math.sin(a))*Math.cos(u));
				//�����������X��Y��Z�������ԭʼ�����б�
        		alVertix.add(x); alVertix.add(y); alVertix.add(z);
			}
		}				
		
		for(int i=0;i<nCol;i++){//���վ��Ƴ������ε���Ҫ
			for(int j=0;j<nRow;j++){//���ɶ������б�
				int index=i*(nRow+1)+j;//��ǰ�ı��ε�һ������
			
				alFaceIndex.add(index+1);//��һ����������������ı�����б�
				alFaceIndex.add(index+nRow+1);
				alFaceIndex.add(index+nRow+2);
				
				alFaceIndex.add(index+1);//�ڶ�����������������ı�����б�
				alFaceIndex.add(index);
				alFaceIndex.add(index+nRow+1);
			}
		}
		float[] vertices=new float[vCount*3];//��Ű��վ���˳�򶥵�����ֵ������
		
		cullVertex(alVertix, alFaceIndex, vertices);//���ɾ��ƺ�Ķ�����������ֵ
		
		ArrayList<Float> alST=new ArrayList<Float>();//ԭ���������б�δ���ƣ�
		for(float angdegCol=0;Math.ceil(angdegCol)<360+angdegColSpan;angdegCol+=angdegColSpan)
		{//��СԲ���յȽǶȼ��ѭ��
			float t=angdegCol/360;//��ǰ�Ƕȶ�Ӧ��t����
			for(float angdegRow=0;Math.ceil(angdegRow)<360+angdegRowSpan;angdegRow+=angdegRowSpan)//�ظ���һ���������꣬�������ļ���
			{//�Դ�Բ���յȽǶȼ��ѭ��
				float s=angdegRow/360;//��ǰ�Ƕȶ�Ӧ��s����
				
				alST.add(s); alST.add(t);//����ԭʼ���������б�
			}
		}
		
		float[] textures=cullTexCoor(alST, alFaceIndex);//���ɾ��ƺ�������������ֵ
		
		//�����������ݳ�ʼ��
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);//���������������ݻ���
        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��Ϊ���ز���ϵͳ˳��
        mVertexBuffer = vbb.asFloatBuffer();//ת��Ϊfloat�ͻ���
        mVertexBuffer.put(vertices);//�򻺳����з��붥����������
        mVertexBuffer.position(0);//���û�������ʼλ��

        //st�������ݳ�ʼ��		
        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length*4);//���������������ݻ���
        tbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��Ϊ���ز���ϵͳ˳��
        mTexCoorBuffer = tbb.asFloatBuffer();//ת��Ϊfloat�ͻ���
        mTexCoorBuffer.put(textures);//�򻺳����з��붥����������
        mTexCoorBuffer.position(0);//���û�������ʼλ��
	}
    
	public static void cullVertex(//���ݶ��������ɾ��ƺ󶥵���������ķ���
			ArrayList<Float> alv,//ԭʼ�����б�
			ArrayList<Integer> alFaceIndex,//������֯��������Ķ������б�
			float[] vertices//��ž��ƺ󶥵�����ֵ������
		){
		int vCount=0;//���������
		for(int i:alFaceIndex){//�Զ������б����ѭ��
			vertices[vCount++]=alv.get(3*i);//����ǰ��Ŷ����X����ֵ������������
			vertices[vCount++]=alv.get(3*i+1);//����ǰ��Ŷ����Y����ֵ������������
			vertices[vCount++]=alv.get(3*i+2);//����ǰ��Ŷ����Z����ֵ������������
		}
	}
	
	public static float[] cullTexCoor(//���ݶ��������ɾ��ƺ󶥵�������������ķ���
			ArrayList<Float> alST,//ԭʼ���������б�
			ArrayList<Integer> alTexIndex//������֯��������Ķ������б�
			)
	{
		float[] textures=new float[alTexIndex.size()*2];//���������������
		
		int stCount=0;//�������������
		for(int i:alTexIndex){//�Զ������б����ѭ��
			textures[stCount++]=alST.get(2*i);//����ǰ��Ŷ����S����ֵ������������
			textures[stCount++]=alST.get(2*i+1);//����ǰ��Ŷ����T����ֵ������������
		}
		return textures;//���ؽ��������������
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
