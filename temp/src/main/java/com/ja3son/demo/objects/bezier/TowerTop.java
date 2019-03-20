package com.ja3son.demo.objects.bezier;

/*
 * ������������
 */
public class TowerTop {
    float scale;//���Ĵ�С

    TowerPart1 tower1;//���ĵ�һ����
    TowerPart2 tower2;//����Բ������
    TowerPart3 tower3;//

    float xAngle = 0;//��x����ת�ĽǶ�
    float yAngle = 0;//��y����ת�ĽǶ�
    float zAngle = 0;//��z����ת�ĽǶ�

    TowerTop(MySurfaceView mv, float scale, int nCol, int nRow) {
        this.scale = scale;//�����С��ֵ

        //��������
        tower1 = new TowerPart1(mv, 0.4f * scale, 20, 40);//񷶥�ĵ�һ����
        tower2 = new TowerPart2(mv, 0.35f * scale, 20, 40);//񷶥�ĵڶ�����

    }

    public void drawSelf(int texId) {
        MatrixState.rotate(xAngle, 1, 0, 0);
        MatrixState.rotate(yAngle, 0, 1, 0);
        MatrixState.rotate(zAngle, 0, 0, 1);

        //���ĵ�һ����
        MatrixState.pushMatrix();
        MatrixState.translate(0f, 3.0f * scale, 0f);
        tower1.drawSelf(texId);//񷶥�ĵ�һ����
        MatrixState.popMatrix();

        //**************���ĵڶ�����****************************************
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
        //**************���ĵڶ�����***************************************
    }

}
