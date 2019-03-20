package com.ja3son.demo.objects.bezier;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import android.opengl.GLES30;
/*
 * ̩���궥���齨1
 */
public class TopPart1 {	
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
    
    float scale;
    
    public TopPart1(MySurfaceView mv,float scale, int nCol ,int nRow)
    {
    	this.scale=scale;
    	//���ó�ʼ���������ݵ�initVertexData����
    	initVertexData(scale,nCol,nRow);
    	//���ó�ʼ����ɫ����intShader����
    	initShader(mv);
    }
    
  //��ʼ���������ݵķ���
    public void initVertexData(float scale, int nCol ,int nRow 
			){
		
		float angdegSpan=360.0f/nCol;
		vCount=3*nCol*nRow*2;//�������
		
		ArrayList<Float> alVertix=new ArrayList<Float>();//ԭ�����б�δ���ƣ�
		ArrayList<Integer> alFaceIndex=new ArrayList<Integer>();//��֯����Ķ���ı��ֵ�б�
		
		//�����Ǳ��������ߵ�ʵ�ִ���
		BezierUtil.al.clear();//��տ��Ƶ��б�

		//�������ݵ�
		BezierUtil.al.add(new BNPosition(-1, 171));//���Ƶ���������1
		BezierUtil.al.add(new BNPosition(14, 191));//���Ƶ���������2
		BezierUtil.al.add(new BNPosition(17, 183));//���Ƶ���������3
		BezierUtil.al.add(new BNPosition(5, 154));	//���Ƶ���������4	
		BezierUtil.al.add(new BNPosition(31, 274));//���Ƶ���������5
		BezierUtil.al.add(new BNPosition(32, 243));	//���Ƶ���������6	
		BezierUtil.al.add(new BNPosition(30, 230));	//���Ƶ���������7	
		BezierUtil.al.add(new BNPosition(0, 253));//���Ƶ���������8
		
		
		//���ݿ��Ƶ����ɱ����������ϵ���б�
		ArrayList<BNPosition> alCurve=BezierUtil.getBezierData(1.0f/nRow);
		
		for(int i=0;i<nRow+1;i++)
		{//���ݵõ��������ϵ���б�������ת���ϵ�������б�
			double r=alCurve.get(i).x*Constant.DATA_RATIO*scale;	//��ǰԲ�İ뾶
			float y=alCurve.get(i).y*Constant.DATA_RATIO*scale;//��ǰyֵ
			for(float angdeg=0;Math.ceil(angdeg)<360+angdegSpan;angdeg+=angdegSpan)
			{
				double angrad=Math.toRadians(angdeg);//��ǰ����
				float x=(float) (-r*Math.sin(angrad));//���㶥�������ֵ
				float z=(float) (-r*Math.cos(angrad));//���㶥�������ֵ
				
				alVertix.add(x); alVertix.add(y); alVertix.add(z);//���������ݼ��뵽�б���
			}
		}
		
		for(int i=0;i<nRow;i++){//ͨ��ѭ����������Ƴ������ε�
			for(int j=0;j<nCol;j++){//�������б�
				int index=i*(nCol+1)+j;//��ǰ�ı��ε�һ������ı��
				//��������
				alFaceIndex.add(index+1);//��һ��������1�ŵ���
				alFaceIndex.add(index+nCol+2);//��һ��������2�ŵ���
				alFaceIndex.add(index+nCol+1);//��һ��������3�ŵ���
				
				alFaceIndex.add(index+1);//�ڶ���������1�ŵ���
				alFaceIndex.add(index+nCol+1);//�ڶ���������2�ŵ���
				alFaceIndex.add(index);//�ڶ���������3�ŵ���
			}
		}
		
		float[] vertices=new float[vCount*3];//���Ƴ������εĶ�����������
		vertices=VectorUtil.calVertices(alVertix, alFaceIndex);
		
		//�����������ݳ�ʼ��
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);//���������������ݻ���
		vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
		mVertexBuffer = vbb.asFloatBuffer();//ת��Ϊfloat�ͻ���
		mVertexBuffer.put(vertices);//�򻺳����з��붥����������
		mVertexBuffer.position(0);//���û�������ʼλ��
        
		//����
		ArrayList<Float> alST=new ArrayList<Float>();//��������������б�

		float yMin=999999999;//y��Сֵ
		float yMax=0;//y���ֵ
		for(BNPosition pos:alCurve){
			yMin=Math.min(yMin, pos.y);//y��Сֵ
			yMax=Math.max(yMax, pos.y);//y���ֵ
		}
		for(int i=0;i<nRow+1;i++)
		{
			float y=alCurve.get(i).y;//��ǰyֵ
			float t=1-(y-yMin)/(yMax-yMin);//t����
			for(float angdeg=0;Math.ceil(angdeg)<360+angdegSpan;angdeg+=angdegSpan)
			{
				float s=angdeg/360;//s����
				
				alST.add(s); alST.add(t);//�����������S��T������������б�
			}
		}
		//������ƺ���������
		float[] textures=VectorUtil.calTextures(alST, alFaceIndex);
        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length*4);//���������������ݻ���
        tbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mTexCoorBuffer = tbb.asFloatBuffer();//ת��Ϊfloat�ͻ���
        mTexCoorBuffer.put(textures);//�򻺳����з��붥����������
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
