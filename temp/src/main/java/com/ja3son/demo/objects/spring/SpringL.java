package com.ja3son.demo.objects.spring;


import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

/*
 * Բ���Ǽ���
 */
public class SpringL 
{	
	int mProgram;//�Զ�����Ⱦ������ɫ������id
    int muMVPMatrixHandle;//�ܱ任��������
    int maPositionHandle; //����λ����������
    int maColorHandle; //������ɫ�������� 
    
    String mVertexShader;//������ɫ��    	 
    String mFragmentShader;//ƬԪ��ɫ��
	
	FloatBuffer   mVertexBuffer;//�����������ݻ���
	FloatBuffer   mColorBuffer;	//������ɫ���ݻ���
	
    int vCount=0;   
    float xAngle=0;//��x����ת�ĽǶ�
    float yAngle=0;//��y����ת�ĽǶ�
    float zAngle=0;//��z����ת�ĽǶ�
    float h;
    
    public SpringL(MySurfaceView mv,
    		float rBig, float rSmall,
    		float h, float nCirclef, //�߶ȣ�Ȧ��
    		int nCol ,int nRow)
    {
    	this.h=h;
    	//���ó�ʼ���������ݵ�initVertexData����
    	initVertexData(rBig,rSmall,h,nCirclef,nCol,nRow);
    	//���ó�ʼ����ɫ����intShader����
    	initShader(mv);
    }
    
    //�Զ���ĳ�ʼ���������ݵķ���
    public void initVertexData(
			float rBig, float rSmall,//��뾶��С�뾶
			float h, float nCirclef, //�߶ȣ�Ȧ��
			int nCol ,int nRow) {//����������
		//��Ա������ʼ��
		float angdegTotal=nCirclef*360.0f;//�ܶ���
		float angdegColSpan=360.0f/nCol;
		float angdegRowSpan=angdegTotal/nRow;
		float A=(rBig-rSmall)/2;//������ת��СԲ�뾶
		float D=rSmall+A;//��ת�켣�γɵĴ�Բ�ܰ뾶
		vCount=3*nCol*nRow*2;//�������������nColumn*nRow*2�������Σ�ÿ�������ζ�����������
		//�������ݳ�ʼ��
		ArrayList<Float> alVertix=new ArrayList<Float>();//ԭ�����б�δ���ƣ�
		ArrayList<Integer> alFaceIndex=new ArrayList<Integer>();//��֯����Ķ��������ֵ�б�����ʱ����ƣ�
		
		//����
		for(float angdegCol=0;Math.ceil(angdegCol)<360+angdegColSpan;angdegCol+=angdegColSpan)
		{
			double a=Math.toRadians(angdegCol);//��ǰСԲ�ܻ���
			for(float angdegRow=0;Math.ceil(angdegRow)<angdegTotal+angdegRowSpan;angdegRow+=angdegRowSpan)//�ظ���һ�ж��㣬�����������ļ���
			{
				float yVec=(angdegRow/angdegTotal)*h;//������ת�Ƕ�����y��ֵ
				double u=Math.toRadians(angdegRow);//��ǰ��Բ�ܻ���
				float y=(float) (A*Math.cos(a));
				float x=(float) ((D+A*Math.sin(a))*Math.sin(u));
				float z=(float) ((D+A*Math.sin(a))*Math.cos(u));
				//�����������XYZ��������Ŷ��������ArrayList
        		alVertix.add(x); alVertix.add(y+yVec); alVertix.add(z);
			}
		}
		//����
		for(int i=0;i<nCol;i++){
			for(int j=0;j<nRow;j++){
				int index=i*(nRow+1)+j;//��ǰ����
				//��������
				alFaceIndex.add(index+1);//��һ��---1
				alFaceIndex.add(index+nRow+1);//��һ��---2
				alFaceIndex.add(index+nRow+2);//��һ����һ��---3
				
				alFaceIndex.add(index+1);//��һ��---1
				alFaceIndex.add(index);//��ǰ---0
				alFaceIndex.add(index+nRow+1);//��һ��---2
			}
		}
		//������ƶ����ƽ��������
		float[] vertices=new float[vCount*3];
		cullVertex(alVertix, alFaceIndex, vertices);
		
		//�����������ݳ�ʼ��
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);//���������������ݻ���
        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mVertexBuffer = vbb.asFloatBuffer();//ת��Ϊfloat�ͻ���
        mVertexBuffer.put(vertices);//�򻺳����з��붥����������
        mVertexBuffer.position(0);//���û�������ʼλ��
        
        float[] colors=new float[vCount*4];//������ɫ����
		int Count=0;
		for(int i=0;i<vCount;i++)
		{
			colors[Count++]=1;	//r
			colors[Count++]=1;	//g
			colors[Count++]=1;	//b
			colors[Count++]=1;	//a
			
		}
        //����������ɫ���ݻ���
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
        cbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��Ϊ���ز���ϵͳ˳��
        mColorBuffer = cbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mColorBuffer.put(colors);//�򻺳����з��붥����ɫ����
        mColorBuffer.position(0);//���û�������ʼλ��
	}
    
	//ͨ��ԭ������������ֵ���õ��ö�����Ƶ�����
	public static void cullVertex(
			ArrayList<Float> alv,//ԭ�����б�δ���ƣ�
			ArrayList<Integer> alFaceIndex,//��֯����Ķ��������ֵ�б�����ʱ����ƣ�
			float[] vertices//�ö�����Ƶ����飨����������������У����鳤��Ӧ���������б��ȵ�3����
		){
		//���ɶ��������
		int vCount=0;
		for(int i:alFaceIndex){
			vertices[vCount++]=alv.get(3*i);
			vertices[vCount++]=alv.get(3*i+1);
			vertices[vCount++]=alv.get(3*i+2);
		}
	}
    //��ʼ����ɫ��
    public void initShader(MySurfaceView mv)
    {
    	//���ض�����ɫ���Ľű�����
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex_color.sh", mv.getResources());
        //����ƬԪ��ɫ���Ľű�����
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag_color.sh", mv.getResources());  
        //���ڶ�����ɫ����ƬԪ��ɫ����������
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //��ȡ�����ж���λ����������id  
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //��ȡ�����ж�����ɫ��������id  
        maColorHandle= GLES30.glGetAttribLocation(mProgram, "aColor");
        //��ȡ�������ܱ任��������id
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");  
    }
    
    public void drawSelf()
    {     
   	 	MatrixState.rotate(xAngle, 1, 0, 0);
   	 	MatrixState.rotate(yAngle, 0, 1, 0);
   	 	MatrixState.rotate(zAngle, 0, 0, 1);
   	 	
		MatrixState.pushMatrix();
    	MatrixState.translate(0, -h/2, 0);
   	 	
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
         
         //���ö���λ������
         GLES30.glEnableVertexAttribArray(maPositionHandle);
         //���ö�����ɫ����
         GLES30.glEnableVertexAttribArray(maColorHandle);  
         
         //���������Ĵ�ϸ
         GLES30.glLineWidth(2);
         //����
         GLES30.glDrawArrays(GLES30.GL_LINES, 0, vCount); 
         
         MatrixState.popMatrix();
         
    }
}
