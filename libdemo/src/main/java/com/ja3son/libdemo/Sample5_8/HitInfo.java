package com.ja3son.libdemo.Sample5_8;

public class HitInfo {

    double hitTime;
    HitObject hitObject;
    boolean isEntering;
    int surface;
    Point3 hitPoint;
    Vector3 hitNormal;

    public HitInfo() {
        hitPoint = new Point3();
        hitNormal = new Vector3();
    }

    public void set(HitInfo hit) {
        this.hitTime = hit.hitTime;
        this.hitObject = hit.hitObject;
        this.isEntering = hit.isEntering;
        this.surface = hit.surface;
        this.hitPoint.set(hit.hitPoint);
        this.hitNormal.set(hit.hitNormal);
    }

    @Override
    public String toString() {
        return "hitTime" + hitTime + ",hitPoint" + hitPoint;
    }
}
