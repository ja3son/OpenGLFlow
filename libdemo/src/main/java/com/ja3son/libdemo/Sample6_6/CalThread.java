package com.ja3son.libdemo.Sample6_6;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class CalThread extends Thread {
    boolean flag = true;
    ParticleControl pc;

    public CalThread(ParticleControl pc) {
        this.pc = pc;
    }

    public void run() {
        while (flag) {
            synchronized (Constant.lockB) {
                pc.stepSimulation(0.016f);
            }

            float[] vdata = pc.getVerties();

            ByteBuffer vbb = ByteBuffer.allocateDirect(vdata.length * 4);
            vbb.order(ByteOrder.nativeOrder());
            FloatBuffer mVertexBuffer = vbb.asFloatBuffer();
            mVertexBuffer.put(vdata);
            mVertexBuffer.position(0);

            synchronized (Constant.lockA) {
                Constant.mVertexBufferForFlag = mVertexBuffer;
            }
        }
    }
}

