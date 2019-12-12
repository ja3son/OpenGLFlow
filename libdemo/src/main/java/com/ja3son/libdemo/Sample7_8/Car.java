package com.ja3son.libdemo.Sample7_8;

import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.CompoundShape;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.vehicle.DefaultVehicleRaycaster;
import com.bulletphysics.dynamics.vehicle.RaycastVehicle;
import com.bulletphysics.dynamics.vehicle.VehicleTuning;
import com.bulletphysics.dynamics.vehicle.WheelInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import com.ja3son.libdemo.Sample7_8.util.LoadedObjectVertexNormalTexture;
import com.ja3son.libdemo.Sample7_8.util.MatrixState;
import com.ja3son.libdemo.Sample7_8.util.SYSUtil;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

public class Car {
    float v_x1 = 2.26f; //�����ϲ��ֿ�
    float v_y1 = 0.8f;//�����ϲ��ָ�
    float v_z1 = 6.14f;//�����ϲ��ֿ�
    float v_x2 = 2.36f; //�����²��ֿ��
    float v_y2 = 0.9f;//�����²��ָ�
    float v_z2 = 1.57f;///�����²��ֳ�

    DefaultVehicleRaycaster vehicleRayCaster;    //��ͨ���߲�������
    RaycastVehicle rayVehicle;                    //��ͨ���߶���
    VehicleTuning tuning;                        //Э����
    DynamicsWorld dynamicsWorld;                //��������
    RigidBody carChassis;                        //�������

    float wheelRadius = 0.4f;//���ְ뾶
    float wheelWidth = 0.4f;//���ֿ��
    float gVehicleSteering = 0f;//ת��װ��(ǰ��)��ת�Ƕ�
    float gVehicleEngineForce = 0;//����������

    float wheelFrontX = 0.99f;    //ǰ���ֵ����������߾���
    float wheelBackX = 1.1f;    //���ֵ����������߾���
    float wheelFrontY = 1.925f;    //ǰ���ֵ�����ԭ���������
    float wheelBackY = 1.848f;    //���ֵ�����ԭ���������
    //////////��������
    static final float wheelFriction = 1000;//����Ħ����
    static final float suspensionStiffness = 20.f;//���ܸն�
    static final float suspensionDamping = 2.3f;//��������
    static final float suspensionCompression = 4.4f;//����ѹ��ϵ��
    static final float rollInfluence = 0.1f;//����Ӱ��ֵ
    //�������Ҹ߶�
    float suspensionRestLength = 0.9f;
    /////////���������
    Vector3f cameraPosition = new Vector3f(0f, 0f, 0f);    //�����λ��
    Vector3f cameraTargetPosition = new Vector3f(0f, 0f, 0f); //�����Ŀ���
    Vector3f cameraUp = new Vector3f(0f, 1f, 0f);    //�����up����
    //��̥����
    final static Vector3f wheelDirectionCS0 = new Vector3f(0, -1, 0);//��̥��ֱ����ƫ������
    final static Vector3f wheelAxleCS = new Vector3f(-1, 0, 0);    //����������
    //�����ƶ���
    VehicleDraw vd;
    //ǰ�����͵�����־λ
    boolean DRR = true;
    float camera_angle = 0;//�������ʼ�Ƕ�
    //��������1��ֵ��������Y����ת�ĳ�ʼ�Ƕȣ���2����3��ֵ�������ʼ��XZ����
    Vector3f carStartPoint;
    static final int rightIndex = 0;//��ͨ��������ϵͳ������������
    static final int upIndex = 1;//��ͨ��������ϵͳ������������
    static final int forwardIndex = 2; //��ͨ��������ϵͳǰ����������


