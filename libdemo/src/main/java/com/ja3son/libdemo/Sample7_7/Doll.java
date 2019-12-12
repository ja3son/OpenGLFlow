package com.ja3son.libdemo.Sample7_7;
import com.bulletphysics.BulletGlobals;
import com.bulletphysics.collision.shapes.CapsuleShape;
import com.bulletphysics.collision.shapes.CapsuleShapeX;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.Generic6DofConstraint;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

import javax.vecmath.Vector3f;
public class Doll {

	MySurfaceView mv;//������Ⱦ����������
	DynamicsWorld dynamicsWorld;//������������
	//��ײ��״���飬�����ż��������ֵĽ�����״���洢�ڴ�������
	CollisionShape[] bodyShapes= new CollisionShape[BodyPartIndex.BODYPART_COUNT.ordinal()];
	//�������飬�����ż��������ֵĸ�����洢�ڴ������У�ÿ�������Ӧһ��������״
	RigidBody[] rigidBodies = new RigidBody[BodyPartIndex.BODYPART_COUNT.ordinal()];//��������
	LoadedObjectVertexNormal[] bodyForDraws;//������Ⱦ��������ֵļ�������
	//========����ֵ===========
	float mass=1;//���������
	//=========λ��==========
	float bodyCenterH=7;//������ż���ĵĸ߶�
	//===================���ҵĳߴ�=============
	//ͷ��
	float headR=0.5f;//ͷ���������˰���İ뾶
	float headH=1.25f-2*headR;//ͷ�������м�Բ���ĳ���
	float headTH=1.25f;//ͷ�������ܳ���
	//��׵
	float spineR=0.7f;//��׵�������˰���İ뾶
	float spineH=2.0f-2*spineR;//��׵�����м�Բ���ĳ���
	float spineTH=2.0f;//��׵�����ܳ���
	//�Ҵ��
	float rightUpperArmR=0.3f;//�Ҵ�۽������˰���İ뾶
	float rightUpperArmH=2.0f-2*rightUpperArmR;//�Ҵ�۽����м�Բ���ĳ���
	float rightUpperArmTH=2.0f;//�Ҵ�۽����ܳ���
	//��С��
	float rightLowerArmR=0.3f;//��С�۽������˰���İ뾶
	float rightLowerArmH=1.7f-2*rightLowerArmR;//��С�۽����м�Բ���ĳ���
	float rightLowerArmTH=1.7f;//��С�۽����ܳ���
	//����
	float leftUpperArmR=0.3f;//���۽������˰���İ뾶
	float leftUpperArmH=2.0f-2*leftUpperArmR;//���۽����м�Բ���ĳ���
	float leftUpperArmTH=2.0f;//���۽����ܳ���
	//��С��
	float leftLowerArmR=0.3f;//��С�۽������˰���İ뾶
	float leftLowerArmH=1.7f-2*leftLowerArmR;//��С�۽����м�Բ���ĳ���
	float leftLowerArmTH=1.7f;//��С�۽����ܳ���
	//����
	float pelvisR=0.8f;//���轺�����˰���İ뾶
	float pelvisH=2.5f-pelvisR*2;//���轺���м�Բ���ĳ���
	float pelvisTH=2.5f;//���轺���ܳ���
	//�Ҵ���
	float rightUpperLegR=0.3f;//�Ҵ��Ƚ������˰���İ뾶
	float rightUpperLegH=1.7f-rightUpperLegR*2;//�Ҵ��Ƚ����м�Բ���ĳ���
	float rightUpperLegTH=1.7f;//�Ҵ��Ƚ����ܳ���
	//�����
	float leftUpperLegR=0.3f;//����Ƚ������˰���İ뾶
	float leftUpperLegH=1.7f-leftUpperLegR*2;//����Ƚ����м�Բ���ĳ���
	float leftUpperLegTH=1.7f;//����Ƚ����ܳ���
	//��С��
	float leftLowerLegR=0.3f;//��С�Ƚ������˰���İ뾶
	float leftLowerLegH=2.0f-2*leftLowerLegR;//��С�Ƚ����м�Բ���ĳ���
	float leftLowerLegTH=2.0f;//��С�Ƚ����ܳ���
	//��С��
	float rightLowerLegR=0.3f;//��С�Ƚ������˰���İ뾶
	float rightLowerLegH=2.0f-2*rightLowerLegR;//��С�Ƚ����м�Բ���ĳ���
	float rightLowerLegTH=2.0f;//��С�Ƚ����ܳ���
	
