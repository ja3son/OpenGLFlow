package com.ja3son.libdemo.Sample7_7;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

import javax.vecmath.Vector3f;

public class AddRigidBody {
	
	RigidBody body;

	 public AddRigidBody(CollisionShape shape,float mass,DiscreteDynamicsWorld dynamicsWorld,
			 float restitution,float friction,Vector3f origin){    	
	    	boolean isDynamic = (mass!=0);
			Vector3f localInertia = new Vector3f(0,0,0);
			if(isDynamic){
				shape.calculateLocalInertia(mass, localInertia);
			}
	    	//��������ĳ�ʼ�任����
			Transform groundTransform = new Transform();
			groundTransform.setIdentity();
			groundTransform.origin.set(origin);		
			//����������˶�״̬����
			DefaultMotionState myMotionState = new DefaultMotionState(groundTransform);
			//����������Ϣ����
			RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(mass, myMotionState, shape, localInertia);
			//��������
			body = new RigidBody(rbInfo);
			//���÷���ϵ��
			body.setRestitution(restitution);
			//����Ħ��ϵ��
			body.setFriction(friction);
			//��������ӽ���������
			dynamicsWorld.addRigidBody(body);
	    } 
}
