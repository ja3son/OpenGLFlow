package com.ja3son.libdemo.Sample7_8;

import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.vehicle.RaycastVehicle;
import com.bulletphysics.linearmath.Transform;
import com.ja3son.libdemo.Sample7_8.util.LoadedObjectVertexNormalTexture;
import com.ja3son.libdemo.Sample7_8.util.MatrixState;
import com.ja3son.libdemo.Sample7_8.util.SYSUtil;

import java.nio.FloatBuffer;

import javax.vecmath.Quat4f;

public class VehicleDraw {
	RaycastVehicle vehicle;//��ͨ���߶�������
	LoadedObjectVertexNormalTexture vBox;
	LoadedObjectVertexNormalTexture vWheel;
	int vehicleTextureId;
	int mProgram;//�Զ�����Ⱦ������ɫ������id
    int muMVPMatrixHandle;//�ܱ任��������id
    int maPositionHandle; //����λ����������id  
    int maColorHandle; //��������������������id 
    
    FloatBuffer   mVertexBuffer;//�����������ݻ���
	FloatBuffer   mColorBuffer;//���������������ݻ���	
	int vCount;//�������	
	
	public VehicleDraw(
			RaycastVehicle vehicle,//��Ҫ���ƵĽ�ͨ����
			LoadedObjectVertexNormalTexture vBox,//��Obj���صĳ���ģ�� 
			LoadedObjectVertexNormalTexture vWheel, //��Obj���صĳ���ģ��
			int vehicleTextureId)//��������Id
	{
		this.vehicle = vehicle;//��ʼ�������ƽ�ͨ��������
		this.vBox = vBox;//��ʼ������ģ������
		this.vWheel = vWheel;//��ʼ������ģ������
		this.vehicleTextureId = vehicleTextureId;//��������Id
	}
	
	//���Ʒ���
	void drawSelf(){//���������ķ���
		RigidBody vBody = vehicle.getRigidBody();			//�õ���ǰ�����ĸ���
		MatrixState.pushMatrix();							//�����ֳ�
		//���������ǰ��ƽ������
		Transform trans = vBody.getMotionState().getWorldTransform(new Transform());
		//����ƽ�Ʊ任
		MatrixState.translate(trans.origin.x,trans.origin.y, trans.origin.z);
		//System.out.println("////"+trans.origin.y);
		//��ȡ��ǰ�任����ת����
		Quat4f ro=trans.getRotation(new Quat4f());			
		if(ro.x!=0||ro.y!=0||ro.z!=0){
			float[] fa= SYSUtil.fromSYStoAXYZ(ro);			//����Ԫ��ת����AXYZ����ʽ
			MatrixState.rotate(fa[0], fa[1], fa[2], fa[3]);			//������ת�任
		}
		//���Ƴ������
		vBox.drawSelf(vehicleTextureId);//���Ƴ���
		MatrixState.popMatrix();							//�ָ��ֳ�
		for (int i = 0; i < vehicle.getNumWheels(); i++) {//��������
			vehicle.updateWheelTransform(i, true);			//���³��ֵı任
			MatrixState.pushMatrix();//�����ֳ�
			trans = vehicle.getWheelInfo(i).worldTransform;	//��ȡ���ֵ�ǰ�任����ת����
			//����ƽ�Ʊ任
			MatrixState.translate(trans.origin.x,trans.origin.y,trans.origin.z);
			ro=trans.getRotation(new Quat4f());				//��ȡ��ǰ�任����ת��Ϣ
			if(ro.x!=0||ro.y!=0||ro.z!=0){
				float[] fa=SYSUtil.fromSYStoAXYZ(ro);		//����Ԫ��ת����AXYZ����ʽ
				MatrixState.rotate(fa[0], fa[1], fa[2], fa[3]);		//������ת�任
			}
			vWheel.drawSelf(vehicleTextureId);   			//��������
			MatrixState.popMatrix();						//�ָ��ֳ�
	}}}
