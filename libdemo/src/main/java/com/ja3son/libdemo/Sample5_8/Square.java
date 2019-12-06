package com.ja3son.libdemo.Sample5_8;


public class Square extends HitObject {
    public Square(Camera cam, Color3f color) {
        this.cam = cam;
        this.color = color;
    }

    @Override
    public boolean hit(Ray r, Intersection inter) {

        Ray genRay = new Ray();
        xfrmRay(genRay, getInvertMatrix(), r);
        double denom = genRay.dir.z;

        if (Math.abs(denom) < 0.0001) {
            return false;
        }
        double time = -genRay.start.z / denom;
        if (time <= 0.0) {
            return false;
        }


        double hx = genRay.start.x + genRay.dir.x * time;
        double hy = genRay.start.y + genRay.dir.y * time;
        if (hx > 1.0 || hx < -1.0) {
            return false;
        }
        if (hy > 1.0 || hy < -1.0) {
            return false;
        }

        inter.numHits = 1;


        inter.hit[0].hitTime = time;
        inter.hit[0].hitObject = this;
        inter.hit[0].isEntering = true;
        inter.hit[0].surface = 0;
        Point3 P = rayPos(r, time);
        inter.hit[0].hitPoint.set(P);
        inter.hit[0].hitNormal.set(0, 0, 1);
        return true;
    }

    @Override
    public boolean hit(Ray r) {
        Ray genRay = new Ray();
        xfrmRay(genRay, getInvertMatrix(), r);
        double denom = genRay.dir.z;

        if (Math.abs(denom) < 0.0001) {
            return false;
        }
        double time = -genRay.start.z / denom;

        if (time < 0.0 || time > 1) {
            return false;
        }

        double hx = genRay.start.x + genRay.dir.x * time;
        double hy = genRay.start.y + genRay.dir.y * time;
        if (hx > 1.0 || hx < -1.0) {
            return false;
        }
        if (hy > 1.0 || hy < -1.0) {
            return false;
        }
        return true;
    }
}
