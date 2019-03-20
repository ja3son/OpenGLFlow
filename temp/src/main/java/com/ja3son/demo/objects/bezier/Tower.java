package com.ja3son.demo.objects.bezier;

/*
 * ����������
 */
public class Tower {
    float scale;//���Ĵ�С
    boolean texFlag;//�Ƿ��������ı�־λ

    TowerPart1 tower1;//���ĵ�һ����
    TowerPart2 tower2;//����Բ������
    TowerPart3 tower3;//

    float xAngle = 0;//��x����ת�ĽǶ�
    float yAngle = 0;//��y����ת�ĽǶ�
    float zAngle = 0;//��z����ת�ĽǶ�

    Tower(MySurfaceView mv, float scale, int nCol, int nRow) {
        this.scale = scale;//��С��ֵ
        //��������
        tower1 = new TowerPart1(mv, 0.4f * scale, nCol, nRow);//񷶥�ĵ�һ����
        tower2 = new TowerPart2(mv, 0.35f * scale, nCol, nRow);//񷶥�ĵڶ�����
        tower3 = new TowerPart3(mv, 0.4f * scale, nCol, nRow);//񷶥�ĵڶ�����

    }

    public void drawSelf(int texId) {
        MatrixState.rotate(xAngle, 1, 0, 0);
        MatrixState.rotate(yAngle, 0, 1, 0);
        MatrixState.rotate(zAngle, 0, 0, 1);

        //���ĵ�һ���֡���񷶥
        MatrixState.pushMatrix();
        MatrixState.translate(0f, 3.0f * scale, 0f);
        tower1.drawSelf(texId);//񷶥�ĵ�һ����
        MatrixState.popMatrix();

        //**************���ĵڶ����֡����ĸ�Բ��****************************************
        //�ĸ�Բ��
        MatrixState.pushMatrix();
        MatrixState.translate(0.62f * scale, 2.15f * scale, 0.62f * scale);
        tower2.drawSelf(texId);//񷶥�ĵ�һ����
        MatrixState.popMatrix();

        MatrixState.pushMatrix();
        MatrixState.translate(-0.62f * scale, 2.15f * scale, 0.62f * scale);
        tower2.drawSelf(texId);//񷶥�ĵ�һ����
        MatrixState.popMatrix();

        MatrixState.pushMatrix();
        MatrixState.translate(0.62f * scale, 2.15f * scale, -0.62f * scale);
        tower2.drawSelf(texId);//񷶥�ĵ�һ����
        MatrixState.popMatrix();

        MatrixState.pushMatrix();
        MatrixState.translate(-0.62f * scale, 2.15f * scale, -0.62f * scale);
        tower2.drawSelf(texId);//񷶥�ĵ�һ����
        MatrixState.popMatrix();
        //**************���ĵڶ�����****************************************

        //���ĵ�һ����
        MatrixState.pushMatrix();
        MatrixState.translate(0f, 0.7f * scale, 0f);
        tower3.drawSelf(texId);
        MatrixState.popMatrix();

        MatrixState.pushMatrix();
        MatrixState.translate(0f, -1.4f * scale, 0f);
        tower3.drawSelf(texId);
        MatrixState.popMatrix();

        MatrixState.pushMatrix();
        MatrixState.translate(0f, -3.55f * scale, 0f);
        tower3.drawSelf(texId);
        MatrixState.popMatrix();
    }

}
