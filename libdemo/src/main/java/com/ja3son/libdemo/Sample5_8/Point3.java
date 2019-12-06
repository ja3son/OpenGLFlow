package com.ja3son.libdemo.Sample5_8;

public class Point3 {
    float x;
    float y;
    float z;

    public Point3() {
    }

    public Point3(float x, float y, float z) {
        super();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3(float[] data) {
        this.x = data[0];
        this.y = data[1];
        this.z = data[2];
    }

    public void set(Point3 p) {
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
    }

    public void set(Vector3 vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }

    public void set(float p[]) {
        this.x = p[0];
        this.y = p[1];
        this.z = p[2];
    }

    public Point3 addVec(Vector3 vec) {
        return new Point3(this.x + vec.x, this.y + vec.y, this.z + vec.z);
    }

    public Vector3 minus(Point3 p) {
        return new Vector3(this.x - p.x, this.y - p.y, this.z - p.z);
    }

    public Vector3 minus(Vector3 vec) {
        return new Vector3(this.x - vec.x, this.y - vec.y, this.z - vec.z);
    }

    @Override
    public String toString() {
        return "Point:(" + this.x + "," + this.y + "," + this.z + ")";
    }

    public float[] toQici4() {
        return new float[]{this.x, this.y, this.z, 1};
    }
}