    public Car(
            DynamicsWorld dynamicsWorld,    //��������
            LoadedObjectVertexNormalTexture vBox,//��Obj�ļ��м��صĳ���ģ��
            LoadedObjectVertexNormalTexture vWheel,//��Obj�ļ��м��صĳ���ģ��
            int vehicleTextureId,//����������Id
            Vector3f carStartPoint    //������ʼλ��
    ) {
        this.dynamicsWorld = dynamicsWorld;//��ʼ��������������
        this.carStartPoint = carStartPoint;    //��ʼ��������ʼλ��
        //������������ĳ�������״
        CollisionShape chassisShape_down = new BoxShape(new Vector3f(v_x1 / 2, v_y1 / 2, v_z1 / 2));
        //������������ĳ�������״
        CollisionShape chassisShape_up = new BoxShape(new Vector3f(v_x2 / 2, v_y2 / 2, v_z2 / 2));
        //�������������
        CompoundShape compound = new CompoundShape();
        //localTrans ��ʵת��������ڵ��̵�����
        // localTrans effectively shifts the center of mass with respect to the chassis
        //�����²��������ڳ���������еĳ�ʼ�任
        Transform localTrans1 = new Transform();
        localTrans1.setIdentity();//���任�����ʼ��
        localTrans1.origin.set(0, 1, 0);//����Ϊ��Y��������ƽ��1�ı任
        //�������²��ֳ�������ײ��״���복�������
        compound.addChildShape(localTrans1, chassisShape_down);
        //�����ϲ��������ڳ���������еĳ�ʼ�任
        Transform localTrans2 = new Transform();
        localTrans2.setIdentity();//���任�����ʼ��
        localTrans2.origin.set(0, 1.4f, -0.4f);//����Y��ƽ��1.4��Z��ƽ��-0.4�ı任
        //�������ϲ��ֳ�������ײ��״���복�������
        compound.addChildShape(localTrans2, chassisShape_up);

        //���������ĳ�ʼ�任
        Transform tr = new Transform();
        tr.setIdentity();//���任�����ʼ��
        tr.origin.set(carStartPoint.y, 0, carStartPoint.z);//����ƽ�Ʊ任��ƽ�Ƶ�������ʼλ��

        if (carStartPoint.x != 0)//�������ʼ��ת�ǲ�Ϊ0
        {
            Quat4f q = new Quat4f(0, 0, 0, 1f);//�½�һ����Ԫ������
            //�½�һ������ת���ݶ���ǰ����ֵ��ʾ��ת�����������һ��ֵ��ʾ��ת�Ƕ�
            AxisAngle4f aa = new AxisAngle4f(0, 1, 0, carStartPoint.x);
            q.set(aa);//������ת����ת��Ϊ��Ԫ��
            tr.setRotation(q);//����Ԫ����ʾ����ת����任����
        }


        //�����������
        carChassis = localCreateRigidBody(500, tr, compound);//�����û���Ϣ���ڼ����ײ
        //�����û���Ϣ���ڼ����ײ
        carChassis.setUserPointer(1);

        //���ó���
        //clientResetScene();
        //������ͨ����
        {
            //������ͨ����
            tuning = new VehicleTuning();//����Э����
            //������ͨ���߲�������
            vehicleRayCaster = new DefaultVehicleRaycaster(dynamicsWorld);
            //������ʾ�����������Ľ�ͨ���߶���
            rayVehicle = new RaycastVehicle(tuning, carChassis, vehicleRayCaster);

            //���ý�ͨ���߲�����
            carChassis.setActivationState(CollisionObject.DISABLE_DEACTIVATION);
            //����ͨ���߶�����ӽ���������
            dynamicsWorld.addVehicle(rayVehicle);
            //�������ӵ�߶�
            float connectionHeight = 1.2f;

            // choose coordinate system
            //���ý�ͨ��������ϵͳ���������������������
            rayVehicle.setCoordinateSystem(rightIndex, upIndex, forwardIndex);
            //����ǰ��
            boolean isFrontWheel = true;//�Ƿ�Ϊǰ�ֱ�־λ
            //������1��ǰ�ֵ����ӵ����
            Vector3f connectionPointCS0 = new Vector3f(wheelFrontX - (0.3f * wheelWidth), connectionHeight, wheelFrontY - wheelRadius);

            //��ӵ�1��ǰ��
            rayVehicle.addWheel(connectionPointCS0, wheelDirectionCS0, wheelAxleCS, suspensionRestLength, wheelRadius, tuning, isFrontWheel);
            //��2��ǰ�ֵ����ӵ�����
            connectionPointCS0.set(-wheelFrontX + (0.3f * wheelWidth), connectionHeight, wheelFrontY - wheelRadius);

            //��ӵ�2��ǰ��
            rayVehicle.addWheel(connectionPointCS0, wheelDirectionCS0, wheelAxleCS, suspensionRestLength, wheelRadius, tuning, isFrontWheel);
            //��1�����ֵ����ӵ�����
            connectionPointCS0.set(-wheelBackX + (0.3f * wheelWidth), connectionHeight, -wheelBackY + wheelRadius);

            isFrontWheel = false;//�Ƿ�Ϊǰ�ֱ�־λ
            //��ӵ�1������
            rayVehicle.addWheel(connectionPointCS0, wheelDirectionCS0, wheelAxleCS, suspensionRestLength, wheelRadius, tuning, isFrontWheel);
            //��2�����ֵ����ӵ�����
            connectionPointCS0.set(wheelBackX - (0.3f * wheelWidth), connectionHeight, -wheelBackY + wheelRadius);
            //��ӵ�2������
            rayVehicle.addWheel(connectionPointCS0, wheelDirectionCS0, wheelAxleCS, suspensionRestLength, wheelRadius, tuning, isFrontWheel);
            //�������г���
            for (int i = 0; i < rayVehicle.getNumWheels(); i++) {
                WheelInfo wheel = rayVehicle.getWheelInfo(i);//��ȡ������Ϣ
                wheel.suspensionStiffness = suspensionStiffness; //�������ܸն�
                wheel.wheelsDampingRelaxation = suspensionDamping; //���ó��ֵ�����ϵ��
                wheel.wheelsDampingCompression = suspensionCompression;//��������ѹ��ϵ��
                wheel.frictionSlip = wheelFriction;//������̥Ħ��ϵ��
                wheel.rollInfluence = rollInfluence;//���ù���Ӱ��ֵ
            }


        }
        //�����������ƶ���
        vd = new VehicleDraw(rayVehicle, vBox,//����
                vWheel,//����
                vehicleTextureId);
    }

