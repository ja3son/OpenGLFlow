package com.ja3son.demo.objects.cone;

//���������η������Ĺ�����
public class VectorUtil {
	
	public static float[] calConeNormal
	(//����Բ׶��ָ���ⶥ�㷨�����ķ���
			float x0,float y0,float z0,//A�����ĵ�(����Բ��Բ��)
			float x1,float y1,float z1,//B������Բ�ϵ�ĳһ��
			float x2,float y2,float z2 //C��Բ׶������ߵ�
	)
	{
		float[] a={x1-x0, y1-y0, z1-z0};//����AB
		float[] b={x2-x0, y2-y0, z2-z0};//����AC
		float[] c={x2-x1, y2-y1, z2-z1};//����BC
		float[] k=crossTwoVectors(a,b);//����ƽ��ABC�ķ�����k
		
		float[] d=crossTwoVectors(c,k);//��c��k����ˣ��ó���������d
		return normalizeVector(d);//���ع�񻯺�ķ�����
	}
	//������񻯵ķ���
	public static float[] normalizeVector(float [] vec){//������񻯵ķ���
		float mod=module(vec);//��������ģ
		return new float[]{vec[0]/mod, vec[1]/mod, vec[2]/mod};//���ع�񻯺������
	}
	
	public static float module(float [] vec){//��������ģ�ķ���
		return (float) Math.sqrt(vec[0]*vec[0]+vec[1]*vec[1]+vec[2]*vec[2]);
	}
	
	public static float[] crossTwoVectors(//��������������ķ���
			float[] a,
			float[] b)
	{
		float x=a[1]*b[2]-a[2]*b[1];//���������X����
		float y=a[2]*b[0]-a[0]*b[2];//���������Y����
		float z=a[0]*b[1]-a[1]*b[0];//���������Z����
		return new float[]{x, y, z};//���ز������
	}
}
