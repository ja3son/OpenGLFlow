package com.ja3son.demo.objects.c60.util;//������

public class ZQTEdgeUtil 
{
	public static float STANDARD_EDGE_LENGTH=2;//ԭʼԲ������
	public static float[] ZHU_VECTOR_NORMAL={1,0,0}; 	//ԭʼԲ����������
	
	public static float[] calTranslateRotateScale(float[] ab)
	{		
		//��ֳ�Ŀ��߶�ӦԲ��AB���˵������
		float xa=ab[0];
		float ya=ab[1];
		float za=ab[2];
		float xb=ab[3];
		float yb=ab[4];
		float zb=ab[5];
		//����A�㵽B�������
		float[] abVector={xb-xa,yb-ya,zb-za};
		//���AB����
		float[] normalAB=VectorUtil.vectorNormal(abVector);
		//AB�������ԭʼԲ������
		float[] normalABCrossZhu=VectorUtil.vectorNormal
		(
			VectorUtil.getCrossProduct
		    (
		    	normalAB[0],normalAB[1],normalAB[2],
		    	ZHU_VECTOR_NORMAL[0], ZHU_VECTOR_NORMAL[1], ZHU_VECTOR_NORMAL[2]
		    )
		);
		//��AB������ԭʼԲ�������ļн�
		float angle=(float)Math.toDegrees(VectorUtil.angle(normalAB, ZHU_VECTOR_NORMAL));
		
		float xABZ=(xa+xb)/2;//��AB�����е�
		float yABZ=(ya+yb)/2;//��AB�����е�
		float zABZ=(za+zb)/2;//��AB�����е�
		//�󳤶�����ֵ
		float scale=VectorUtil.mould(abVector)/STANDARD_EDGE_LENGTH;//�󳤶�����ֵ		
		final float angleThold=0.8f;	//��ת����ֵ	
		//��ת��Ϊ0�Ȼ�180��ʱ����Ҫ��ת
		if(Math.abs(angle)>angleThold&&Math.abs(angle)<180-angleThold){			
			return new float[]{
				xABZ,yABZ,zABZ,
				-angle,normalABCrossZhu[0],normalABCrossZhu[1],normalABCrossZhu[2],
				scale,1,1
			};
		}
		else{			
			return new float[]{
				xABZ,yABZ,zABZ,//ƽ����Ϣ
				0,0,0,1,//��ת��Ϣ
				scale,1,1//������Ϣ
			};
		}
	}
}
