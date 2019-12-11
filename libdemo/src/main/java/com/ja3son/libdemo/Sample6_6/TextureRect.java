package com.ja3son.libdemo.Sample6_6;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.ja3son.libdemo.util.MatrixState;
import com.ja3son.libdemo.util.ShaderManager;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static com.ja3son.libdemo.Sample6_6.Constant.NUMCOLS;
import static com.ja3son.libdemo.Sample6_6.Constant.NUMROWS;

public class TextureRect {
    FloatBuffer mTextureBuffer;

    int mProgram;
    int muMVPMatrixHandle;
    int maPositionHandle;
    int maTexCoorHandle;
    int maColorHandle;
    String mVertexShader;
    String mFragmentShader;

    int texId;

    public TextureRect(GLSurfaceView gsv) {

        initVertexData();

        initShader(gsv);
    }


    private void initVertexData() {
        final int cols = NUMCOLS;
        final int rows = NUMROWS;


        float textures[] = generateTexCoor(cols, rows);


        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length * 4);
        tbb.order(ByteOrder.nativeOrder());
        mTextureBuffer = tbb.asFloatBuffer();
        mTextureBuffer.put(textures);
        mTextureBuffer.position(0);


    }


    public void initShader(GLSurfaceView gsv) {

        mVertexShader = ShaderManager.loadFromAssetsFile("vertex.sh", gsv.getResources());

        mFragmentShader = ShaderManager.loadFromAssetsFile("frag.sh", gsv.getResources());

        mProgram = ShaderManager.createProgram(mVertexShader, mFragmentShader);

        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");

        maTexCoorHandle = GLES30.glGetAttribLocation(mProgram, "aTexCoor");

        maColorHandle = GLES30.glGetAttribLocation(mProgram, "aColor");

        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    public void drawSelf(FloatBuffer fb, int texId) {

        if (fb == null) return;


        GLES30.glUseProgram(mProgram);

        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);

        GLES30.glVertexAttribPointer(maPositionHandle, 3, GLES30.GL_FLOAT, false, 3 * 4, fb);

        GLES30.glVertexAttribPointer(maTexCoorHandle, 2, GLES30.GL_FLOAT, false, 2 * 4, mTextureBuffer);

        GLES30.glEnableVertexAttribArray(maPositionHandle);
        GLES30.glEnableVertexAttribArray(maTexCoorHandle);

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, fb.capacity() / 3);
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
