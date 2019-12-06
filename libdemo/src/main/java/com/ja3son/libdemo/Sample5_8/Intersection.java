package com.ja3son.libdemo.Sample5_8;

public class Intersection {

    int numHits;
    HitInfo[] hit = new HitInfo[8];

    public Intersection() {
        for (int i = 0; i < 8; i++) {
            hit[i] = new HitInfo();
        }
    }

    public void set(Intersection inter) {
        for (int i = 0; i < 8; i++) {
            this.hit[i].set(inter.hit[i]);
        }

        this.numHits = inter.numHits;
    }
}
