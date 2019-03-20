package com.ja3son.demo.objects.regular20;


import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

/*
 * ����ʮ����
 * �����������ഹֱ�Ļƽ𳤷���
 */
public class Regular20 {
    int mProgram;//�Զ�����Ⱦ������ɫ������id
    int muMVPMatrixHandle;//�ܱ任��������
    int maPositionHandle; //����λ����������
    int maTexCoorHandle; //��������������������
    int muMMatrixHandle;

    int maCameraHandle; //�����λ���������� 
    int maNormalHandle; //���㷨������������ 
    int maLightLocationHandle;//��Դλ����������  


    String mVertexShader;//������ɫ������ű�  	 
    String mFragmentShader;//ƬԪ��ɫ������ű�

    FloatBuffer mVertexBuffer;//�����������ݻ���
    FloatBuffer mTexCoorBuffer;//���������������ݻ���
    FloatBuffer mNormalBuffer;//���㷨�������ݻ���
    int vCount = 0;
    float xAngle = 0;//��x����ת�ĽǶ�
    float yAngle = 0;//��y����ת�ĽǶ�
    float zAngle = 0;//��z����ת�ĽǶ�


    float bHalf = 0;//�ƽ𳤷��εĿ�
    float r = 0;//��İ뾶

    public Regular20(MySurfaceView mv, float scale, float aHalf, int n) {
        //���ó�ʼ���������ݵ�initVertexData����
        initVertexData(scale, aHalf, n);
        //���ó�ʼ����ɫ����intShader����
        initShader(mv);
    }

