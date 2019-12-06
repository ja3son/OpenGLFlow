package com.ja3son.libdemo.Sample5_8;

import android.opengl.Matrix;

public abstract class HitObject {
    Color3f color;
    private float[] myMatrix;

    Camera cam;

    public abstract boolean hit(Ray ray, Intersection inter);

    public abstract boolean hit(Ray ray);

    public Color3f getColor() {
        return color;
    }

    public Point3 rayPos(Ray r, double t) {
        return cam.eye.addVec(r.dir.multiConst((float) t));
    }

    public void xfrmRay(Ray genRay, float[] invTransf, Ray r) {
        float[] genStart = new float[4];
        Matrix.multiplyMV(genStart, 0, invTransf, 0, r.start.toQici4(), 0);
        genRay.start.set(genStart);

        float[] genDir = new float[4];
        Matrix.multiplyMV(genDir, 0, invTransf, 0, r.dir.toQici4(), 0);
        genRay.dir.set(genDir);
    }

    public void xfrmNormal(Vector3 genNormal, float[] invTranspM, Vector3 normal) {
        float[] tmpNormal = new float[4];
        Matrix.multiplyMV(tmpNormal, 0, invTranspM, 0, normal.toQici4(), 0);
        genNormal.set(tmpNormal);
    }

    public Point3 xfrmPtoPreP(Point3 P) {
        float[] inverM = getInvertMatrix();
        float[] preP = new float[4];
        Matrix.multiplyMV(preP, 0, inverM, 0, P.toQici4(), 0);
        return new Point3(preP);
    }

    public void initMyMatrix() {
        myMatrix = new float[16];
        Matrix.setIdentityM(myMatrix, 0);
    }

    public float[] getMatrix() {
        return myMatrix;
    }

    public float[] getInvertMatrix() {
        float[] invM = new float[16];
        Matrix.invertM(invM, 0, myMatrix, 0);
        return invM;
    }

    public float[] getInvertTransposeMatrix() {
        float[] invTranspM = new float[16];
        Matrix.transposeM(invTranspM, 0, myMatrix, 0);
        Matrix.invertM(invTranspM, 0, invTranspM, 0);
        return invTranspM;
    }

    public void translate(float x, float y, float z) {
        Matrix.translateM(myMatrix, 0, x, y, z);
    }

    public void rotate(float angle, float x, float y, float z) {
        Matrix.rotateM(myMatrix, 0, angle, x, y, z);
    }

    public void scale(float x, float y, float z) {
        Matrix.scaleM(myMatrix, 0, x, y, z);
    }
}