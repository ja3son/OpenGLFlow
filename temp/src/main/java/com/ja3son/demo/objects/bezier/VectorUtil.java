package com.ja3son.demo.objects.bezier;

import java.util.ArrayList;

//��ά����ϵ�з������Ĺ�����
public class VectorUtil {

    //����ԭ���������������������ƺ������ķ���
    public static float[] calTextures(ArrayList<Float> alST,//ԭ���������б�δ���ƣ�
                                      ArrayList<Integer> alTexIndex//��֯������������������ֵ�б�����ʱ����ƣ�
    ) {
        float[] textures = new float[alTexIndex.size() * 2];
        //���ɶ��������
        int stCount = 0;
        for (int i : alTexIndex) {
            textures[stCount++] = alST.get(2 * i);
            textures[stCount++] = alST.get(2 * i + 1);
        }
        return textures;
    }

    public static float[] calVertices(ArrayList<Float> alv,//ԭ�����б�δ���ƣ�
                                      ArrayList<Integer> alFaceIndex//��֯����Ķ��������ֵ�б�����ʱ����ƣ�
    ) {
        float[] vertices = new float[alFaceIndex.size() * 3];
        //���ɶ��������
        int vCount = 0;
        for (int i : alFaceIndex) {
            vertices[vCount++] = alv.get(3 * i);
            vertices[vCount++] = alv.get(3 * i + 1);
            vertices[vCount++] = alv.get(3 * i + 2);
        }
        return vertices;
    }

}
