package com.ja3son.libdemo.Sample11_1;

import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static com.ja3son.libdemo.Sample11_1.MySurfaceView.UNIT_SIZE;


public class Desert {
    int mProgram;
    int muMVPMatrixHandle;
    int maPositionHandle;
    int maTexCoorHandle;
    String mVertexShader;
    String mFragmentShader;
    static float[] mMMatrix = new float[16];

    FloatBuffer mVertexBuffer;
    FloatBuffer mTexCoorBuffer;
    int vCount = 0;

    public Desert(MySurfaceView mv, float[] texCoor, int width, int height) {

        initVertexData(texCoor, width, height);

        initShader(mv);
    }


    public void initVertexData(float[] texCoor, int width, int height) {
        vCount = 6;
        float vertices[] = new float[]
                {
                        -UNIT_SIZE * width, 0, -UNIT_SIZE * height,
                        UNIT_SIZE * width, 0, -UNIT_SIZE * height,
                        UNIT_SIZE * width, 0, UNIT_SIZE * height,

                        -UNIT_SIZE * width, 0, -UNIT_SIZE * height,
                        UNIT_SIZE * width, 0, UNIT_SIZE * height,
                        -UNIT_SIZE * width, 0, UNIT_SIZE * height
                };

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);


        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        mTexCoorBuffer = cbb.asFloatBuffer();
        mTexCoorBuffer.put(texCoor);
        mTexCoorBuffer.position(0);
    }


    public void initShader(MySurfaceView mv) {

        mVertexShader = ShaderUtil.loadFromAssetsFile("vertex.sh", mv.getResources());

        mFragmentShader = ShaderUtil.loadFromAssetsFile("frag.sh", mv.getResources());

        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);

        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");

        maTexCoorHandle = GLES30.glGetAttribLocation(mProgram, "aTexCoor");

        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    public void drawSelf(int texId) {

        GLES30.glUseProgram(mProgram);

        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);

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
                        maTexCoorHandle,
                        2,
                        GLES30.GL_FLOAT,
                        false,
                        2 * 4,
                        mTexCoorBuffer
                );

        GLES30.glEnableVertexAttribArray(maPositionHandle);

        GLES30.glEnableVertexAttribArray(maTexCoorHandle);

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount);
    }
}