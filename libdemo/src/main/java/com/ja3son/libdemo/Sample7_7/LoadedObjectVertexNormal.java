package com.ja3son.libdemo.Sample7_7;

import android.opengl.GLES30;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.TriangleIndexVertexArray;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.Point2PointConstraint;
import com.bulletphysics.extras.gimpact.GImpactMeshShape;
import com.bulletphysics.linearmath.Transform;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

//���غ�����塪����Я��������Ϣ����ɫ���
public class LoadedObjectVertexNormal 
{
	int mProgram;//�Զ�����Ⱦ���߳���id 
    int muMVPMatrixHandle;//�ܱ任��������id   
    int muMMatrixHandle;//λ�á���ת�任����
    int maCameraHandle; //�����λ����������id  
    int maPositionHandle; //����λ����������id  
    int muColorHandle;
    int maNormalHandle; //���㷨������������  
    int maLightLocationHandle;//��Դλ����������  
    
    String mVertexShader;//������ɫ��    	 
    String mFragmentShader;//ƬԪ��ɫ��
	
	private FloatBuffer   mVertexBuffer;//�����������ݻ���
    private FloatBuffer[]   mColorBuffer=new FloatBuffer[2];//������ɫ���ݻ���
    private FloatBuffer mNormalBuffer;
    int vCount=0;
    CollisionShape loadShape;
    float[] vertices;
    float[] normals;
    MySurfaceView mv;
    //==========ʰȡ================
	float midX;//���ĵ�����
	float midY;
	float midZ;
	RigidBody body;
    Point2PointConstraint p2p;  
    boolean isPicked=false;
    AABB3 preBox;//����任֮ǰ�İ�Χ��
    float[] m = new float[16];//����任�ľ���  
	float[] color=new float[]{1,1,1,1};//������ɫ
    public LoadedObjectVertexNormal(MySurfaceView mv,float[] vertices,float[] normals) 
    {
    	this.mv=mv;
    	this.vertices=vertices;
    	this.normals=normals;
    	//�����������ݵĳ�ʼ��================begin============================
        vCount=vertices.length/3; 
        
        //��ʼ����Χ��
    	preBox = new AABB3(vertices);
    	
        //���������������ݻ���
        //vertices.length*4����Ϊһ�������ĸ��ֽ�
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mVertexBuffer = vbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mVertexBuffer.put(vertices);//�򻺳����з��붥����������
        mVertexBuffer.position(0);//���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
        //�����������ݵĳ�ʼ��================end============================
        
        //������ɫ���ݵĳ�ʼ��================begin============================
        float colors[]=new float[vCount*4];//������ɫֵ���飬ÿ������4��ɫ��ֵRGBA
        for(int i=0;i<vCount;i++)
        {
        	colors[i*4]=1;//(float)(1*Math.random()); 	
        	colors[i*4+1]=0;//(float)(1*Math.random()); 
        	colors[i*4+2]=0;//(float)(1*Math.random()); 
        	colors[i*4+3]=1; 
        };
        
        //����������ɫ���ݻ���
        //vertices.length*4����Ϊһ��int�������ĸ��ֽ�
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
        cbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mColorBuffer[0] = cbb.asFloatBuffer();//ת��Ϊint�ͻ���
        mColorBuffer[0].put(colors);//�򻺳����з��붥����ɫ����
        mColorBuffer[0].position(0);//���û�������ʼλ��
        
        for(int i=0;i<vCount;i++)
        {
        	colors[i*4]=color[0]; 	
        	colors[i*4+1]=color[1]; 
        	colors[i*4+2]=color[2]; 
        	colors[i*4+3]=color[3]; 
        };
        
        //����������ɫ���ݻ���
        //vertices.length*4����Ϊһ��int�������ĸ��ֽ�
        cbb = ByteBuffer.allocateDirect(colors.length*4);
        cbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mColorBuffer[1] = cbb.asFloatBuffer();//ת��Ϊint�ͻ���
        mColorBuffer[1].put(colors);//�򻺳����з��붥����ɫ����
        mColorBuffer[1].position(0);//���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
        //������ɫ���ݵĳ�ʼ��================end============================
      //���㷨�������ݵĳ�ʼ��================begin============================  
        ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length*4);
        nbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mNormalBuffer = nbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mNormalBuffer.put(normals);//�򻺳����з��붥�㷨��������
        mNormalBuffer.position(0);//���û�������ʼλ��
        
