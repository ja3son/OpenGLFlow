package com.ja3son.libdemo.Sample5_8;

import android.annotation.SuppressLint;
import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static com.ja3son.libdemo.Sample5_8.Constant.H;
import static com.ja3son.libdemo.Sample5_8.Constant.W;
import static com.ja3son.libdemo.Sample5_8.Constant.nCols;
import static com.ja3son.libdemo.Sample5_8.Constant.nRows;


@SuppressLint("NewApi")
public class ColorRect {
    int mProgram;
    int muMVPMatrixHandle;
    int maPositionHandle;

    int muColorHandle;
    int mu3DPosHandle;
    int muNormalHandle;
    int muLightLocationHandle;
    int muCameraHandle;
    int muIsShadow;

    String mVertexShader;
    String mFragmentShader;

    FloatBuffer mVertexBuffer;
    int vCount = 0;
    float[] color3 = new float[3];
    float[] vertexPos3D = new float[3];
    float[] normal3D = new float[3];
    float[] lightPos3D = new float[3];
    float[] cameraPos3D = new float[3];
    int isShadow;

    float u;
    float v;

    public ColorRect(MySurfaceView mv) {
        initVertexData();
        intShader(mv);
    }

    public void initVertexData() {
        vCount = 6;
        float vertices[] = new float[]{
                0, 0, 0,
                Constant.blockSize, 0, 0,
                Constant.blockSize, Constant.blockSize, 0,

                0, 0, 0,
                Constant.blockSize, Constant.blockSize, 0,
                0, Constant.blockSize, 0
        };

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
    }

    public void intShader(MySurfaceView mv) {
        mVertexShader = ShaderUtil.loadFromAssetsFile("vertex.sh", mv.getResources());
        mFragmentShader = ShaderUtil.loadFromAssetsFile("frag.sh", mv.getResources());
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");

        muColorHandle = GLES30.glGetUniformLocation(mProgram, "uColor");
        mu3DPosHandle = GLES30.glGetUniformLocation(mProgram, "uPosition");
        muNormalHandle = GLES30.glGetUniformLocation(mProgram, "uNormal");
        muLightLocationHandle = GLES30.glGetUniformLocation(mProgram, "uLightLocation");
        muCameraHandle = GLES30.glGetUniformLocation(mProgram, "uCamera");
        muIsShadow = GLES30.glGetUniformLocation(mProgram, "isShadow");
    }

    public void drawSelf() {
        MatrixState.pushMatrix();
        MatrixState.translate(u, v, 0);
        GLES30.glUseProgram(mProgram);
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,
                MatrixState.getFinalMatrix(), 0);

        GLES30.glUniform3fv(muColorHandle, 1, color3, 0);
        GLES30.glUniform3fv(mu3DPosHandle, 1, vertexPos3D, 0);
        GLES30.glUniform3fv(muNormalHandle, 1, normal3D, 0);
        GLES30.glUniform3fv(muLightLocationHandle, 1, lightPos3D, 0);
        GLES30.glUniform3fv(muCameraHandle, 1, cameraPos3D, 0);
        GLES30.glUniform1i(muIsShadow, isShadow);

        GLES30.glVertexAttribPointer(maPositionHandle, 3, GLES30.GL_FLOAT,
                false, 3 * 4, mVertexBuffer);
        GLES30.glEnableVertexAttribArray(maPositionHandle);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount);
        MatrixState.popMatrix();
    }

    public void setColor(float r, float g, float b) {
        this.color3[0] = r;
        this.color3[1] = g;
        this.color3[2] = b;
    }

    public void setPos3D(float x, float y, float z) {
        this.vertexPos3D[0] = x;
        this.vertexPos3D[1] = y;
        this.vertexPos3D[2] = z;
    }

    public void setNormal3D(float x, float y, float z) {
        this.normal3D[0] = x;
        this.normal3D[1] = y;
        this.normal3D[2] = z;
    }

    public void setLightPos3D(float x, float y, float z) {
        this.lightPos3D[0] = x;
        this.lightPos3D[1] = y;
        this.lightPos3D[2] = z;
    }

    public void setCameraPos3D(float x, float y, float z) {
        this.cameraPos3D[0] = x;
        this.cameraPos3D[1] = y;
        this.cameraPos3D[2] = z;
    }

    public void setShadow(int isShadow) {
        this.isShadow = isShadow;
    }

    public void setPos(float u, float v) {
        this.u = u;
        this.v = v;
    }

    public void setColRow(int col, int row) {
        float u = -W + W * (2 * col / nCols);
        float v = -H + H * (2 * row / nRows);
        this.setPos(u, v);
    }
}
