package com.ja3son.demo.objects.cone;
//Բ׶��
public class Cone
{
	Circle bottomCircle;//��Բ
	ConeSide coneSide;//����
	float xAngle=0;//��x����ת�ĽǶ�
    float yAngle=0;//��y����ת�ĽǶ�
    float zAngle=0;//��z����ת�ĽǶ�
    float h;
    float scale;

    int BottomTexId;  //��������
    int sideTexId;  //��������
    
	public Cone(MySurfaceView mySurfaceView,float scale,float r, float h,int n,
			int BottomTexId, int sideTexId)
	{
		this.scale=scale;
		this.h=h;
		this.BottomTexId=BottomTexId;
		this.sideTexId=sideTexId;
		
		bottomCircle=new Circle(mySurfaceView,scale,r,n);  //��������Բ����
		coneSide=new ConeSide(mySurfaceView,scale,r,h,n); //����Բ׶�������
	}
	public void drawSelf()
	{
		MatrixState.rotate(xAngle, 1, 0, 0);
		MatrixState.rotate(yAngle, 0, 1, 0);
		MatrixState.rotate(zAngle, 0, 0, 1);				
		//����
		MatrixState.pushMatrix();
		MatrixState.translate(0, -h/2*scale, 0);
		MatrixState.rotate(90, 1, 0, 0);
		MatrixState.rotate(180, 0, 0, 1);
		bottomCircle.drawSelf(BottomTexId);
		MatrixState.popMatrix();
		//����
		MatrixState.pushMatrix();
		MatrixState.translate(0, -h/2*scale, 0);
		coneSide.drawSelf(sideTexId);
		MatrixState.popMatrix();
	}
}