	public Doll(MySurfaceView mv,DynamicsWorld dynamicsWorld,LoadedObjectVertexNormal[] bodyForDraws){
		this.mv=mv;//���泡����Ⱦ����������
		this.dynamicsWorld=dynamicsWorld;//�����������������
		this.bodyForDraws=bodyForDraws;//����������Ⱦ��������б�����
		initShapes();//��ʼ����ײ��״
		initRigidBodys();//��ʼ������
	}
	
	public void initShapes(){//��ʼ����ײ��״�ķ���
		//����ͷ����Ӧ�Ľ�����ײ��״
		bodyShapes[BodyPartIndex.BODYPART_HEAD.ordinal()] = new CapsuleShape(headR,headH);
		//������׵��Ӧ�Ľ�����ײ��״
		bodyShapes[BodyPartIndex.BODYPART_SPINE.ordinal()] = new CapsuleShape(spineR,spineH);
		bodyShapes[BodyPartIndex.BODYPART_RIGHT_UPPER_ARM.ordinal()] = new CapsuleShapeX(rightUpperArmR,rightUpperArmH);
		bodyShapes[BodyPartIndex.BODYPART_RIGHT_LOWER_ARM.ordinal()] = new CapsuleShapeX(rightLowerArmR,rightLowerArmH);
		bodyShapes[BodyPartIndex.BODYPART_LEFT_UPPER_ARM.ordinal()] = new CapsuleShapeX(leftUpperArmR,leftUpperArmH);
		bodyShapes[BodyPartIndex.BODYPART_LEFT_LOWER_ARM.ordinal()] = new CapsuleShapeX(leftLowerArmR,leftLowerArmH);
		bodyShapes[BodyPartIndex.BODYPART_PELVIS.ordinal()] = new CapsuleShape(pelvisR,pelvisH);
		bodyShapes[BodyPartIndex.BODYPART_RIGHT_UPPER_LEG.ordinal()] = new CapsuleShape(rightUpperLegR,rightUpperLegH);
		bodyShapes[BodyPartIndex.BODYPART_LEFT_UPPER_LEG.ordinal()] = new CapsuleShape(leftUpperLegR,leftUpperLegH);
		bodyShapes[BodyPartIndex.BODYPART_LEFT_LOWER_LEG.ordinal()] = new CapsuleShape(leftLowerLegR,leftLowerLegH);
		bodyShapes[BodyPartIndex.BODYPART_RIGHT_LOWER_LEG.ordinal()] = new CapsuleShape(rightLowerLegR,rightLowerLegH);
	}
	
