package com.ja3son.libdemo.Sample7_7;

//���ڴ洢�����������
public class MyVector3f
{
	float x;
	float y;
	float z;
	public MyVector3f(){}
	public MyVector3f(float x,float y,float z)
	{
		this.x=x;
		this.y=y;
		this.z=z;
	}
	public MyVector3f(float[] v)
	{
		this.x=v[0];
		this.y=v[1];
		this.z=v[2];
	}
	//�ӷ�
	public MyVector3f add(MyVector3f v){
		return new MyVector3f(this.x+v.x,this.y+v.y,this.z+v.z);
	}
	//����
	public MyVector3f minus(MyVector3f v){
		return new MyVector3f(this.x-v.x,this.y-v.y,this.z-v.z);
	}

	//�볣�����
	public MyVector3f multiK(float k){
		return new MyVector3f(this.x*k,this.y*k,this.z*k);
	}
	//���
	public void normalize(){
		float mod=module();
		x /= mod;
		y /= mod;
		z /= mod;
	}
	//ģ
	public float module(){
		return (float) Math.sqrt(x*x+y*y+z*z);
	}
}
