package com.ja3son.libdemo.Sample7_3;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
public class TexCube 
{
	TextureRect tr;//���ڻ��Ƹ�������������
	float halfSize;//������İ�߳�
	RigidBody body;//��Ӧ�ĸ������
	int mProgram;
	MySurfaceView mv;
	public TexCube(MySurfaceView mv,float halfSize,CollisionShape colShape,
			DiscreteDynamicsWorld dynamicsWorld,float mass,float cx,float cy,float cz,int mProgram)
	{		
		boolean isDynamic = (mass != 0f);//�����Ƿ�����˶�
		Vector3f localInertia = new Vector3f(0, 0, 0);//��������
		if (isDynamic) //�����������˶�
		{
			colShape.calculateLocalInertia(mass, localInertia);//�������
		}
		Transform startTransform = new Transform();//��������ĳ�ʼ�任����
		startTransform.setIdentity();//�任��ʼ��
		startTransform.origin.set(new Vector3f(cx, cy, cz));//���ó�ʼ��λ��
		//����������˶�״̬����
		DefaultMotionState myMotionState = new DefaultMotionState(startTransform);
		//����������Ϣ����
		RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo
									(mass, myMotionState, colShape, localInertia);
		body = new RigidBody(rbInfo);//��������
		body.setRestitution(0.6f);//���÷���ϵ��
		body.setFriction(0.8f);//����Ħ��ϵ��
		dynamicsWorld.addRigidBody(body);//��������ӽ���������
		this.mv=mv;	//����MySurfaceView����
		tr=new TextureRect(halfSize);//�����������
		this.mProgram=mProgram;//������ɫ����������
		this.halfSize=halfSize;	//����볤
	}
	public void drawSelf(int[] texIda)
	{
		tr.intShader(mv, mProgram);//������γ�ʼ����ɫ��
		int texId=texIda[0];//�����˶�ʱ������id
		if(!body.isActive()){texId=texIda[1];}//���徲ֹʱ������id
		MatrixState.pushMatrix();//�����ֳ�
		//��ȡ������ӵı任��Ϣ����
		Transform trans = body.getMotionState().getWorldTransform(new Transform());
		//������λ�任
		MatrixState.translate(trans.origin.x,trans.origin.y, trans.origin.z);
		Quat4f ro=trans.getRotation(new Quat4f());//��ȡ��ǰ�任����ת��Ϣ
		if(ro.x!=0||ro.y!=0||ro.z!=0)
		{
			float[] fa=SYSUtil.fromSYStoAXYZ(ro);//����Ԫ��ת����AXYZ����ʽ
			MatrixState.rotate(fa[0], fa[1], fa[2], fa[3]);//ִ����ת
		}
		MatrixState.pushMatrix();//�����ֳ�
	    MatrixState.translate(0, halfSize, 0);//ִ��ƽ��
	    MatrixState.rotate(-90, 1, 0, 0);//ִ����ת
	    tr.drawSelf( texId);//��������
		MatrixState.popMatrix();//�ָ��ֳ�
		MatrixState.pushMatrix();//�����ֳ�
	    MatrixState.translate(0, -halfSize, 0);//ִ��ƽ��
	    MatrixState.rotate(90, 1, 0, 0);//ִ����ת
	    tr.drawSelf( texId);//��������
		MatrixState.popMatrix();//�ָ��ֳ�
		MatrixState.pushMatrix();//�����ֳ�
	    MatrixState.translate(-halfSize, 0, 0);//ִ��ƽ��
	    MatrixState.rotate(-90, 0, 1, 0);//ִ����ת
	    tr.drawSelf( texId);//��������
		MatrixState.popMatrix();//�ָ��ֳ�
		MatrixState.pushMatrix();//�����ֳ�
	    MatrixState.translate(halfSize, 0, 0);//ִ��ƽ��
	    MatrixState.rotate(90, 0, 1, 0);//ִ����ת
	    tr.drawSelf( texId);//��������
		MatrixState.popMatrix();//�ָ��ֳ�
		MatrixState.pushMatrix();//�����ֳ�
		MatrixState.translate(0, 0, halfSize);//ִ��ƽ��
	    tr.drawSelf(texId);//����ǰ��
		MatrixState.popMatrix();//�ָ��ֳ�
		MatrixState.pushMatrix();//�����ֳ�
		MatrixState.translate(0, 0, -halfSize);//ִ��ƽ��
		MatrixState.rotate(180, 0, 1, 0);//ִ����ת
	    tr.drawSelf( texId);//���ƺ���
		MatrixState.popMatrix();//�ָ��ֳ�
		
		MatrixState.popMatrix();//�ָ��ֳ�
	}
}