    public void carMove_go()//����ǰ���ķ���
    {
        float gEngineForce = 3000;//������
        int wheelIndex = 2;//��Ҫ�����������ĳ�������
        rayVehicle.applyEngineForce(gEngineForce, wheelIndex);//��ָ����������������
        rayVehicle.setBrake(0, wheelIndex);//��ָ�����ֵ�ɲ��������Ϊ0
        wheelIndex = 3;//��һ����Ҫ�����������ĳ�������
        rayVehicle.applyEngineForce(gEngineForce, wheelIndex);//��ָ����������������
        rayVehicle.setBrake(0, wheelIndex);//����һָ�����ֵ�ɲ��������Ϊ0
    }

    public void carMove_back()//�������˵ķ���
    {
        float gEngineForce = -3000;//������
        int wheelIndex = 2;//��Ҫ�����������ĳ�������
        rayVehicle.applyEngineForce(gEngineForce, wheelIndex);//��ָ����������������
        rayVehicle.setBrake(0, wheelIndex);//��ָ�����ֵ�ɲ��������Ϊ0
        wheelIndex = 3;//��һ����Ҫ�����������ĳ�������
        rayVehicle.applyEngineForce(gEngineForce, wheelIndex);//��ָ����������������
        rayVehicle.setBrake(0, wheelIndex);//����һָ�����ֵ�ɲ��������Ϊ0
    }

    public void carKong() {//����פ���ƶ�����

        float gEngineForce = 0.0f;//������
        int wheelIndex = 2;//��Ҫ�����������ĳ�������
        rayVehicle.applyEngineForce(gEngineForce, wheelIndex);//��ָ����������������
        rayVehicle.setBrake(60, wheelIndex);//��ָ�����ֵ�ɲ��������Ϊ60
        wheelIndex = 3;//��һ����Ҫ�����������ĳ�������
        rayVehicle.applyEngineForce(gEngineForce, wheelIndex);//��ָ����������������
        rayVehicle.setBrake(60, wheelIndex);//����һָ�����ֵ�ɲ��������Ϊ60

    }

