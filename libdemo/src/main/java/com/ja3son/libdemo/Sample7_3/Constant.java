package com.ja3son.libdemo.Sample7_3;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.ja3son.libdemo.R;

public class Constant    
{
	 final static float TIME_STEP=1.0f/60;					//ģ���Ƶ��
	  final static int MAX_SUB_STEPS=5;					//�����Ӳ���
	  final static float EYE_X=-5;							//�۲��ߵ�λ��x
	  final static float EYE_Y=4;							//�۲��ߵ�λ��y
	  final static float EYE_Z=5;							//�۲��ߵ�λ��z
	  final static float TARGET_X=0;						//Ŀ���λ��x
	  final static float TARGET_Y=0;						//Ŀ��λ��Y
	  final static float TARGET_Z=0;						//Ŀ���λ��Z
	//��������ߴ絥Ԫ
    final static float GT_UNIT_SIZE=0.6f;
    
	public static final float UNIT_SIZE=0.5f;//ÿ��ĵ�λ����	
	public static final float LAND_HIGHEST=1.5f;//½�����߲�
	
	public static float[][] yArray;//½����ÿ������ĸ߶�����
	public static int COLS;//½������
	public static int ROWS;//½������

	public static void initConstant(Resources r)
	{
		//�ӻҶ�ͼƬ�м���½����ÿ������ĸ߶�
		yArray=loadLandforms(r);
		//���������С����½�ص�����������
		COLS=yArray[0].length-1;
		ROWS=yArray.length-1;		 
	}
	
	//�ӻҶ�ͼƬ�м���½����ÿ������ĸ߶�
	public static float[][] loadLandforms(Resources resources)
	{
		Bitmap bt=BitmapFactory.decodeResource(resources, R.drawable.landform);
		int colsPlusOne=bt.getWidth();
		int rowsPlusOne=bt.getHeight(); 
		float[][] result=new float[rowsPlusOne][colsPlusOne];
		for(int i=0;i<rowsPlusOne;i++)
		{
			for(int j=0;j<colsPlusOne;j++)
			{
				int color=bt.getPixel(j,i);
				int r=Color.red(color);
				int g=Color.green(color); 
				int b=Color.blue(color);
				int h=(r+g+b)/3;
				result[i][j]=h*LAND_HIGHEST/255;  
			}
		}		
		return result;
	}
}
