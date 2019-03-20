package com.ja3son.demo.objects.bezier;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import android.opengl.GLES30;

/*
 * ̩���궥���齨3
 */
public class TowerPart3 {
    int mProgram;//�Զ�����Ⱦ������ɫ������id
    int muMVPMatrixHandle;//�ܱ任��������
    int maPositionHandle; //����λ����������
    int maTexCoorHandle; //��������������������

    String mVertexShader;//������ɫ������ű�  	 
    String mFragmentShader;//ƬԪ��ɫ������ű�

    FloatBuffer mVertexBuffer;//�����������ݻ���
    FloatBuffer mTexCoorBuffer;//���������������ݻ���

    int vCount = 0;
    float xAngle = 0;//��x����ת�ĽǶ�
    float yAngle = 0;//��y����ת�ĽǶ�
    float zAngle = 0;//��z����ת�ĽǶ�

    float scale;

    public TowerPart3(MySurfaceView mv, float scale, int nCol, int nRow) {
        this.scale = scale;
        //���ó�ʼ���������ݵ�initVertexData����
        initVertexData(scale, nCol, nRow);
        //���ó�ʼ����ɫ����intShader����
        initShader(mv);
    }

    //�Զ���ĳ�ʼ���������ݵķ���
    public void initVertexData(float scale, int nCol, int nRow //��С������������������Ҫ��֤���Ա�1������
    ) {
        //��Ա������ʼ��
        float angdegSpan = 360.0f / nCol;
        vCount = 3 * nCol * nRow * 2;//�������������nColumn*nRow*2�������Σ�ÿ�������ζ�����������
        //�������ݳ�ʼ��
        ArrayList<Float> alVertix = new ArrayList<Float>();//ԭ�����б�δ���ƣ�
        ArrayList<Integer> alFaceIndex = new ArrayList<Integer>();//��֯����Ķ��������ֵ�б�����ʱ����ƣ�

        //�����Ǳ��������ߵ�ʵ�ִ���
        BezierUtil.al.clear();//������ݵ��б�

        //�������ݵ�
        BezierUtil.al.add(new BNPosition(87, 22));
        BezierUtil.al.add(new BNPosition(83, 229));
        BezierUtil.al.add(new BNPosition(77, 226));
        BezierUtil.al.add(new BNPosition(72, 205));
        BezierUtil.al.add(new BNPosition(75, 233));
        BezierUtil.al.add(new BNPosition(137, 240));
        BezierUtil.al.add(new BNPosition(94, 212));
        BezierUtil.al.add(new BNPosition(65, 248));
        BezierUtil.al.add(new BNPosition(78, 245));

        //ͨ�����ݵ㣬��ȡ�����������ϵĵ���б�
        ArrayList<BNPosition> alCurve = BezierUtil.getBezierData(1.0f / nRow);
        //����
        for (int i = 0; i < nRow + 1; i++) {
            double r = alCurve.get(i).x * Constant.DATA_RATIO * scale;//��ǰԲ�İ뾶
            float y = alCurve.get(i).y * Constant.DATA_RATIO * scale;//��ǰyֵ
            for (float angdeg = 0; Math.ceil(angdeg) < 360 + angdegSpan;
                 angdeg += angdegSpan)//�ظ���һ�ж��㣬�����������ļ���
            {
                double angrad = Math.toRadians(angdeg);//��ǰ�л���
                float x = (float) (-r * Math.sin(angrad));
                float z = (float) (-r * Math.cos(angrad));
                //�����������XYZ��������Ŷ��������ArrayList
                alVertix.add(x);
                alVertix.add(y);
                alVertix.add(z);
            }
        }
        //����
        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                int index = i * (nCol + 1) + j;//��ǰ����
                //��������
                alFaceIndex.add(index + 1);//��һ��---1
                alFaceIndex.add(index + nCol + 2);//��һ����һ��---3
                alFaceIndex.add(index + nCol + 1);//��һ��---2

                alFaceIndex.add(index + 1);//��һ��---1
                alFaceIndex.add(index + nCol + 1);//��һ��---2
                alFaceIndex.add(index);//��ǰ---0
            }
        }
        //������ƶ���
        float[] vertices = new float[vCount * 3];
        vertices = VectorUtil.calVertices(alVertix, alFaceIndex);

        //�����������ݳ�ʼ��
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);//���������������ݻ���
        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mVertexBuffer = vbb.asFloatBuffer();//ת��Ϊfloat�ͻ���
        mVertexBuffer.put(vertices);//�򻺳����з��붥����������
        mVertexBuffer.position(0);//���û�������ʼλ��

        //����
        ArrayList<Float> alST = new ArrayList<Float>();//ԭ�����б�δ���ƣ�

        float yMin = 999999999;//y��Сֵ
        float yMax = 0;//y���ֵ
        for (BNPosition pos : alCurve) {
            yMin = Math.min(yMin, pos.y);//y��Сֵ
            yMax = Math.max(yMax, pos.y);//y���ֵ
        }
        for (int i = 0; i < nRow + 1; i++) {
            float y = alCurve.get(i).y;//��ǰyֵ
            float t = 1 - (y - yMin) / (yMax - yMin);//t����
            for (float angdeg = 0; Math.ceil(angdeg) < 360 + angdegSpan;
                 angdeg += angdegSpan)//�ظ���һ���������꣬�������ļ���
            {
                float s = angdeg / 360;//s����
                //�����������ST��������Ŷ��������ArrayList
                alST.add(s);
                alST.add(t);
            }
        }
        //������ƺ���������
        float[] textures = VectorUtil.calTextures(alST, alFaceIndex);
        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length * 4);//���������������ݻ���
        tbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mTexCoorBuffer = tbb.asFloatBuffer();//ת��Ϊfloat�ͻ���
        mTexCoorBuffer.put(textures);//�򻺳����з��붥����������
        mTexCoorBuffer.position(0);//���û�������ʼλ��
    }

    //�Զ����ʼ����ɫ��initShader����
    public void initShader(MySurfaceView mv) {
        //���ض�����ɫ���Ľű�����
        mVertexShader = ShaderUtil.loadFromAssetsFile("vertex_tex.sh", mv.getResources());
        //����ƬԪ��ɫ���Ľű�����
        mFragmentShader = ShaderUtil.loadFromAssetsFile("frag_tex.sh", mv.getResources());
        //���ڶ�����ɫ����ƬԪ��ɫ����������
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //��ȡ�����ж���λ����������id  
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //��ȡ�����ж�������������������id  
        maTexCoorHandle = GLES30.glGetAttribLocation(mProgram, "aTexCoor");
        //��ȡ�������ܱ任��������id
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    public void drawSelf(int texId) {
        MatrixState.rotate(xAngle, 1, 0, 0);
        MatrixState.rotate(yAngle, 0, 1, 0);
        MatrixState.rotate(zAngle, 0, 0, 1);

        //�ƶ�ʹ��ĳ��shader����
        GLES30.glUseProgram(mProgram);

        //�����ձ任������shader����
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);

        //���Ͷ���λ������
        GLES30.glVertexAttribPointer(maPositionHandle, 3, GLES30.GL_FLOAT, false, 3 * 4, mVertexBuffer);
        //���Ͷ���������������
        GLES30.glVertexAttribPointer(maTexCoorHandle, 2, GLES30.GL_FLOAT, false, 2 * 4, mTexCoorBuffer);

        //���ö���λ������
        GLES30.glEnableVertexAttribArray(maPositionHandle);
        //���ö�����������
        GLES30.glEnableVertexAttribArray(maTexCoorHandle);

        //������
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);

        //�����������
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount);
    }
}