    public void carBreak() {//����ɲ������
        int wheelIndex = 2;//��Ҫ����ɲ�����ĳ�������
        rayVehicle.setBrake(100f, wheelIndex);//��ָ�����ֵ�ɲ��������Ϊ100
        wheelIndex = 3;//��һ����Ҫ����ɲ�����ĳ�������
        rayVehicle.setBrake(100f, wheelIndex);//����һָ�����ֵ�ɲ��������Ϊ100
    }

    public void carTurn(float angle) {//����ת��ķ���
        camera_angle = angle;//���õ�ǰת��Ƕ�
        int wheelIndex = 0;//��Ҫ����ת��Ƕȵĳ�������
        rayVehicle.setSteeringValue(angle / 900, wheelIndex);//��ָ������������ת�Ƕ�
        wheelIndex = 1;//��һ����Ҫ����ת��Ƕȵĳ�������
        rayVehicle.setSteeringValue(angle / 900, wheelIndex);//����һָ������������ת�Ƕ�
    }

    public void clientResetScene() {
        Transform tr = new Transform();
        tr.setIdentity();
        tr.origin.set(carStartPoint.y, 1, carStartPoint.z);

        if (carStartPoint.x != 0) {
            Quat4f q = new Quat4f(0, 0, 0, 1f);
            AxisAngle4f aa = new AxisAngle4f(0, 1, 0, carStartPoint.x);
            q.set(aa);
            tr.setRotation(q);
        }
        carChassis.setCenterOfMassTransform(tr); //������������
        carChassis.setLinearVelocity(new Vector3f(0, 0, 0));//�������ٶ�
        carChassis.setAngularVelocity(new Vector3f(0, 0, 0));//���ý��ٶ�
        dynamicsWorld.getBroadphase().getOverlappingPairCache().cleanProxyFromPairs(carChassis.getBroadphaseHandle(), dynamicsWorld.getDispatcher());
        if (rayVehicle != null) {
            rayVehicle.resetSuspension();
            for (int i = 0; i < rayVehicle.getNumWheels(); i++) {
                // synchronize the wheels with the (interpolated) chassis worldtransform
                // ͬ��������̱任
                rayVehicle.updateWheelTransform(i, true);
            }

        }
    }

    public RigidBody localCreateRigidBody(float mass, Transform startTransform,
                                          CollisionShape shape) {
        boolean isDynamic = (mass != 0f);
        Vector3f localInertia = new Vector3f(0f, 0f, 0f);
        if (isDynamic) {
            shape.calculateLocalInertia(mass, localInertia);
        }
        // using motionstate is recommended, it provides interpolation capabilities, and only synchronizes 'active' objects
        DefaultMotionState myMotionState = new DefaultMotionState(startTransform);
        RigidBodyConstructionInfo cInfo = new RigidBodyConstructionInfo(mass, myMotionState, shape, localInertia);
        RigidBody body = new RigidBody(cInfo);
        dynamicsWorld.addRigidBody(body);
        return body;
    }

    public void drawSelf() {
        vd.drawSelf();
    }

