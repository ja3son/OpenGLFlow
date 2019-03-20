package com.ja3son.demo.objects.c60.util;

//�������㷽���ķ�װ��
public class VectorUtil 
{
	//�����������Ĳ��
	public static float[] getCrossProduct(float x1,float y1,float z1,float x2,float y2,float z2){		
		//�������ʸ�����ʸ����XYZ��ķ���ABC
        float A=y1*z2-y2*z1;
        float B=z1*x2-z2*x1;
        float C=x1*y2-x2*y1;
		return new float[]{A,B,C};
	}
	
	//������� 
	public static float[] vectorNormal(float[] vector){
		//��������ģ
		float module=(float)Math.sqrt(vector[0]*vector[0]+vector[1]*vector[1]+vector[2]*vector[2]);
		return new float[]{vector[0]/module,vector[1]/module,vector[2]/module};
	}
	//�����������ĵ��
	public static float dotProduct(float[] vec1,float[] vec2){
	    	return
			vec1[0]*vec2[0]+
			vec1[1]*vec2[1]+
			vec1[2]*vec2[2];
		
	}   
	
	//��������ģ
	public static float mould(float[] vec){
		return (float)Math.sqrt(vec[0]*vec[0]+vec[1]*vec[1]+vec[2]*vec[2]);
	}
	
	//�����������ļн�
	public static float angle(float[] vec1,float[] vec2){
		//������
		float dp=dotProduct(vec1,vec2);
		//��������������ģ
		float m1=mould(vec1);
		float m2=mould(vec2);
		
		float acos=dp/(m1*m2);
		
		//Ϊ�˱������������������
		if(acos>1)	{
			acos=1;
		}
		else if(acos<-1){
			acos=-1;
		}
		return (float)Math.acos(acos);
	}
}
