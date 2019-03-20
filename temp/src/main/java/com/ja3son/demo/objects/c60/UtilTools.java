package com.ja3son.demo.objects.c60;//������

import java.util.ArrayList;//����������

public class UtilTools {

	public UtilTools() {

	}//������

	float bHalf = 0;//�ƽ𳤷��εĿ�
	float r = 0;//����̼�İ뾶
	int count = 0;
	//��С���ƽ𳤷��εĳ���һ�룬�ֶ���
	   public ResultData initVertexData(float scale, float aHalf, int n) {
		ResultData Rda = new ResultData();//����Rda(�������)����
		aHalf *= scale;//�ƽ𳤷��εĳ�
		
		bHalf = aHalf * 0.618034f;//�ƽ𳤷��εĿ�
		r = (float) Math.sqrt(aHalf * aHalf + bHalf * bHalf);//����̼���ӵİ뾶
		//���ɶ�ʮ����Ķ��������б�
		ArrayList<Float> alVertix20 = new ArrayList<Float>();
		//����ʮ������֯����Ķ��������ֵ�б�
		ArrayList<Integer> alFaceIndex20 = new ArrayList<Integer>();
		// ��20���嶥��
		alVertix20.add(0f);alVertix20.add(aHalf);	alVertix20.add(-bHalf);
		alVertix20.add(0f);alVertix20.add(aHalf);	alVertix20.add(bHalf);
		alVertix20.add(aHalf);	alVertix20.add(bHalf);	alVertix20.add(0f);
		alVertix20.add(bHalf);	alVertix20.add(0f);alVertix20.add(-aHalf);
		alVertix20.add(-bHalf);alVertix20.add(0f);alVertix20.add(-aHalf);
		alVertix20.add(-aHalf);alVertix20.add(bHalf);	alVertix20.add(0f);

		alVertix20.add(-bHalf);alVertix20.add(0f);alVertix20.add(aHalf);
		alVertix20.add(bHalf);alVertix20.add(0f);alVertix20.add(aHalf);
		alVertix20.add(aHalf);alVertix20.add(-bHalf);alVertix20.add(0f);
		alVertix20.add(0f);alVertix20.add(-aHalf);alVertix20.add(-bHalf);
		alVertix20.add(-aHalf);alVertix20.add(-bHalf);alVertix20.add(0f);
		alVertix20.add(0f);alVertix20.add(-aHalf);alVertix20.add(bHalf);
		// ��20����������
		alFaceIndex20.add(0);alFaceIndex20.add(1);alFaceIndex20.add(2);// 1������������������
		alFaceIndex20.add(0); alFaceIndex20.add(2); alFaceIndex20.add(3);//2������������������
		alFaceIndex20.add(0);alFaceIndex20.add(3);alFaceIndex20.add(4);// 3������������������
		alFaceIndex20.add(0); alFaceIndex20.add(4); alFaceIndex20.add(5);//4������������������
		alFaceIndex20.add(0);alFaceIndex20.add(5);alFaceIndex20.add(1);// 5������������������

		alFaceIndex20.add(1);	alFaceIndex20.add(6);	alFaceIndex20.add(7);// 6������������������
		alFaceIndex20.add(1); alFaceIndex20.add(7); alFaceIndex20.add(2);//7������������������
		alFaceIndex20.add(2);	alFaceIndex20.add(7);	alFaceIndex20.add(8);// 8������������������
		alFaceIndex20.add(2);	alFaceIndex20.add(8);	alFaceIndex20.add(3);// 9������������������
		 alFaceIndex20.add(3); alFaceIndex20.add(8); alFaceIndex20.add(9);//10������������������
		 
		alFaceIndex20.add(3);	alFaceIndex20.add(9);	alFaceIndex20.add(4);// 11������������������
		alFaceIndex20.add(4); alFaceIndex20.add(9); alFaceIndex20.add(10);//12������������������
		alFaceIndex20.add(4);	alFaceIndex20.add(10);	alFaceIndex20.add(5);// 13������������������
		alFaceIndex20.add(5);	alFaceIndex20.add(10);	alFaceIndex20.add(6);// 14������������������
		alFaceIndex20.add(5); alFaceIndex20.add(6); alFaceIndex20.add(1);//15������������������
		
		alFaceIndex20.add(6);	alFaceIndex20.add(11);alFaceIndex20.add(7);// 16������������������
		alFaceIndex20.add(7); alFaceIndex20.add(11); alFaceIndex20.add(8);//17������������������
		alFaceIndex20.add(8);	alFaceIndex20.add(11);alFaceIndex20.add(9);// 18������������������
		alFaceIndex20.add(9);	alFaceIndex20.add(11);alFaceIndex20.add(10);// 19������������������
		alFaceIndex20.add(10); alFaceIndex20.add(11); alFaceIndex20.add(6);//20������������������
		
		//��������ʮ����������ζ�������
		float[] vertices20 = VectorUtil.cullVertex(alVertix20, alFaceIndex20);
		//���̼ԭ�ӵ������б�
		ArrayList<float[]> AlCAtomicPosition = new ArrayList<float[]>();
		//��Ŵ���ѧ���ıߵĶ˵������б�
		ArrayList<float[]> AlChemicalBondPoints = new ArrayList<float[]>();

		for (int k = 0; k < vertices20.length; k += 9){//������ʮ������ÿ����������ѭ��
			float[] v1 = new float[] { vertices20[k + 0], vertices20[k + 1],
					vertices20[k + 2] };
			float[] v2 = new float[] { vertices20[k + 3], vertices20[k + 4],
					vertices20[k + 5] };
			float[] v3 = new float[] { vertices20[k + 6], vertices20[k + 7],
					vertices20[k + 8] };
			//�����ÿ����������ÿ�����ϵ������������ڴ�ԲԲ���ϵ����ȷֵ�����
			for (int i = 1; i < n; i++) {
				float[] vi1 = VectorUtil.devideBall(r, v1, v2, n, i);//��1��ԲԲ���ϵĵ�i���ȷֵ�����
				vi1 = VectorUtil.normalizeVector(vi1);//���vi1
				float[] vi2 = VectorUtil.devideBall(r, v1, v3, n, i);//��2��ԲԲ���ϵĵ�i���ȷֵ�����
				vi2 = VectorUtil.normalizeVector(vi2);//���vi2
				float[] vi3 = VectorUtil.devideBall(r, v2, v3, n, i);//��3��ԲԲ���ϵĵ�i���ȷֵ�����
				vi3 = VectorUtil.normalizeVector(vi3);//���vi3
				AlCAtomicPosition.add(vi1);
				AlCAtomicPosition.add(vi2);
				AlCAtomicPosition.add(vi3);
			}
			//����õ�6�����ȷֵ���������������������
			AlChemicalBondPoints.add(AlCAtomicPosition.get(count * 6));	 //1�Ŷ��������
			AlChemicalBondPoints.add(AlCAtomicPosition.get(count * 6 + 1));//2�Ŷ��������
			
			AlChemicalBondPoints.add(AlCAtomicPosition.get(count * 6 + 1)); //2�Ŷ��������
			AlChemicalBondPoints.add(AlCAtomicPosition.get(count * 6 + 4)); //5�Ŷ��������
			
			AlChemicalBondPoints.add(AlCAtomicPosition.get(count * 6 + 4)); //5�Ŷ��������
			AlChemicalBondPoints.add(AlCAtomicPosition.get(count * 6 + 5)); //6�Ŷ��������
			
			AlChemicalBondPoints.add(AlCAtomicPosition.get(count * 6 + 5)); //6�Ŷ��������
			AlChemicalBondPoints.add(AlCAtomicPosition.get(count * 6 + 2)); //3�Ŷ��������
			
			AlChemicalBondPoints.add(AlCAtomicPosition.get(count * 6 + 2)); //3�Ŷ��������
			AlChemicalBondPoints.add(AlCAtomicPosition.get(count * 6 + 3)); //4�Ŷ��������
			
			AlChemicalBondPoints.add(AlCAtomicPosition.get(count * 6 + 3)); //4�Ŷ��������
			AlChemicalBondPoints.add(AlCAtomicPosition.get(count * 6)); //1�Ŷ��������
			count+=1;
		}
		//��������ѧ���ıߵĶ˵��������飬ChemicalBondPoints��ÿ��һά���ݴ�������������Ϣ
		Rda.ChemicalBondPoints = new float[AlChemicalBondPoints.size() / 2][6];
		//��AlChemicalBondPoints�е�����ת�浽Rda.ChemicalBondPoints��
		for (int i = 0; i < Rda.ChemicalBondPoints.length; i++) {
			//��AlChemicalBondPoints�еĵ�2*i���������ת�浽Rda.ChemicalBondPoints��
			for (int j = 0; j < 3; j++) {
				Rda.ChemicalBondPoints[i][j] =r * AlChemicalBondPoints.get(2 * i)[j];
			}
			//��AlChemicalBondPoints�еĵ�2*i+1���������ת�浽Rda.ChemicalBondPoints��
			for (int k= 3; k <6; k++) {
				Rda.ChemicalBondPoints[i][k] = r *AlChemicalBondPoints.get(2 * i + 1)[k - 3];
			}
		}
		//����̼ԭ��λ�õ���������Rda.CAtomicPosition
		Rda.CAtomicPosition = new float[AlCAtomicPosition.size()][3];
		for (int i = 0; i < AlCAtomicPosition.size(); i++) {
			for (int j = 0; j < 3; j++) {
				//��AlCAtomicPosition�е�����ת�浽Rda.CAtomicPosition��
				Rda.CAtomicPosition[i][j] = r * AlCAtomicPosition.get(i)[j];
			}
		}
		return Rda;
	}
}