	public void initRigidBodys(){//��ʼ������ķ���
		//====================��������=========================
		//ͷ
		Transform tempTrans = new Transform();//��������ĳ�ʼ�任����
		tempTrans.setIdentity();//��ʼ���任����
		tempTrans.origin.set(0,bodyCenterH+spineTH+headTH/2,0);//����ͷ����y�����ƽ��
		//����ͷ������
		rigidBodies[BodyPartIndex.BODYPART_HEAD.ordinal()] = addRigidBody(mass, tempTrans, bodyShapes[BodyPartIndex.BODYPART_HEAD.ordinal()]);
		//��ʼ���任����
		tempTrans.setIdentity();
		tempTrans.origin.set(new Vector3f(0,bodyCenterH+spineTH/2,0));//���ü�׵��y�����ƽ��
		//������׵����
		rigidBodies[BodyPartIndex.BODYPART_SPINE.ordinal()] = addRigidBody(mass, tempTrans, bodyShapes[BodyPartIndex.BODYPART_SPINE.ordinal()]);
		//�Ҵ��
		tempTrans.setIdentity();
		tempTrans.origin.set(new Vector3f(spineR+rightUpperArmTH/2,bodyCenterH+spineTH-spineR,0));
		rigidBodies[BodyPartIndex.BODYPART_RIGHT_UPPER_ARM.ordinal()] = addRigidBody(mass, tempTrans, bodyShapes[BodyPartIndex.BODYPART_RIGHT_UPPER_ARM.ordinal()]);
		//��С��
		tempTrans.setIdentity();
		tempTrans.origin.set(new Vector3f(spineR+rightUpperArmTH+rightLowerArmTH/2,bodyCenterH+spineTH-spineR,0));
		rigidBodies[BodyPartIndex.BODYPART_RIGHT_LOWER_ARM.ordinal()] = addRigidBody(mass, tempTrans, bodyShapes[BodyPartIndex.BODYPART_RIGHT_LOWER_ARM.ordinal()]);
		//����
		tempTrans.setIdentity();
		tempTrans.origin.set(new Vector3f(-spineR-leftUpperArmTH/2,bodyCenterH+spineTH-spineR,0));
		rigidBodies[BodyPartIndex.BODYPART_LEFT_UPPER_ARM.ordinal()] = addRigidBody(mass, tempTrans, bodyShapes[BodyPartIndex.BODYPART_LEFT_UPPER_ARM.ordinal()]);
		//��С��
		tempTrans.setIdentity();
		tempTrans.origin.set(new Vector3f(-spineR-leftUpperArmTH-leftLowerArmTH/2,bodyCenterH+spineTH-spineR,0));
		rigidBodies[BodyPartIndex.BODYPART_LEFT_LOWER_ARM.ordinal()] = addRigidBody(mass, tempTrans, bodyShapes[BodyPartIndex.BODYPART_LEFT_LOWER_ARM.ordinal()]);
		//����
		tempTrans.setIdentity();
		tempTrans.origin.set(new Vector3f(0,bodyCenterH-pelvisTH/2,0));
		rigidBodies[BodyPartIndex.BODYPART_PELVIS.ordinal()] = addRigidBody(mass, tempTrans, bodyShapes[BodyPartIndex.BODYPART_PELVIS.ordinal()]);
		//�Ҵ���
		tempTrans.setIdentity();
		tempTrans.origin.set(new Vector3f(pelvisR+rightUpperLegR,bodyCenterH-pelvisTH-rightUpperLegH/2+pelvisR,0));
		rigidBodies[BodyPartIndex.BODYPART_RIGHT_UPPER_LEG.ordinal()] = addRigidBody(mass, tempTrans, bodyShapes[BodyPartIndex.BODYPART_RIGHT_UPPER_LEG.ordinal()]);
		//�����
		tempTrans.setIdentity();
		tempTrans.origin.set(new Vector3f(-pelvisR-leftUpperLegR,bodyCenterH-pelvisTH-rightUpperLegH/2+pelvisR,0));
		rigidBodies[BodyPartIndex.BODYPART_LEFT_UPPER_LEG.ordinal()] = addRigidBody(mass, tempTrans, bodyShapes[BodyPartIndex.BODYPART_LEFT_UPPER_LEG.ordinal()]);
		//��С��
		tempTrans.setIdentity();
		tempTrans.origin.set(new Vector3f(-pelvisR-leftUpperLegR,bodyCenterH-pelvisTH-leftUpperLegTH-leftLowerLegTH/2+pelvisR,0));
		rigidBodies[BodyPartIndex.BODYPART_LEFT_LOWER_LEG.ordinal()] = addRigidBody(mass, tempTrans, bodyShapes[BodyPartIndex.BODYPART_LEFT_LOWER_LEG.ordinal()]);
		//��С��
		tempTrans.setIdentity();
		tempTrans.origin.set(new Vector3f(pelvisR+rightUpperLegR,bodyCenterH-pelvisTH-rightUpperLegTH-rightLowerLegTH/2+pelvisR,0));
		rigidBodies[BodyPartIndex.BODYPART_RIGHT_LOWER_LEG.ordinal()] = addRigidBody(mass, tempTrans, bodyShapes[BodyPartIndex.BODYPART_RIGHT_LOWER_LEG.ordinal()]);
		//============���Լ��=============
		Generic6DofConstraint joint6DOF;//���������ɶȹؽ�����
		Transform localA = new Transform(); //�����任����A����ʾ�ӱ任������A���ĵı任��
		Transform localB = new Transform();//�����任����B����ʾ�ӱ任������B���ĵı任��
		//ͷ���ͼ�׵Լ��
		localA.setIdentity();//��ʼ���任����A
		localB.setIdentity();//��ʼ���任����B
		localA.origin.set(0f, -headTH/2, 0f);//���ñ任����A��ƽ�Ʋ���
		localB.origin.set(0,spineTH/2,0);//���ñ任����B��ƽ�Ʋ���
		//����ͷ���ͼ�׵֮��������ɶ�Լ��
		joint6DOF = new Generic6DofConstraint(rigidBodies[BodyPartIndex.BODYPART_HEAD.ordinal()], rigidBodies[BodyPartIndex.BODYPART_SPINE.ordinal()], localA, localB, true);
		Vector3f limitTrans = new Vector3f();//���ڴ洢�����ɶȹؽ���ת�Ƕ������޵�����
		//������ת�Ƕ����޵�3������
		limitTrans.set(-BulletGlobals.SIMD_PI * 0.1f, -BulletGlobals.FLT_EPSILON, -BulletGlobals.SIMD_PI * 0.1f);
		//���ùؽ�ת���Ƕȵ�����
		joint6DOF.setAngularLowerLimit(limitTrans);
		//������ת�Ƕ����޵�3������
		limitTrans.set(BulletGlobals.SIMD_PI * 0.2f, BulletGlobals.FLT_EPSILON, BulletGlobals.SIMD_PI * 0.2f);
		//���ùؽ�ת���Ƕȵ�����
		joint6DOF.setAngularUpperLimit(limitTrans);
		//��Լ����ӽ���������
		dynamicsWorld.addConstraint(joint6DOF, true);
		//�Ҵ�ۺͼ�׵
		localA.setIdentity();
		localB.setIdentity();
		localA.origin.set(spineR,spineTH/2-rightUpperArmR*2, 0f);
		localB.origin.set(-rightUpperArmTH/2,0,0);
		joint6DOF = new Generic6DofConstraint(rigidBodies[BodyPartIndex.BODYPART_SPINE.ordinal()], rigidBodies[BodyPartIndex.BODYPART_RIGHT_UPPER_ARM.ordinal()], localA, localB, true);
		limitTrans.set(-BulletGlobals.FLT_EPSILON,-BulletGlobals.SIMD_PI * 0.1f, -BulletGlobals.SIMD_PI * 0.4f);
		joint6DOF.setAngularLowerLimit(limitTrans);
		limitTrans.set(BulletGlobals.FLT_EPSILON,BulletGlobals.SIMD_PI * 0.1f,  BulletGlobals.SIMD_PI * 0.4f);
		joint6DOF.setAngularUpperLimit(limitTrans);
		dynamicsWorld.addConstraint(joint6DOF, true);
		//�Ҵ�ۺ���С��
		localA.setIdentity();
		localB.setIdentity();
		localA.origin.set(rightUpperArmTH/2,0, 0f);
		localB.origin.set(-rightLowerArmTH/2,0,0);
		joint6DOF = new Generic6DofConstraint(rigidBodies[BodyPartIndex.BODYPART_RIGHT_UPPER_ARM.ordinal()], rigidBodies[BodyPartIndex.BODYPART_RIGHT_LOWER_ARM.ordinal()], localA, localB, true);
		limitTrans.set(-BulletGlobals.FLT_EPSILON,-BulletGlobals.SIMD_PI * 0.4f,  -BulletGlobals.SIMD_PI * 0.05f);
		joint6DOF.setAngularLowerLimit(limitTrans);
		limitTrans.set(BulletGlobals.FLT_EPSILON,BulletGlobals.SIMD_PI * 0.4f,  BulletGlobals.SIMD_PI * 0.05f);
		joint6DOF.setAngularUpperLimit(limitTrans);
		dynamicsWorld.addConstraint(joint6DOF, true);
		//���ۺͼ�׵
		localA.setIdentity();
		localB.setIdentity();
		localA.origin.set(-spineR,spineTH/2-leftUpperArmR*2, 0f);
		localB.origin.set(leftUpperArmTH/2,0,0);
		joint6DOF = new Generic6DofConstraint(rigidBodies[BodyPartIndex.BODYPART_SPINE.ordinal()], rigidBodies[BodyPartIndex.BODYPART_LEFT_UPPER_ARM.ordinal()], localA, localB, true);
		limitTrans.set( -BulletGlobals.FLT_EPSILON,-BulletGlobals.SIMD_PI * 0.1f, -BulletGlobals.SIMD_PI * 0.4f);
		joint6DOF.setAngularLowerLimit(limitTrans);
		limitTrans.set( BulletGlobals.FLT_EPSILON,BulletGlobals.SIMD_PI * 0.1f, BulletGlobals.SIMD_PI * 0.4f);
		joint6DOF.setAngularUpperLimit(limitTrans);
		dynamicsWorld.addConstraint(joint6DOF, true);
		//��С�ۺ�����
		localA.setIdentity();
		localB.setIdentity();
		localA.origin.set(-leftUpperArmTH/2,0, 0f);
		localB.origin.set(leftLowerArmTH/2,0,0);
		joint6DOF = new Generic6DofConstraint(rigidBodies[BodyPartIndex.BODYPART_LEFT_UPPER_ARM.ordinal()], rigidBodies[BodyPartIndex.BODYPART_LEFT_LOWER_ARM.ordinal()], localA, localB, true);
		limitTrans.set(-BulletGlobals.FLT_EPSILON,-BulletGlobals.SIMD_PI * 0.4f, -BulletGlobals.SIMD_PI * 0.05f);
		joint6DOF.setAngularLowerLimit(limitTrans);
		limitTrans.set(BulletGlobals.FLT_EPSILON,BulletGlobals.SIMD_PI * 0.4f,  BulletGlobals.SIMD_PI * 0.05f);
		joint6DOF.setAngularUpperLimit(limitTrans);
		dynamicsWorld.addConstraint(joint6DOF, true);
		//��׵�͹���
		localA.setIdentity();
		localB.setIdentity();
		localA.origin.set(0,-spineTH/2,0f);
		localB.origin.set(0,pelvisTH/2,0);
		joint6DOF = new Generic6DofConstraint(rigidBodies[BodyPartIndex.BODYPART_SPINE.ordinal()], rigidBodies[BodyPartIndex.BODYPART_PELVIS.ordinal()], localA, localB, true);
		limitTrans.set(-BulletGlobals.SIMD_PI * 0.2f, -BulletGlobals.SIMD_PI/2, -BulletGlobals.SIMD_PI * 0.2f);
		joint6DOF.setAngularLowerLimit(limitTrans);
		limitTrans.set(BulletGlobals.SIMD_PI * 0.2f, BulletGlobals.SIMD_PI/2, BulletGlobals.SIMD_PI * 0.2f);
		joint6DOF.setAngularUpperLimit(limitTrans);
		dynamicsWorld.addConstraint(joint6DOF, true);
		//������Ҵ���
		localA.setIdentity();
		localB.setIdentity();
		localA.origin.set(pelvisR+rightUpperLegR,-pelvisH/2,0f);
		localB.origin.set(0,rightUpperLegH/2,0);
		joint6DOF = new Generic6DofConstraint(rigidBodies[BodyPartIndex.BODYPART_PELVIS.ordinal()], rigidBodies[BodyPartIndex.BODYPART_RIGHT_UPPER_LEG.ordinal()], localA, localB, true);
		limitTrans.set(-BulletGlobals.SIMD_PI * 0.1f, -BulletGlobals.SIMD_PI * 0.1f, -BulletGlobals.SIMD_PI * 0.2f);
		joint6DOF.setAngularLowerLimit(limitTrans);
		limitTrans.set(BulletGlobals.SIMD_PI * 0.1f, BulletGlobals.SIMD_PI * 0.1f, BulletGlobals.SIMD_PI * 0.2f);
		joint6DOF.setAngularUpperLimit(limitTrans);
		dynamicsWorld.addConstraint(joint6DOF, true);
		//��Ǻ������
		localA.setIdentity();
		localB.setIdentity();
		localA.origin.set(-pelvisR-leftUpperLegR,-pelvisH/2,0f);
		localB.origin.set(0,rightUpperLegH/2,0);
		joint6DOF = new Generic6DofConstraint(rigidBodies[BodyPartIndex.BODYPART_PELVIS.ordinal()], rigidBodies[BodyPartIndex.BODYPART_LEFT_UPPER_LEG.ordinal()], localA, localB, true);
		limitTrans.set(-BulletGlobals.SIMD_PI * 0.1f, -BulletGlobals.SIMD_PI * 0.1f, -BulletGlobals.SIMD_PI * 0.2f);
		joint6DOF.setAngularLowerLimit(limitTrans);
		limitTrans.set(BulletGlobals.SIMD_PI * 0.1f, BulletGlobals.SIMD_PI * 0.1f, BulletGlobals.SIMD_PI * 0.2f);
		joint6DOF.setAngularUpperLimit(limitTrans);
		dynamicsWorld.addConstraint(joint6DOF, true);
		//����Ⱥ���С��
		localA.setIdentity();
		localB.setIdentity();
		localA.origin.set(0,-leftUpperLegTH/2,0f);
		localB.origin.set(0,leftLowerLegTH/2,0);
		joint6DOF = new Generic6DofConstraint(rigidBodies[BodyPartIndex.BODYPART_LEFT_UPPER_LEG.ordinal()], rigidBodies[BodyPartIndex.BODYPART_LEFT_LOWER_LEG.ordinal()], localA, localB, true);
		limitTrans.set(-BulletGlobals.SIMD_PI * 0.5f, -BulletGlobals.FLT_EPSILON, -BulletGlobals.FLT_EPSILON);
		joint6DOF.setAngularLowerLimit(limitTrans);
		limitTrans.set(BulletGlobals.FLT_EPSILON, BulletGlobals.FLT_EPSILON, BulletGlobals.FLT_EPSILON);
		joint6DOF.setAngularUpperLimit(limitTrans);
		dynamicsWorld.addConstraint(joint6DOF, true);
		//�Ҵ��Ⱥ���С��
		localA.setIdentity();
		localB.setIdentity();
		localA.origin.set(0,-rightUpperLegTH/2,0f);
		localB.origin.set(0,rightLowerLegTH/2,0);
		joint6DOF = new Generic6DofConstraint(rigidBodies[BodyPartIndex.BODYPART_RIGHT_UPPER_LEG.ordinal()], rigidBodies[BodyPartIndex.BODYPART_RIGHT_LOWER_LEG.ordinal()], localA, localB, true);
		limitTrans.set(-BulletGlobals.SIMD_PI * 0.5f, -BulletGlobals.FLT_EPSILON, -BulletGlobals.FLT_EPSILON);
		joint6DOF.setAngularLowerLimit(limitTrans);
		limitTrans.set(BulletGlobals.FLT_EPSILON, BulletGlobals.FLT_EPSILON, BulletGlobals.FLT_EPSILON);
		joint6DOF.setAngularUpperLimit(limitTrans);
		dynamicsWorld.addConstraint(joint6DOF, true);
	}
	private RigidBody addRigidBody(float mass, Transform startTransform, CollisionShape shape) {
		boolean isDynamic = (mass != 0f);
		Vector3f localInertia = new Vector3f();
		localInertia.set(0f, 0f, 0f);
		if (isDynamic) {
			shape.calculateLocalInertia(mass, localInertia);
		}
		DefaultMotionState myMotionState = new DefaultMotionState(startTransform);
		RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(mass, myMotionState, shape, localInertia);
		rbInfo.additionalDamping = true;
		RigidBody body = new RigidBody(rbInfo);
		body.setRestitution(0.0f);
		body.setFriction(0.8f);
		dynamicsWorld.addRigidBody(body);
		return body;
	}
	
	public void drawSelf(int checkedIndex){
		MatrixState.pushMatrix();//�����ֳ�
		for(int i=0;i<bodyForDraws.length;i++){//����������Ⱦ��������ֵļ�����������
			LoadedObjectVertexNormal lovo = bodyForDraws[i];
			if(i==checkedIndex){//���������(���岿��)��ʰȡ
				lovo.changeColor(true);//���øı���ɫ�ı�־λΪtrue
				lovo.drawSelf(rigidBodies[i]);//���Ƹ�����(���岿��)
			}else{//��������(���岿��)û�б�ʰȡ
				lovo.changeColor(false);//���øı���ɫ�ı�־λΪfalse
				lovo.drawSelf(rigidBodies[i]);//���Ƹ�����(���岿��)
			}
		}
		MatrixState.popMatrix();//�ָ��ֳ�
	}
}
