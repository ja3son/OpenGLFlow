package com.ja3son.libdemo.util;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


public class LoadedObjectVertexNormalTexture {
    int mProgram;
    int muMVPMatrixHandle;
    int muMMatrixHandle;
    int maPositionHandle;
    int maNormalHandle;
    int maLightLocationHandle;
    int maCameraHandle;
    int maTexCoorHandle;
    int muIsShadow;
    int muProjCameraMatrixHandle;

    String mVertexShader;
    String mFragmentShader;

    FloatBuffer mVertexBuffer;
    FloatBuffer mNormalBuffer;
    FloatBuffer mTexCoorBuffer;
    int vCount = 0;

    public LoadedObjectVertexNormalTexture(GLSurfaceView mv, float[] vertices, float[] normals, float texCoors[]) {

        initVertexData(vertices, normals, texCoors);

        initShader(mv);
    }


    public void initVertexData(float[] vertices, float[] normals, float texCoors[]) {

        vCount = vertices.length / 3;


        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);


        ByteBuffer cbb = ByteBuffer.allocateDirect(normals.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        mNormalBuffer = cbb.asFloatBuffer();
        mNormalBuffer.put(normals);
        mNormalBuffer.position(0);


        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoors.length * 4);
        tbb.order(ByteOrder.nativeOrder());
        mTexCoorBuffer = tbb.asFloatBuffer();
        mTexCoorBuffer.put(texCoors);
        mTexCoorBuffer.position(0);


    }


    public void initShader(GLSurfaceView mv) {

        mVertexShader = ShaderManager.loadFromAssetsFile("vertex_nom.sh", mv.getResources());

        mFragmentShader = ShaderManager.loadFromAssetsFile("frag_nom.sh", mv.getResources());

        mProgram = ShaderManager.createProgram(mVertexShader, mFragmentShader);

        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");

        maNormalHandle = GLES30.glGetAttribLocation(mProgram, "aNormal");

        maTexCoorHandle = GLES30.glGetAttribLocation(mProgram, "aTexCoor");


        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");

        muMMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMMatrix");

        maLightLocationHandle = GLES30.glGetUniformLocation(mProgram, "uLightLocation");

        maCameraHandle = GLES30.glGetUniformLocation(mProgram, "uCamera");

        muIsShadow = GLES30.glGetUniformLocation(mProgram, "isShadow");

        muProjCameraMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMProjCameraMatrix");

    }

    public void drawSelf(int texId, int isShadow) {

        GLES30.glUseProgram(mProgram);

        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);

        GLES30.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);

        GLES30.glUniform3fv(maLightLocationHandle, 1, MatrixState.lightPositionFB);

        GLES30.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);

        GLES30.glUniform1i(muIsShadow, isShadow);

        GLES30.glUniformMatrix4fv(muProjCameraMatrixHandle, 1, false, MatrixState.getViewProjMatrix(), 0);


        GLES30.glVertexAttribPointer
                (
                        maPositionHandle,
                        3,
                        GLES30.GL_FLOAT,
                        false,
                        3 * 4,
                        mVertexBuffer
                );

        GLES30.glVertexAttribPointer
                (
                        maNormalHandle,
                        3,
                        GLES30.GL_FLOAT,
                        false,
                        3 * 4,
                        mNormalBuffer
                );

        GLES30.glVertexAttribPointer
                (
                        maTexCoorHandle,
                        2,
                        GLES30.GL_FLOAT,
                        false,
                        2 * 4,
                        mTexCoorBuffer
                );

        GLES30.glEnableVertexAttribArray(maPositionHandle);
        GLES30.glEnableVertexAttribArray(maNormalHandle);
        GLES30.glEnableVertexAttribArray(maTexCoorHandle);

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount);
    }
}
