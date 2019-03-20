package com.ja3son.demo.objects.bezier;

public class Cube 
{
	Texture[] rect=new Texture[6];
	float xAngle=0;//��x����ת�ĽǶ�
    float yAngle=0;//��y����ת�ĽǶ�
    float zAngle=0;//��z����ת�ĽǶ�
    float a;
    float b;
    float c;
    float scale;//�ߴ�

	boolean texFlag;//�Ƿ��������ı�־λ
	public Cube(MySurfaceView mv,float scale,float[] abc)
	{
		a=abc[0];
		b=abc[1];
		c=abc[2];
		rect[0]=new Texture(mv,scale,a,b,1,1);
		rect[1]=new Texture(mv,scale,a,b,1,1);
		rect[2]=new Texture(mv,scale,c,b,1,1);
		rect[3]=new Texture(mv,scale,c,b,1,1);
		rect[4]=new Texture(mv,scale,a,c,1,1);
		rect[5]=new Texture(mv,scale,a,c,1,1);
		// ��ʼ����ɺ��ٸı������ֵ

		a*=scale;
		b*=scale;
		c*=scale;
	}
	public void drawSelf(int texId)
	{
		MatrixState.rotate(xAngle, 1, 0, 0);
		MatrixState.rotate(yAngle, 0, 1, 0);
		MatrixState.rotate(zAngle, 0, 0, 1);
        //ǰ��
		MatrixState.pushMatrix();
		MatrixState.translate(0, 0, c/2);
		rect[0].drawSelf(texId);
        MatrixState.popMatrix();
		//����
		MatrixState.pushMatrix();
		MatrixState.translate(0, 0, -c/2);
		MatrixState.rotate(180.0f, 0, 1, 0);
		rect[1].drawSelf(texId);
        MatrixState.popMatrix();
		//����
		MatrixState.pushMatrix();
		MatrixState.translate(a/2, 0, 0);
		MatrixState.rotate(90.0f, 0, 1, 0);
		rect[2].drawSelf(texId);
        MatrixState.popMatrix();
		//����
		MatrixState.pushMatrix();
		MatrixState.translate(-a/2, 0, 0);
		MatrixState.rotate(-90.0f, 0, 1, 0);
		rect[3].drawSelf(texId);
        MatrixState.popMatrix();
		//����
		MatrixState.pushMatrix();
		MatrixState.translate(0, -b/2, 0);
		MatrixState.rotate(90.0f, 1, 0, 0);
		rect[4].drawSelf(texId);
        MatrixState.popMatrix();
		//����
		MatrixState.pushMatrix();
		MatrixState.translate(0, b/2, 0);
		MatrixState.rotate(-90.0f, 1, 0, 0);
		rect[5].drawSelf(texId);
        MatrixState.popMatrix();
	}
}
