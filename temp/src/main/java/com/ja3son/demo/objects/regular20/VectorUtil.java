package com.ja3son.demo.objects.regular20;//������

import java.util.ArrayList;//����������

//���������η������Ĺ�����
public class VectorUtil {
	//������񻯵ķ���
	public static float[] normalizeVector(float [] vec){
		float mod=module(vec);
		return new float[]{vec[0]/mod, vec[1]/mod, vec[2]/mod};//���ع�񻯺������
	}
	//��������ģ�ķ���
	public static float module(float [] vec){
		return (float) Math.sqrt(vec[0]*vec[0]+vec[1]*vec[1]+vec[2]*vec[2]);
	}
	//����������˵ķ���
	public static float[] crossTwoVectors(float[] a, float[] b)
	{
		float x=a[1]*b[2]-a[2]*b[1];
		float y=a[2]*b[0]-a[0]*b[2];
		float z=a[0]*b[1]-a[1]*b[0];
		return new float[]{x, y, z};//���ز�˽��
	}
	
	public static float dotTwoVectors(float[] a, float[] b)
	{//������������ķ���
		return a[0]*b[0]+a[1]*b[1]+a[2]*b[2];//���ص�����
	}

	//���ݶ��������ɾ��ƺ󶥵�������������ķ���
	public static float[] cullTexCoor(
			ArrayList<Float> alST,
			ArrayList<Integer> alTexIndex
			)
	{
		float[] textures=new float[alTexIndex.size()*2];//���������������
		
		int stCount=0;//�������������
		for(int i:alTexIndex){//�Զ������б����ѭ��
			textures[stCount++]=alST.get(2*i);//����ǰ��Ŷ����S����ֵ������������
			textures[stCount++]=alST.get(2*i+1);//����ǰ��Ŷ����T����ֵ������������
		}
		return textures;//���ؽ��������������
	}
	//���ݶ��������ɾ��ƺ󶥵���������ķ���
	public static float[] cullVertex(
			ArrayList<Float> alv,
			ArrayList<Integer> alFaceIndex
			)
	{
		float[] vertices=new float[alFaceIndex.size()*3];//��ž��ƺ󶥵�����ֵ������
		
		int vCount=0;//���������
		for(int i:alFaceIndex){//�Զ������б����ѭ��
			vertices[vCount++]=alv.get(3*i);//����ǰ��Ŷ����X����ֵ������������
			vertices[vCount++]=alv.get(3*i+1);//����ǰ��Ŷ����Y����ֵ������������
			vertices[vCount++]=alv.get(3*i+2);//����ǰ��Ŷ����Z����ֵ������������
		}
		return vertices;//���ؽ��������������
	}
	//����Բ����n�ȷֵ�����ķ�����rΪ�뾶��startΪ�����ĵ�Բ����ʼ���������
	//endΪ�����ĵ�Բ���յ��������nΪ�зֵ��ܷ�����iΪ������Ӧ�ķ������
	public static float[] devideBall(
			float r, //��İ뾶
			float[] start, //ָ��Բ����������
			float[] end, //ָ��Բ���յ������
			int n, //Բ���ֵķ���
			int i //���i����Բ���ϵ����꣨iΪ0��nʱ�ֱ���������յ����꣩
			)
	{
		/* 
		 * ��������������Ĺ���������ٳ��԰뾶r����
		 * s0*x+s1*y+s2*z=cos(angle1)//����������������������н�Ϊangle1---1ʽ
		 * e0*x+e1*y+e2*z=cos(angle2)//���������������յ������н�Ϊangle2---2ʽ
		 * x*x+y*y+z*z=1//���������Ĺ������ģΪ1---3ʽ
		 * x*n0+y*n1+z*n2=0//���������뷨������ֱ---4ʽ
		 * �㷨Ϊ����1��2��ʽ�û�Ԫ���ó�x=a1+b1*z��y=a2+b2*z����ʽ��
		 * �������4ʽ���z�������x��y���������(x,y,z)����r��Ϊ�������ꡣ
		 * 1ʽ��2ʽ�ǽ�3ʽ����õ��ģ�����Ѿ������ˡ�
		 * ���ڲ�˵Ľ�����˷�ĸ�������㡢�յ㡢�������㲻�ܹ���
		 * ע�����ǽ��ӻ��ȷ�
		 */
		//�Ƚ�ָ�������յ���������
		float[] s=VectorUtil.normalizeVector(start);//�Ƚ�ָ�������յ���������
		float[] e=VectorUtil.normalizeVector(end);
		if(n==0){	//���nΪ�㣬�����������
			return new float[]{s[0]*r, s[1]*r, s[2]*r};
		}
		//�����������ļн�
		double angrad=Math.acos(VectorUtil.dotTwoVectors(s, e));//����յ������н�
		double angrad1=angrad*i/n;//������������������ļн�
		double angrad2=angrad-angrad1;//�����������յ������ļн�
		
		float[] normal=VectorUtil.crossTwoVectors(s, e);//����s��e��������������
	
		double matrix[][]={//��doolittle�ֽ��㷨��nԪһ�����Է����������ϵ������
				{s[0],s[1],s[2],Math.cos(angrad1)},
				{e[0],e[1],e[2],Math.cos(angrad2)},
				{normal[0],normal[1],normal[2],0}  
		};
		double result[]=MyMathUtil.doolittle(matrix);//��nԪһ�����Է�����
		//��������xyz��ֵ
		float x=(float) result[0];//�õ������ĵ�����������Ĺ�񻯰汾
		float y=(float) result[1];
		float z=(float) result[2];
		
		return new float[]{x*r, y*r, z*r};//�õ����������겢����
	}
	//�����߶ε�n�ȷֵ�����ķ�����startΪ�߶�������꣬endΪ�߶��յ�����
	//nΪ�зֵ��ܷ�����iΪ������Ӧ�ķ������
	public static float[] devideLine(
			float[] start, //�߶��������
			float[] end, //�߶��յ�����
			int n, //�߶ηֵķ���
			int i //���i�����߶��ϵ����꣨iΪ0��nʱ�ֱ���������յ����꣩
			)
	{
		if(n==0){//���nΪ�㣬�����������
			return start;
		}
		//����㵽�յ������
		float[] ab=new float[]{end[0]-start[0], end[1]-start[1], end[2]-start[2]};
		
		float vecRatio=i/(float)n;//����������
		//����㵽����������
		float[] ac=new float[]{ab[0]*vecRatio, ab[1]*vecRatio, ab[2]*vecRatio};
		//�õ����������
		float x=start[0]+ac[0];
		float y=start[1]+ac[1];
		float z=start[2]+ac[2];
		//�����߶����������
		return new float[]{x, y, z};
	}
}
