package com.ja3son.demo.objects.bezier;

/*
 * ����񷶥����
 */

public class Top {
	float scale;//񷶥�Ĵ�С
	
	TopPart1 top1;//񷶥�ĵ�һ����
	TopPart2 top2;//񷶥�ĵڶ�����
	TopPart3 top3;//񷶥�ĵ�������
	TopPart4 top4;//񷶥�ĵ��Ĳ���
	
	
    float xAngle=0;//��x����ת�ĽǶ�
    float yAngle=0;//��y����ת�ĽǶ�
    float zAngle=0;//��z����ת�ĽǶ�
	
	
	Top(MySurfaceView mv,float scale, int nCol ,int nRow)
	{
		this.scale=scale;//�����С��ֵ
		
		//��������
		top1=new TopPart1(mv,0.4f*scale,nCol,nRow);//񷶥�ĵ�һ����
		top2=new TopPart2(mv,0.4f*scale,nCol,nRow);//񷶥�ĵڶ�����
		top3=new TopPart3(mv,0.8f*scale,nCol,nRow);//񷶥�ĵ�������
		top4=new TopPart4(mv,0.8f*scale,nCol,nRow);//񷶥�ĵ��Ĳ���

	}
	public void drawSelf(int texId)
	{
		
   	 	MatrixState.rotate(xAngle, 1, 0, 0);
   	 	MatrixState.rotate(yAngle, 0, 1, 0);
   	 	MatrixState.rotate(zAngle, 0, 0, 1);
		
		
		//񷶥�ĵ�һ����
		MatrixState.pushMatrix();
		MatrixState.translate(0f, 4.0f*scale, 0f);
        top1.drawSelf(texId);//񷶥�ĵ�һ����
        MatrixState.popMatrix();
		//񷶥�ĵڶ�����
    	MatrixState.pushMatrix();
    	MatrixState.translate(0f, 3.7f*scale, 0f);
        top2.drawSelf(texId);//񷶥�ĵڶ�����
        MatrixState.popMatrix();
		//񷶥�ĵ�������
    	MatrixState.pushMatrix();
    	MatrixState.translate(0f, 0f*scale, 0f);
        top3.drawSelf(texId);
        MatrixState.popMatrix();
		//񷶥�ĵ��Ĳ���
    	MatrixState.pushMatrix();
    	MatrixState.translate(0f, -1.9f*scale, 0f);
        top4.drawSelf(texId);
        MatrixState.popMatrix();
	}

}
