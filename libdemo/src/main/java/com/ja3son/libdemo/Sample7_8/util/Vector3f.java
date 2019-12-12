package com.ja3son.libdemo.Sample7_8.util;

/*
 * ����3D�ռ�����������
 */
public class Vector3f {
	//��������������
	public float x;
	public float y;
	public float z;
	//������
	public Vector3f(float x, float y, float z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	//��������񻯵ķ���
	public void normalize(){
		float mod = module();
		//��֤ģ��Ϊ��ʱ�ٹ������
		if(mod != 0){
			x = x/mod;
			y = y/mod;
			z = z/mod;
		}
	}
	//��������ģ�ķ���
	public float module(){
		return (float) Math.sqrt(x*x + y*y + z*z);
	}
	//����
	public Vector3f minus(Vector3f v){
		return new Vector3f(this.x-v.x,this.y-v.y,this.z-v.z);
	}
	
	public Vector3f fu(){
		return new Vector3f(-x,-y,-z);
	}
	
	public boolean tongxiang(Vector3f v)
	{
		Vector3f vt = this;
		vt.normalize();
		v.normalize();
		if(vt.x==v.x&&vt.y==v.y&&vt.z==v.z)
		{
			return true;
		}
		return false;
	}
	//this*v
	public Vector3f crossProduct(Vector3f v)
	{
		return new Vector3f(
				y*v.z-z*v.y,
				z*v.x-x*v.z,
				x*v.y-y*v.x
				);
	}
	//�Ը��������¸�ֵ
	public void voluation(float x,float y,float z)
	{
		this.x=x;
		this.y=y;
		this.z=z;
	}
	//�Ը��������¸�ֵ
		public void voluation(Vector3f v)
		{
			this.x=v.x;
			this.y=v.y;
			this.z=v.z;
		}
	
		public Vector3f roty(double d)//��y�Ḻ������ת
		{
			float xx = (float) (this.x*Math.cos(d)+this.z*Math.sin(d));
			float yy = this.y;
			float zz = (float) (this.x*(-Math.sin(d))+this.z*Math.cos(d));
			return new Vector3f(xx, yy, zz);
		}
//	public Vector3f[] cross(Vector3f down)
//	{
//		
//		
//		return null;
//		
//	}
}
