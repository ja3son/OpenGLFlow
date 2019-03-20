package com.ja3son.demo.objects.bezier;

/*
 * ���彨����
 */

public class Building {
	float scale;//�����Ĵ�С
	
	Top top;//񷶥	
	Tower tower;//��
	TowerTop towertop;
	Cube cube;//������
	Cube cube2;//������
	Cube cube3;//������
	Cube cube4;//������
	
    float xAngle=0;//��x����ת�ĽǶ�
    float yAngle=0;//��y����ת�ĽǶ�
    float zAngle=0;//��z����ת�ĽǶ�
	
	Building( MySurfaceView mv,
			float scale,//�����Ĵ�С
			int nCol,int nRow)
	{
		this.scale=scale;
		//��������
		top=new Top(mv,0.5f*scale,nCol,nRow);//񷶥
		tower=new Tower(mv,1*scale,nCol,nRow);//��
		
		towertop=new TowerTop(mv,1*scale,nCol,nRow);//��
		
		
        cube=new Cube(mv,1.4f*scale,new float[]{2,3f,3f} );//������
        cube2=new Cube(mv,1.4f*scale,new float[]{2f,3f,2f} );//������
        cube3=new Cube(mv,1.4f*scale,new float[]{1.4f,3f,1.4f} );//������
        
        cube4=new Cube(mv,1.4f*scale,new float[]{10f,1f,10f} );//������

	}
	public void drawSelf(int texId)
	{
		
   	 	MatrixState.rotate(xAngle, 1, 0, 0);
   	 	MatrixState.rotate(yAngle, 0, 1, 0);
   	 	MatrixState.rotate(zAngle, 0, 0, 1);
		
		MatrixState.pushMatrix();
		
		//񷶥
		MatrixState.pushMatrix();
        MatrixState.translate(0f, 0f*scale, 0f);
        top.drawSelf(texId);//񷶥
        MatrixState.popMatrix();
        
		//****************************��********************************
		MatrixState.pushMatrix();
        MatrixState.translate(6f*scale, -1.6f*scale, 6f*scale);
        tower.drawSelf(texId);//񷶥
        MatrixState.popMatrix();
        
		//��
		MatrixState.pushMatrix();
        MatrixState.translate(-6f*scale, -1.6f*scale, 6f*scale);
        tower.drawSelf(texId);//񷶥
        MatrixState.popMatrix();
        
		//��
		MatrixState.pushMatrix();
        MatrixState.translate(6f*scale, -1.6f*scale, -6f*scale);
        tower.drawSelf(texId);//񷶥
        MatrixState.popMatrix();
        
		//��
		MatrixState.pushMatrix();
        MatrixState.translate(-6f*scale, -1.6f*scale, -6f*scale);
        tower.drawSelf(texId);//񷶥
        MatrixState.popMatrix();
        //****************************��********************************
        
        //*******************����************************
		MatrixState.pushMatrix();
        MatrixState.translate(-2.5f*scale, -3.8f*scale, 0f);
        towertop.drawSelf(texId);//񷶥
        MatrixState.popMatrix();
        
		MatrixState.pushMatrix();
        MatrixState.translate(2.5f*scale, -3.8f*scale, 0f);
        towertop.drawSelf(texId);//񷶥
        MatrixState.popMatrix();
        
        //*******************����************************
        
        //*********************������*********************************
		MatrixState.pushMatrix();
        MatrixState.translate(0f, -2.7f*scale, 0f);
        MatrixState.translate(0, 0.05f, 0f);
        cube.drawSelf(texId);
        MatrixState.popMatrix();
        
		MatrixState.pushMatrix();
        MatrixState.translate(-2.0f*scale, -2.7f*scale, 0f);
        cube2.drawSelf(texId);
        MatrixState.popMatrix();
        
		MatrixState.pushMatrix();
        MatrixState.translate(2.0f*scale, -2.7f*scale, 0f);
        cube2.drawSelf(texId);
        MatrixState.popMatrix();
        
		MatrixState.pushMatrix();
        MatrixState.translate(-3.43f*scale, -2.7f*scale, 0f);
        MatrixState.translate(0, -0.05f, 0f);
        MatrixState.rotate(45, 0, 1, 0);
        cube3.drawSelf(texId);
        MatrixState.popMatrix();
        
		MatrixState.pushMatrix();
        MatrixState.translate(3.43f*scale, -2.7f*scale, 0f);
        MatrixState.translate(0, -0.05f, 0f);
        MatrixState.rotate(45, 0, 1, 0);
        cube3.drawSelf(texId);
        MatrixState.popMatrix();
        
      //*********************������*********************************
        
		MatrixState.pushMatrix();
        MatrixState.translate(0f, -5f*scale, 0f);
        cube4.drawSelf(texId);
        MatrixState.popMatrix(); 
        
        MatrixState.popMatrix();
	}
}
