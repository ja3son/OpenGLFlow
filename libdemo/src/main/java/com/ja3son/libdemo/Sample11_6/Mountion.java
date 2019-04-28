package com.ja3son.libdemo.Sample11_6;

import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Mountion {

    float UNIT_SIZE = 3.0f;


    int mProgram;

    int muMVPMatrixHandle;

    int maPositionHandle;

    int maTexCoorHandle;


    int sTextureGrassHandle;

    int sTextureRockHandle;

    int landStartYYHandle;

    int landYSpanHandle;


    FloatBuffer mVertexBuffer;
    FloatBuffer mTexCoorBuffer;

    int vCount = 0;

    public Mountion(MySurfaceView mv, float[][] yArray, int rows, int cols) {
        initVertexData(yArray, rows, cols);
        initShader(mv);
    }

    public void initVertexData(float[][] yArray, int rows, int cols) {

        vCount = cols * rows * 2 * 3;
        float vertices[] = new float[vCount * 3];
        int count = 0;
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {

                float zsx = -UNIT_SIZE * cols / 2 + i * UNIT_SIZE;
                float zsz = -UNIT_SIZE * rows / 2 + j * UNIT_SIZE;

                vertices[count++] = zsx;
                vertices[count++] = yArray[j][i];
                vertices[count++] = zsz;

                vertices[count++] = zsx;
                vertices[count++] = yArray[j + 1][i];
                vertices[count++] = zsz + UNIT_SIZE;

                vertices[count++] = zsx + UNIT_SIZE;
                vertices[count++] = yArray[j][i + 1];
                vertices[count++] = zsz;

                vertices[count++] = zsx + UNIT_SIZE;
                vertices[count++] = yArray[j][i + 1];
                vertices[count++] = zsz;

                vertices[count++] = zsx;
                vertices[count++] = yArray[j + 1][i];
                vertices[count++] = zsz + UNIT_SIZE;

                vertices[count++] = zsx + UNIT_SIZE;
                vertices[count++] = yArray[j + 1][i + 1];
                vertices[count++] = zsz + UNIT_SIZE;
            }
        }


        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);


        float[] texCoor = generateTexCoor(cols, rows);

        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        mTexCoorBuffer = cbb.asFloatBuffer();
        mTexCoorBuffer.put(texCoor);
        mTexCoorBuffer.position(0);
    }


    public void initShader(MySurfaceView mv) {
        String mVertexShader = ShaderUtil.loadFromAssetsFile("vertex_mountion.sh", mv.getResources());
        String mFragmentShader = ShaderUtil.loadFromAssetsFile("frag_mountion.sh", mv.getResources());

        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);

        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");

        maTexCoorHandle = GLES30.glGetAttribLocation(mProgram, "aTexCoor");

        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");


        sTextureGrassHandle = GLES30.glGetUniformLocation(mProgram, "sTextureGrass");

        sTextureRockHandle = GLES30.glGetUniformLocation(mProgram, "sTextureRock");

        landStartYYHandle = GLES30.glGetUniformLocation(mProgram, "landStartY");

        landYSpanHandle = GLES30.glGetUniformLocation(mProgram, "landYSpan");
    }

    public void drawSelf(int texId, int rock_textId) {

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
        GLES30.glActiveTexture(GLES30.GL_TEXTURE1);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, rock_textId);
        GLES30.glUniform1i(sTextureGrassHandle, 0);
        GLES30.glUniform1i(sTextureRockHandle, 1);


        GLES30.glUniform1f(landStartYYHandle, 0);
        GLES30.glUniform1f(landYSpanHandle, 50);


        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount);
    }

    public float[] generateTexCoor(int bw, int bh) {
        float[] result = new float[bw * bh * 6 * 2];
        float sizew = 16.0f / bw;
        float sizeh = 16.0f / bh;
        int c = 0;
        for (int i = 0; i < bh; i++) {
            for (int j = 0; j < bw; j++) {

                float s = j * sizew;
                float t = i * sizeh;

                result[c++] = s;
                result[c++] = t;

                result[c++] = s;
                result[c++] = t + sizeh;

                result[c++] = s + sizew;
                result[c++] = t;

                result[c++] = s + sizew;
                result[c++] = t;

                result[c++] = s;
                result[c++] = t + sizeh;

                result[c++] = s + sizew;
                result[c++] = t + sizeh;
            }
        }
        return result;
    }
}