    public void initVertexData(float scale, float aHalf, int n) {//�Զ���ĳ�ʼ���������ݵķ���
        aHalf *= scale;        //�ƽ𳤷��γ��ߵ�һ��
        bHalf = aHalf * 0.618034f;    //�ƽ𳤷��ζ̱ߵ�һ��
        r = (float) Math.sqrt(aHalf * aHalf + bHalf * bHalf);//������İ뾶
        vCount = 3 * 20 * n * n;//�������

        ArrayList<Float> alVertix20 = new ArrayList<Float>();//����ʮ����Ķ����б�
        ArrayList<Integer> alFaceIndex20 = new ArrayList<Integer>();//���ھ��ƹ�������ʮ������������εĶ������б�

        initAlVertix20(alVertix20, aHalf, bHalf);//��ʼ������ʮ����Ķ�����������

        initAlFaceIndex20(alFaceIndex20);//��ʼ�����ھ��ƹ�������ʮ������������εĶ������б�
        //��������ʮ����ĸ��������ζ����������������
        float[] vertices20 = VectorUtil.cullVertex(alVertix20, alFaceIndex20);

        ArrayList<Float> alVertix = new ArrayList<Float>();//������ԭʼ�����б�
        ArrayList<Integer> alFaceIndex = new ArrayList<Integer>();//���ɼ�����ĸ������ζ������б�
        int vnCount = 0;//���������
        for (int k = 0; k < vertices20.length; k += 9)//������ʮ�����е�ÿ��������ѭ��
        {
            float[] v1 = new float[]{vertices20[k + 0], vertices20[k + 1], vertices20[k + 2]};    //��ǰ������3��
            float[] v2 = new float[]{vertices20[k + 3], vertices20[k + 4], vertices20[k + 5]};//���������
            float[] v3 = new float[]{vertices20[k + 6], vertices20[k + 7], vertices20[k + 8]};

            for (int i = 0; i <= n; i++) {//�����зֵķ������������ԭʼ���������
                float[] viStart = VectorUtil.devideBall(r, v1, v2, n, i);//��Բ�������з�
                float[] viEnd = VectorUtil.devideBall(r, v1, v3, n, i);//��Բ�������з�
                for (int j = 0; j <= i; j++) {
                    float[] vi = VectorUtil.devideBall(r, viStart, viEnd, i, j);//��Բ�������з�
                    alVertix.add(vi[0]);
                    alVertix.add(vi[1]);
                    alVertix.add(vi[2]);//���������ԭʼ�����б�
                }
            }

            for (int i = 0; i < n; i++) {//ѭ�����ɹ��ɼ�������������εĶ������б�
                if (i == 0) {//���ǵ�0�У�������012
                    alFaceIndex.add(vnCount + 0);
                    alFaceIndex.add(vnCount + 1);
                    alFaceIndex.add(vnCount + 2);
                    vnCount += 1;//�����������1
                    if (i == n - 1) { //���������ʮ���������ε����һ��ѭ��������һ���Ķ������Ҳ����
                        vnCount += 2;
                    }
                    continue;
                }
                int iStart = vnCount;//��i�п�ʼ�ı��(�������ָ����ƽ��չ��ͼ�е���)
                int viCount = i + 1;//��i�ж�����
                int iEnd = iStart + viCount - 1;//��i�н���������

                int iStartNext = iStart + viCount;//��i+1�п�ʼ�Ķ�����
                int viCountNext = viCount + 1;//��i+1�ж�����
                int iEndNext = iStartNext + viCountNext - 1;//��i+1�н����Ķ�����

                for (int j = 0; j < viCount - 1; j++) {//ǰ����ı���
                    int index0 = iStart + j;//�ı���4������ı��
                    int index1 = index0 + 1;
                    int index2 = iStartNext + j;
                    int index3 = index2 + 1;
                    //���ı���4��������Ƴ�����������
                    alFaceIndex.add(index0);
                    alFaceIndex.add(index2);
                    alFaceIndex.add(index3);
                    alFaceIndex.add(index0);
                    alFaceIndex.add(index3);
                    alFaceIndex.add(index1);
                }
                //���һ��������3������ı��
                alFaceIndex.add(iEnd);
                alFaceIndex.add(iEndNext - 1);
                alFaceIndex.add(iEndNext); //���һ��������
                vnCount += viCount;//��i��ǰ���ж������ĺ�
                if (i == n - 1) { //���������ʮ���������ε����һ��ѭ��������һ���Ķ������Ҳ����
                    vnCount += viCountNext;
                }
            }
        }

        //�������ɵĶ�����������䶥��������������
        float[] vertices = VectorUtil.cullVertex(alVertix, alFaceIndex);
        float[] normals = vertices;//����������Ƿ�����

        ArrayList<Float> alST20 = new ArrayList<Float>();//����ʮ���嶥���ԭʼ���������б�
        ArrayList<Integer> alTexIndex20 = new ArrayList<Integer>();//����ʮ���������������б�

        float sSpan = 1 / 5.5f;//ÿ�����������εı߳�
        float tSpan = 1 / 3.0f;//ÿ�����������εĸ�
        //����4��ѭ������������ʮ����չ��������������������
        for (int i = 0; i < 5; i++) {
            alST20.add(sSpan + sSpan * i);
            alST20.add(0f);
        }
        for (int i = 0; i < 6; i++) {
            alST20.add(sSpan / 2 + sSpan * i);
            alST20.add(tSpan);
        }
        for (int i = 0; i < 6; i++) {
            alST20.add(sSpan * i);
            alST20.add(tSpan * 2);
        }
        for (int i = 0; i < 5; i++) {
            alST20.add(sSpan / 2 + sSpan * i);
            alST20.add(tSpan * 3);
        }

        initAlTexIndex20(alTexIndex20); //�����������ʮ������������ζ���������������б�

        //�������ɵĶ�����������䶥�����������������飨����ʮ����ģ�
        float[] st20 = VectorUtil.cullTexCoor(alST20, alTexIndex20);
        ArrayList<Float> alST = new ArrayList<Float>(); //�������ԭʼ���������б�δ���ƣ�
        for (int k = 0; k < st20.length; k += 6) {//������ʮ����ĸ��������ν���ѭ��
            float[] st1 = new float[]{st20[k + 0], st20[k + 1], 0};//ȡ����ǰ������
            float[] st2 = new float[]{st20[k + 2], st20[k + 3], 0};//3���������������
            float[] st3 = new float[]{st20[k + 4], st20[k + 5], 0};
            for (int i = 0; i <= n; i++) {//������ʮ����ƽ��չ��ͼ�ı߽����з�
                float[] stiStart = VectorUtil.devideLine(st1, st2, n, i);
                float[] stiEnd = VectorUtil.devideLine(st1, st3, n, i);
                for (int j = 0; j <= i; j++) {//���㼸����ƽ��չ��ͼ�����Ӧ����������
                    float[] sti = VectorUtil.devideLine(stiStart, stiEnd, i, j);

                    alST.add(sti[0]);
                    alST.add(sti[1]);//��������������б�
                }
            }
        }
        //�������ɵĶ�����������䶥�����������������飨������ģ�
        float[] textures = VectorUtil.cullTexCoor(alST, alFaceIndex);

        //�����������ݳ�ʼ��
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);//���������������ݻ���
        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��Ϊ���ز���ϵͳ˳��
        mVertexBuffer = vbb.asFloatBuffer();//ת��Ϊfloat�ͻ���
        mVertexBuffer.put(vertices);//�򻺳����з��붥����������
        mVertexBuffer.position(0);//���û�������ʼλ��
        //���������ݳ�ʼ��  
        ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length * 4);//�������㷨�������ݻ���
        nbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��Ϊ���ز���ϵͳ˳��
        mNormalBuffer = nbb.asFloatBuffer();//ת��Ϊfloat�ͻ���
        mNormalBuffer.put(normals);//�򻺳����з��붥�㷨��������
        mNormalBuffer.position(0);//���û�������ʼλ��
        //st�������ݳ�ʼ��		
        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length * 4);//���������������ݻ���
        tbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��Ϊ���ز���ϵͳ˳��
        mTexCoorBuffer = tbb.asFloatBuffer();//ת��Ϊfloat�ͻ���
        mTexCoorBuffer.put(textures);//�򻺳����з��붥����������
        mTexCoorBuffer.position(0);//���û�������ʼλ��
    }

    public void initAlVertix20(ArrayList<Float> alVertix20, float aHalf, float bHalf) {

        alVertix20.add(0f);
        alVertix20.add(aHalf);
        alVertix20.add(-bHalf);//��Ӧͼ8-17��1�ŵ�

        alVertix20.add(0f);
        alVertix20.add(aHalf);
        alVertix20.add(bHalf);//��Ӧͼ8-17��2�ŵ�
        alVertix20.add(aHalf);
        alVertix20.add(bHalf);
        alVertix20.add(0f);//��Ӧͼ8-17��3�ŵ�
        alVertix20.add(bHalf);
        alVertix20.add(0f);
        alVertix20.add(-aHalf);//��Ӧͼ8-17��4�ŵ�
        alVertix20.add(-bHalf);
        alVertix20.add(0f);
        alVertix20.add(-aHalf);//��Ӧͼ8-17��5�ŵ�
        alVertix20.add(-aHalf);
        alVertix20.add(bHalf);
        alVertix20.add(0f);//��Ӧͼ8-17��6�ŵ�

        alVertix20.add(-bHalf);
        alVertix20.add(0f);
        alVertix20.add(aHalf);//��Ӧͼ8-17��7�ŵ�
        alVertix20.add(bHalf);
        alVertix20.add(0f);
        alVertix20.add(aHalf);//��Ӧͼ8-17��8�ŵ�
        alVertix20.add(aHalf);
        alVertix20.add(-bHalf);
        alVertix20.add(0f);//��Ӧͼ8-17��9�ŵ�
        alVertix20.add(0f);
        alVertix20.add(-aHalf);
        alVertix20.add(-bHalf);//��Ӧͼ8-17��10�ŵ�
        alVertix20.add(-aHalf);
        alVertix20.add(-bHalf);
        alVertix20.add(0f);//��Ӧͼ8-17��11�ŵ�

        alVertix20.add(0f);
        alVertix20.add(-aHalf);
        alVertix20.add(bHalf);//��Ӧͼ8-17��12�ŵ�

    }

    public void initAlFaceIndex20(ArrayList<Integer> alFaceIndex20) { //��ʼ������ʮ����Ķ�����������
        //��һ��5�������εĸ��������������
        alFaceIndex20.add(0);
        alFaceIndex20.add(1);
        alFaceIndex20.add(2);
        alFaceIndex20.add(0);
        alFaceIndex20.add(2);
        alFaceIndex20.add(3);
        alFaceIndex20.add(0);
        alFaceIndex20.add(3);
        alFaceIndex20.add(4);
        alFaceIndex20.add(0);
        alFaceIndex20.add(4);
        alFaceIndex20.add(5);
        alFaceIndex20.add(0);
        alFaceIndex20.add(5);
        alFaceIndex20.add(1);
        //�ڶ���10�������εĸ��������������
        alFaceIndex20.add(1);
        alFaceIndex20.add(6);
        alFaceIndex20.add(7);
        alFaceIndex20.add(1);
        alFaceIndex20.add(7);
        alFaceIndex20.add(2);
        alFaceIndex20.add(2);
        alFaceIndex20.add(7);
        alFaceIndex20.add(8);
        alFaceIndex20.add(2);
        alFaceIndex20.add(8);
        alFaceIndex20.add(3);
        alFaceIndex20.add(3);
        alFaceIndex20.add(8);
        alFaceIndex20.add(9);
        alFaceIndex20.add(3);
        alFaceIndex20.add(9);
        alFaceIndex20.add(4);
        alFaceIndex20.add(4);
        alFaceIndex20.add(9);
        alFaceIndex20.add(10);
        alFaceIndex20.add(4);
        alFaceIndex20.add(10);
        alFaceIndex20.add(5);
        alFaceIndex20.add(5);
        alFaceIndex20.add(10);
        alFaceIndex20.add(6);
        alFaceIndex20.add(5);
        alFaceIndex20.add(6);
        alFaceIndex20.add(1);
        //������5�������εĸ��������������
        alFaceIndex20.add(6);
        alFaceIndex20.add(11);
        alFaceIndex20.add(7);
        alFaceIndex20.add(7);
        alFaceIndex20.add(11);
        alFaceIndex20.add(8);
        alFaceIndex20.add(8);
        alFaceIndex20.add(11);
        alFaceIndex20.add(9);
        alFaceIndex20.add(9);
        alFaceIndex20.add(11);
        alFaceIndex20.add(10);
        alFaceIndex20.add(10);
        alFaceIndex20.add(11);
        alFaceIndex20.add(6);
    }

    public void initAlTexIndex20(ArrayList<Integer> alTexIndex20) //��ʼ������������������
    {
        //��һ��5�������εĸ������������������
        alTexIndex20.add(0);
        alTexIndex20.add(5);
        alTexIndex20.add(6);
        alTexIndex20.add(1);
        alTexIndex20.add(6);
        alTexIndex20.add(7);
        alTexIndex20.add(2);
        alTexIndex20.add(7);
        alTexIndex20.add(8);
        alTexIndex20.add(3);
        alTexIndex20.add(8);
        alTexIndex20.add(9);
        alTexIndex20.add(4);
        alTexIndex20.add(9);
        alTexIndex20.add(10);
        ///�ڶ���10�������εĸ������������������
        alTexIndex20.add(5);
        alTexIndex20.add(11);
        alTexIndex20.add(12);
        alTexIndex20.add(5);
        alTexIndex20.add(12);
        alTexIndex20.add(6);
        alTexIndex20.add(6);
        alTexIndex20.add(12);
        alTexIndex20.add(13);
        alTexIndex20.add(6);
        alTexIndex20.add(13);
        alTexIndex20.add(7);
        alTexIndex20.add(7);
        alTexIndex20.add(13);
        alTexIndex20.add(14);
        alTexIndex20.add(7);
        alTexIndex20.add(14);
        alTexIndex20.add(8);
        alTexIndex20.add(8);
        alTexIndex20.add(14);
        alTexIndex20.add(15);
        alTexIndex20.add(8);
        alTexIndex20.add(15);
        alTexIndex20.add(9);
        alTexIndex20.add(9);
        alTexIndex20.add(15);
        alTexIndex20.add(16);
        alTexIndex20.add(9);
        alTexIndex20.add(16);
        alTexIndex20.add(10);
        //������5�������εĸ������������������
        alTexIndex20.add(11);
        alTexIndex20.add(17);
        alTexIndex20.add(12);
        alTexIndex20.add(12);
        alTexIndex20.add(18);
        alTexIndex20.add(13);
        alTexIndex20.add(13);
        alTexIndex20.add(19);
        alTexIndex20.add(14);
        alTexIndex20.add(14);
        alTexIndex20.add(20);
        alTexIndex20.add(15);
        alTexIndex20.add(15);
        alTexIndex20.add(21);
        alTexIndex20.add(16);

    }

    //�Զ����ʼ����ɫ��initShader����
    public void initShader(MySurfaceView mv) {
        //���ض�����ɫ���Ľű�����
        mVertexShader = ShaderUtil.loadFromAssetsFile("vertex_tex_light.sh", mv.getResources());
        //����ƬԪ��ɫ���Ľű�����
        mFragmentShader = ShaderUtil.loadFromAssetsFile("frag_tex_light.sh", mv.getResources());
        //���ڶ�����ɫ����ƬԪ��ɫ����������
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //��ȡ�����ж���λ����������id  
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //��ȡ�����ж�������������������id  
        maTexCoorHandle = GLES30.glGetAttribLocation(mProgram, "aTexCoor");
        //��ȡ�������ܱ任��������id
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");


        //��ȡ�����ж��㷨������������id  
        maNormalHandle = GLES30.glGetAttribLocation(mProgram, "aNormal");
        //��ȡ�����������λ������id
        maCameraHandle = GLES30.glGetUniformLocation(mProgram, "uCamera");
        //��ȡ�����й�Դλ������id
        maLightLocationHandle = GLES30.glGetUniformLocation(mProgram, "uLightLocation");
        //��ȡλ�á���ת�任��������id
        muMMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMMatrix");


    }

    public void drawSelf(int texId) {
        MatrixState.rotate(xAngle, 1, 0, 0);
        MatrixState.rotate(yAngle, 0, 1, 0);
        MatrixState.rotate(zAngle, 0, 0, 1);

        //�ƶ�ʹ��ĳ��shader����
        GLES30.glUseProgram(mProgram);

        //�����ձ任������shader����
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);

        //��λ�á���ת�任������shader����
        GLES30.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);
        //�������λ�ô���shader����
        GLES30.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);
        //����Դλ�ô���shader����
        GLES30.glUniform3fv(maLightLocationHandle, 1, MatrixState.lightPositionFB);


        //���Ͷ���λ������
        GLES30.glVertexAttribPointer
                (
                        maPositionHandle,
                        3,
                        GLES30.GL_FLOAT,
                        false,
                        3 * 4,
                        mVertexBuffer
                );
        //���Ͷ���������������
        GLES30.glVertexAttribPointer
                (
                        maTexCoorHandle,
                        2,
                        GLES30.GL_FLOAT,
                        false,
                        2 * 4,
                        mTexCoorBuffer
                );
        //���Ͷ��㷨��������
        GLES30.glVertexAttribPointer
                (
                        maNormalHandle,
                        4,
                        GLES30.GL_FLOAT,
                        false,
                        3 * 4,
                        mNormalBuffer
                );

        //���ö���λ������
        GLES30.glEnableVertexAttribArray(maPositionHandle);
        //���ö�����������
        GLES30.glEnableVertexAttribArray(maTexCoorHandle);
        //���ö��㷨��������
        GLES30.glEnableVertexAttribArray(maNormalHandle);


        //������
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);

        //�����������
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount);
    }
}
