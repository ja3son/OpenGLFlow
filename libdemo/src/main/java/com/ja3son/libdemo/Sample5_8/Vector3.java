package com.ja3son.libdemo.Sample5_8;

public class Vector3 {

    float x;
    float y;
    float z;

    public Vector3() {
    }

    ;

    public Vector3(float x, float y, float z) {
        super();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(Vector3 vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }

    public void set(Point3 p) {
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
    }

    public void set(float vec[]) {
        this.x = vec[0];
        this.y = vec[1];
        this.z = vec[2];
    }

    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 multiConst(float constant) {
        return new Vector3(this.x * constant, this.y * constant, this.z * constant);
    }


    public static float dot(Vector3 v1, Vector3 v2) {

        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }


    public static float dot(Point3 p1, Vector3 v2) {
        return p1.x * v2.x + p1.y * v2.y + p1.z * v2.z;
    }


    public static float dot(Point3 p1, Point3 p2) {
        return p1.x * p2.x + p1.y * p2.y + p1.z * p2.z;
    }


    public Vector3 add(Vector3 v) {
        return new Vector3(this.x + v.x, this.y + v.y, this.z + v.z);
    }

    public String toString() {
        return "vector:[" + this.x + "," + this.y + "," + this.z + "]";
    }


    public float[] toQici4() {
        return new float[]{this.x, this.y, this.z, 0};
    }

}
