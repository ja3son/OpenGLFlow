package com.ja3son.libdemo.Sample7_8;

import android.opengl.GLES30;

import com.ja3son.libdemo.Sample7_8.util.MatrixState;
import com.ja3son.libdemo.Sample7_8.util.ShaderManager;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


public class TextFloor {
	
	private int muMVPMatrixHandle;
	private int maTexCoorHandle;
	private int maPositionHandle;
	private FloatBuffer mVertexBuffer;
	private FloatBuffer mTextureBuffer;
    
	private int vCount;
	private float yOffset;
	private int mProgram;
    private MySurfaceView mv;

	public TextFloor(int unit_size, float yOffset,
			MySurfaceView gameSurfaceView) {
		this.yOffset=yOffset;
		this.mv = gameSurfaceView;
	initVertexData(unit_size);						//��ʼ��������������ɫ����
	initShader();								//��ʼ��shader
}
//��ʼ��������������ɫ���ݵķ���
private void initVertexData(final float unit_size) {
	vCount = 6;
	float vertices[] = new float[]{					//��������
		1 * unit_size, yOffset, 1 * unit_size,
		-1 * unit_size, yOffset, -1 * unit_size, 
		-1 * unit_size,yOffset, 1 * unit_size,
		
		1 * unit_size, yOffset, 1 * unit_size, 
		1 * unit_size, yOffset,-1 * unit_size,
		-1 * unit_size, yOffset, -1 * unit_size, };
	//���������������ݻ���
	ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
	vbb.order(ByteOrder.nativeOrder());				//�����ֽ�˳��
	mVertexBuffer = vbb.asFloatBuffer();				//ת��Ϊint�ͻ���
	mVertexBuffer.put(vertices);					//�򻺳����з��붥����������
	mVertexBuffer.position(0);						//���û�������ʼλ��
	float textures[] = new float[]{					//��������
		unit_size/2, unit_size/2,
		0,0, 
		0,unit_size/2,
		
		unit_size/2, unit_size/2,
		unit_size/2,0,
		0,0
	};
	//���������������ݻ���
	ByteBuffer tbb =ByteBuffer
	.allocateDirect(textures.length * 4);
	tbb.order(ByteOrder.nativeOrder());				//�����ֽ�˳��
	mTextureBuffer = tbb.asFloatBuffer();			//ת��Ϊint�ͻ���
	mTextureBuffer.put(textures);					//�򻺳����з��붥����ɫ����
	mTextureBuffer.position(0);					//���û�������ʼλ��
}
//��ʼ����ɫ��
private void initShader()
{
	 //���ض�����ɫ���Ľű�����
	 String mVertexShader=ShaderManager.loadFromAssetsFile("vertex.sh", 
													mv.getResources());
	 //����ƬԪ��ɫ���Ľű�����
	 String mFragmentShader=ShaderManager.loadFromAssetsFile("frag.sh",  
													  mv.getResources());  
	 //���ڶ�����ɫ����ƬԪ��ɫ����������
	 mProgram = ShaderManager.createProgram(mVertexShader,mFragmentShader);
	 //��ȡ�����ж���λ����������id
	 maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
	 //��ȡ�����ж�������������������id
	 maTexCoorHandle=GLES30.glGetAttribLocation(mProgram, "aTexCoor");  
	 //��ȡ�������ܱ任��������id
	 muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");

}
//���Ʒ���
public void drawSelf(int texId){        
  	 //�ƶ�ʹ��ĳ��shader���� 
  	 GLES30.glUseProgram(mProgram);
  	 //�����ձ任������shader���� 
  	 GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,
  						   MatrixState.getFinalMatrix(), 0);

  	 //Ϊ����ָ������λ������
    GLES30.glVertexAttribPointer(maPositionHandle,
      		                  3,GLES30.GL_FLOAT,false, 3*4,mVertexBuffer);       
    //Ϊ����ָ������������������
    GLES30.glVertexAttribPointer(maTexCoorHandle,2,
      						GLES30.GL_FLOAT,false, 2*4,mTextureBuffer);   
    //���ö���λ�á�������������
    GLES30.glEnableVertexAttribArray(maPositionHandle);  
    GLES30.glEnableVertexAttribArray(maTexCoorHandle);  
    //������
    GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
    GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);     

    //�����������
    GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount); 
  }
}
