package com.ja3son.libdemo.Sample11_6;

import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

public class Sky {
    final float UNIT_SIZE = 100.0f;

    int mProgram;

    int muMVPMatrixHandle;

    int maPositionHandle;

    int maTexCoorHandle;

    FloatBuffer mVertexBuffer;
    FloatBuffer mTexCoorBuffer;

    int vCount = 0;

    public Sky(MySurfaceView mv) {
        initVertexData(UNIT_SIZE);
        initShader(mv);
    }

    public void initVertexData(float radius) {
        float ANGLE_SPAN = 18f;
        float angleV = 90;
        ArrayList<Float> alVertix = new ArrayList<Float>();

        for (float vAngle = angleV; vAngle > 0; vAngle = vAngle - ANGLE_SPAN) {
            for (float hAngle = 360; hAngle > 0; hAngle = hAngle - ANGLE_SPAN) {


                double xozLength = radius * Math.cos(Math.toRadians(vAngle));
                float x1 = (float) (xozLength * Math.cos(Math.toRadians(hAngle)));
                float z1 = (float) (xozLength * Math.sin(Math.toRadians(hAngle)));
                float y1 = (float) (radius * Math.sin(Math.toRadians(vAngle)));

                xozLength = radius * Math.cos(Math.toRadians(vAngle - ANGLE_SPAN));
                float x2 = (float) (xozLength * Math.cos(Math.toRadians(hAngle)));
                float z2 = (float) (xozLength * Math.sin(Math.toRadians(hAngle)));
                float y2 = (float) (radius * Math.sin(Math.toRadians(vAngle - ANGLE_SPAN)));

                xozLength = radius * Math.cos(Math.toRadians(vAngle - ANGLE_SPAN));
                float x3 = (float) (xozLength * Math.cos(Math.toRadians(hAngle - ANGLE_SPAN)));
                float z3 = (float) (xozLength * Math.sin(Math.toRadians(hAngle - ANGLE_SPAN)));
                float y3 = (float) (radius * Math.sin(Math.toRadians(vAngle - ANGLE_SPAN)));

                xozLength = radius * Math.cos(Math.toRadians(vAngle));
                float x4 = (float) (xozLength * Math.cos(Math.toRadians(hAngle - ANGLE_SPAN)));
                float z4 = (float) (xozLength * Math.sin(Math.toRadians(hAngle - ANGLE_SPAN)));
                float y4 = (float) (radius * Math.sin(Math.toRadians(vAngle)));


                alVertix.add(x1);
                alVertix.add(y1);
                alVertix.add(z1);
                alVertix.add(x4);
                alVertix.add(y4);
                alVertix.add(z4);
                alVertix.add(x2);
                alVertix.add(y2);
                alVertix.add(z2);


                alVertix.add(x2);
                alVertix.add(y2);
                alVertix.add(z2);
                alVertix.add(x4);
                alVertix.add(y4);
                alVertix.add(z4);
                alVertix.add(x3);
                alVertix.add(y3);
                alVertix.add(z3);
            }
        }
        vCount = alVertix.size() / 3;

        float vertices[] = new float[vCount * 3];
        for (int i = 0; i < alVertix.size(); i++) {
            vertices[i] = alVertix.get(i);
        }

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);


        float[] textureCoors = generateTexCoor
                (
                        (int) (360 / ANGLE_SPAN),
                        (int) (angleV / ANGLE_SPAN)
                );
        ByteBuffer tbb = ByteBuffer.allocateDirect(textureCoors.length * 4);
        tbb.order(ByteOrder.nativeOrder());
        mTexCoorBuffer = tbb.asFloatBuffer();
        mTexCoorBuffer.put(textureCoors);
        mTexCoorBuffer.position(0);
    }

    public void initShader(MySurfaceView mv) {
        String mVertexHandle = ShaderUtil.loadFromAssetsFile("vertex.sh", mv.getResources());
        String mTexCoorHandle = ShaderUtil.loadFromAssetsFile("frag.sh", mv.getResources());
        mProgram = ShaderUtil.createProgram(mVertexHandle, mTexCoorHandle);

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

    public float[] generateTexCoor(int bw, int bh) {
        float[] result = new float[bw * bh * 6 * 2];
        float sizew = 1.0f / bw;
        float sizeh = 1.0f / bh;
        int c = 0;
        for (int i = 0; i < bh; i++) {
            for (int j = 0; j < bw; j++) {

                float s = j * sizew;
                float t = i * sizeh;

                result[c++] = s;
                result[c++] = t;

                result[c++] = s + sizew;
                result[c++] = t;

                result[c++] = s;
                result[c++] = t + sizeh;

                result[c++] = s;
                result[c++] = t + sizeh;

                result[c++] = s + sizew;
                result[c++] = t;

                result[c++] = s + sizew;
                result[c++] = t + sizeh;
            }
        }
        return result;
    }
}