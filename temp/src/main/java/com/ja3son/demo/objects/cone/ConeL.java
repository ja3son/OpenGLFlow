package com.ja3son.demo.objects.cone;
//�Ǽ�Բ׶��
public class ConeL
{
	CircleL bottomCircle;//��Բ�ĹǼ��������
	ConeSideL coneSide;//����ĹǼ��������
	float xAngle=0;//��x����ת�ĽǶ�
    float yAngle=0;//��y����ת�ĽǶ�
    float zAngle=0;//��z����ת�ĽǶ�
    float h;
    float scale;
    
	public ConeL(MySurfaceView mySurfaceView,float scale,float r, float h,int n)
	{
		this.scale=scale;
		this.h=h;
		bottomCircle=new CircleL(mySurfaceView,scale,r,n);  //��������Ǽ�Բ�Ķ���
		coneSide=new ConeSideL(mySurfaceView,scale,r,h,n); //���������޶�Բ׶�ǼܵĶ���
	}
	public void drawSelf()
	{
		MatrixState.rotate(xAngle, 1, 0, 0);
		MatrixState.rotate(yAngle, 0, 1, 0);
		MatrixState.rotate(zAngle, 0, 0, 1);		
		
		//����
		MatrixState.pushMatrix();
		MatrixState.translate(0, -h/2, 0);
		MatrixState.rotate(90, 1, 0, 0);
		MatrixState.rotate(180, 0, 0, 1);
		bottomCircle.drawSelf();
		MatrixState.popMatrix();
		
		//����
		MatrixState.pushMatrix();
		MatrixState.translate(0, -h/2, 0);
		coneSide.drawSelf();
		MatrixState.popMatrix();
	}
}