    public void updateCamera1() {
        Transform chassisWorldTrans = new Transform();
        carChassis.getMotionState().getWorldTransform(chassisWorldTrans);    //��ó���任

        float[] fa = new float[4];
        Quat4f ro = chassisWorldTrans.getRotation(new Quat4f());//��ȡ��ǰ�任����ת��Ϣ
        fa = SYSUtil.fromSYStoAXYZ(ro);//����Ԫ��ת����AXYZ����ʽ
        //System.out.println("/////////////////////"+fa[0]+"////"+fa[1]+"////"+fa[2]+"////"+fa[3]+"///////"+camera_angle);
        com.ja3son.libdemo.Sample7_8.util.Vector3f start = new com.ja3son.libdemo.Sample7_8.util.Vector3f(0, 0, 6);
        com.ja3son.libdemo.Sample7_8.util.Vector3f end = new com.ja3son.libdemo.Sample7_8.util.Vector3f(0, 0, 0);
        com.ja3son.libdemo.Sample7_8.util.Vector3f end2;
        if (0 <= fa[0] && fa[0] <= 120 && fa[2] < 0) {
            end = start.roty(Math.toRadians(-fa[0]));
        } else {
            end = start.roty(Math.toRadians(fa[0]));
        }

        //if(camera_angle<=4&&camera_angle>=-4)//���������
        {
            end2 = end;
        }
        //else
        //end2=end.roty(Math.toRadians(-camera_angle*rayVehicle.getCurrentSpeedKmHour()/2000));
        Vector3f cam = new Vector3f(end2.x * 1.0f, end2.y, end2.z * 1.0f);
        cameraTargetPosition.set(chassisWorldTrans.origin.x, chassisWorldTrans.origin.y, chassisWorldTrans.origin.z);    //�������Ŀ���
        cameraPosition.set(chassisWorldTrans.origin.x, chassisWorldTrans.origin.y + 12, chassisWorldTrans.origin.z);//���������λ��

        MatrixState.setInitStack();
//		 MatrixState.scale(-1, 1, 1);
        MatrixState.setCamera(cameraPosition.x, cameraPosition.y, cameraPosition.z,
                cameraTargetPosition.x, cameraTargetPosition.y, cameraTargetPosition.z,
                cam.x, cam.y, cam.z);

    }

    //�������
    public void updateCamera2() {
        Transform chassisWorldTrans = new Transform();
        carChassis.getMotionState().getWorldTransform(chassisWorldTrans);    //��ó���任

        float[] fa = new float[4];
        Quat4f ro = chassisWorldTrans.getRotation(new Quat4f());//��ȡ��ǰ�任����ת��Ϣ
        fa = SYSUtil.fromSYStoAXYZ(ro);//����Ԫ��ת����AXYZ����ʽ
        //System.out.println("/////////////////////"+fa[0]+"////"+fa[1]+"////"+fa[2]+"////"+fa[3]+"///////"+camera_angle);
        com.ja3son.libdemo.Sample7_8.util.Vector3f start = new com.ja3son.libdemo.Sample7_8.util.Vector3f(0, 0, 6);
        com.ja3son.libdemo.Sample7_8.util.Vector3f end = new com.ja3son.libdemo.Sample7_8.util.Vector3f(0, 0, 0);
        com.ja3son.libdemo.Sample7_8.util.Vector3f end2;
        if (0 <= fa[0] && fa[0] <= 120 && fa[2] < 0) {
            end = start.roty(Math.toRadians(-fa[0]));
        } else {
            end = start.roty(Math.toRadians(fa[0]));
        }

        if (camera_angle <= 4 && camera_angle >= -4)//���������
        {
            end2 = end;
        } else
            end2 = end.roty(Math.toRadians(-camera_angle * rayVehicle.getCurrentSpeedKmHour() / 2000));
        Vector3f cam = new Vector3f(-end2.x * 1.0f, end2.y + 4.0f, -end2.z * 1.0f);
        cameraTargetPosition.set(chassisWorldTrans.origin.x + end2.x, chassisWorldTrans.origin.y + end2.y, chassisWorldTrans.origin.z + end2.z);    //�������Ŀ���
        cameraPosition.set(chassisWorldTrans.origin.x + cam.x, chassisWorldTrans.origin.y + cam.y, chassisWorldTrans.origin.z + cam.z);//���������λ��

        MatrixState.setInitStack();
//		 MatrixState.scale(-1, 1, 1);
        MatrixState.setCamera(cameraPosition.x, cameraPosition.y, cameraPosition.z,
                cameraTargetPosition.x, cameraTargetPosition.y, cameraTargetPosition.z,
                cameraUp.x, cameraUp.y, cameraUp.z);

    }

}
