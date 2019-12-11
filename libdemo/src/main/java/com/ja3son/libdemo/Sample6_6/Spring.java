package com.ja3son.libdemo.Sample6_6;

import static com.ja3son.libdemo.Sample6_6.Constant.SPRING_DAMPING_CONSTANT;
import static com.ja3son.libdemo.Sample6_6.Constant.SPRING_TENSION_CONSTANT;

public class Spring {
    ParticleRet p1;
    ParticleRet p2;
    float k;
    float d;
    float L;

    public Spring() {
        this.p1 = new ParticleRet();
        this.p2 = new ParticleRet();
        this.k = SPRING_TENSION_CONSTANT;
        this.d = SPRING_DAMPING_CONSTANT;
    }
}