        //���嶥�㻺��
    	ByteBuffer gVertices=ByteBuffer.allocateDirect(vCount*3*4).order(ByteOrder.nativeOrder()); 
    	for(int i=0;i<vertices.length;i++)
    	{
    		gVertices.putFloat(i*4,vertices[i]);
    	} 
    	gVertices.position(0);
    	//������������
    	ByteBuffer gIndices=ByteBuffer.allocateDirect(vCount*4).order(ByteOrder.nativeOrder());
    	for(int i=0;i<vCount;i++)
    	{
    		gIndices.putInt(i);
    	}
    	gIndices.position(0);
    	//������ײ��������
    	int vertStride = 4*3;
		int indexStride = 4*3;
    	TriangleIndexVertexArray indexVertexArrays= 
		new TriangleIndexVertexArray
		(
			vCount/3,
			gIndices,
			indexStride,
			vCount, 
			gVertices, 
			vertStride
		);
    	//����������״    	
    	GImpactMeshShape trimesh = new GImpactMeshShape(indexVertexArrays);   
    	trimesh.updateBound();
    	loadShape =trimesh;
    	intShader(mv);
    }
    
    //��ʼ��shader
    public void intShader(MySurfaceView mv)
    {
        //���ڶ�����ɫ����ƬԪ��ɫ����������
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex_color.sh", mv.getResources());
        ShaderUtil.checkGlError("==ss==");   
        //����ƬԪ��ɫ���Ľű�����
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag_color.sh", mv.getResources());  
        ShaderUtil.checkGlError("==ss==");      
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //��ȡ�����ж���λ����������id  
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //��ȡ�������ܱ任��������id 
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
        //��ȡλ�á���ת�任��������
        muMMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMMatrix"); 
        muColorHandle=GLES30.glGetUniformLocation(mProgram, "aColor");
        //��ȡ�����ж��㷨������������  
        maNormalHandle= GLES30.glGetAttribLocation(mProgram, "aNormal");
        //��ȡ�����й�Դλ������
        maLightLocationHandle=GLES30.glGetUniformLocation(mProgram, "uLightLocation");
        //��ȡ�����������λ������
        maCameraHandle=GLES30.glGetUniformLocation(mProgram, "uCamera");
    }

    public void drawSelf(RigidBody body) 
    {
    	this.body=body;
    	MatrixState.pushMatrix();
		Transform trans = body.getMotionState().getWorldTransform(new Transform());
		MatrixState.translate(trans.origin.x,trans.origin.y, trans.origin.z);
		Quat4f ro=trans.getRotation(new Quat4f());
		if(ro.x!=0||ro.y!=0||ro.z!=0)
		{
			float[] fa=SYSUtil.fromSYStoAXYZ(ro);
			if(!Float.isInfinite(fa[0])&&!Float.isInfinite(fa[1])&&!Float.isInfinite(fa[2])&&
					!Float.isNaN(fa[0])&&!Float.isNaN(fa[1])&&!Float.isNaN(fa[2])){
				MatrixState.rotate(fa[0],fa[1],fa[2],fa[3]);
			}
    	}
		
		copyM();
    	 //�ƶ�ʹ��ĳ��shader����
    	 GLES30.glUseProgram(mProgram);
         //�����ձ任������shader����
         GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
         //��λ�á���ת�任��������ɫ������
         GLES30.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0); 
         //����Դλ�ô�����ɫ������   
         GLES30.glUniform3fv(maLightLocationHandle, 1, MatrixState.lightPositionFB);
         //�������λ�ô�����ɫ������   
         GLES30.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);
         GLES30.glUniform4fv(muColorHandle, 1, color, 0);
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
       //�����㷨�������ݴ�����Ⱦ����
         GLES30.glVertexAttribPointer 
         (
        		maNormalHandle, 
         		3,   
         		GLES30.GL_FLOAT,
         		false,
                3*4,   
                mNormalBuffer
         );
         //������λ����������
         GLES30.glEnableVertexAttribArray(maPositionHandle);  
         GLES30.glEnableVertexAttribArray(maNormalHandle);  
           
         //����������
         GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount); 
         MatrixState.popMatrix();
    }
    
    public LoadedObjectVertexNormal clone(){
    	return new LoadedObjectVertexNormal(mv,vertices,normals); 
    }
    
    //������ɫ�ķ���
    public void changeColor(boolean flag)
    {
    	if(body!=null && !body.isActive()&&!isPicked){
    		color=new float[]//����ɫ
    		{
    			0,1,1
    		};  
    	}else{
    		if(flag){
        		color=new float[]{//��ɫ
        			0,1,0,1
        		};
        	}
        	else{
        		color=new float[]{//��ɫ
        			1,1,1,1
        		};
        	}
    	}
    }
    public void addPickedConstraint(){
    	//���������϶��ĵ�Ե�ؽڣ��˹ؽ�λ���ڱ��϶���������ϵ��ԭ��
    	p2p = new Point2PointConstraint(body, new Vector3f(0,0,0));
    	mv.dynamicsWorld.addConstraint(p2p, true);//���ؽ���ӽ���������
    	this.isPicked=true;//�������ѡ�б�־����Ϊtrue
    	
    }
    public void removePickedConstraint(){    	
    	if(p2p!=null){//���������϶��Ĺؽ�
    		mv.dynamicsWorld.removeConstraint(p2p);//���ؽڴ�����������ɾ��
    	}
    	this.isPicked=false;//�������ѡ�б�־����Ϊfalse
    }
	//������ĵ�λ�úͳ���ߵķ���
    public AABB3 getCurrBox(){
    	return preBox.setToTransformedBox(m);//��ȡ�任��İ�Χ��
    
    }
    //���Ʊ任����
    public void copyM(){
    	for(int i=0;i<16;i++){
    		m[i]=MatrixState.getMMatrix()[i];
    	}
    }
}
