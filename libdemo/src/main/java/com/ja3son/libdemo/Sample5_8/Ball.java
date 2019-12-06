package com.ja3son.libdemo.Sample5_8;

public class Ball extends HitObject {
    public Ball(Camera cam, Color3f color) {
        this.cam = cam;
        this.color = color;
    }

    @Override
    public boolean hit(Ray r, Intersection inter) {
        Ray genRay = new Ray();
        xfrmRay(genRay, getInvertMatrix(), r);
        double A, B, C;
        A = Vector3.dot(genRay.dir, genRay.dir);
        B = Vector3.dot(genRay.start, genRay.dir);
        C = Vector3.dot(genRay.start, genRay.start) - 1.0f;
        double discrim = B * B - A * C;
        if (discrim < 0.0) {
            return false;
        }
        int num = 0;
        double discRoot = (float) Math.sqrt(discrim);
        double t1 = (-B - discRoot) / A;
        if (t1 > 0.00001) {
            inter.hit[0].hitTime = t1;
            inter.hit[0].hitObject = this;
            inter.hit[0].isEntering = true;
            inter.hit[0].surface = 0;
            Point3 P = rayPos(r, t1);
            inter.hit[0].hitPoint.set(P);
            Point3 preP = xfrmPtoPreP(P);
            inter.hit[0].hitNormal.set(preP);
            num = 1;
        }
        double t2 = (-B + discRoot) / A;
        if (t2 > 0.00001) {
            inter.hit[num].hitTime = t2;
            inter.hit[num].hitObject = this;
            inter.hit[num].isEntering = true;
            inter.hit[num].surface = 0;
            Point3 P = rayPos(r, t2);
            inter.hit[num].hitPoint.set(P);
            Point3 preP = xfrmPtoPreP(P);
            inter.hit[num].hitNormal.set(preP);
            num++;
        }
        inter.numHits = num;
        return (num > 0);
    }

    @Override
    public boolean hit(Ray r) {
        Ray genRay = new Ray();
        xfrmRay(genRay, getInvertMatrix(), r);
        double A, B, C;
        A = Vector3.dot(genRay.dir, genRay.dir);
        B = Vector3.dot(genRay.start, genRay.dir);
        C = Vector3.dot(genRay.start, genRay.start) - 1.0f;
        double discrim = B * B - A * C;
        if (discrim < 0.0) {
            return false;
        }
        double discRoot = (float) Math.sqrt(discrim);
        double t1 = (-B - discRoot) / A;
        if (t1 < 0 || t1 > 1) {
            return false;
        }
        return true;
    }
}
