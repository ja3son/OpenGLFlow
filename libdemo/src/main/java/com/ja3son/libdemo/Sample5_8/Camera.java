package com.ja3son.libdemo.Sample5_8;

import android.opengl.Matrix;

import static com.ja3son.libdemo.Sample5_8.Constant.H_3D;
import static com.ja3son.libdemo.Sample5_8.Constant.N_3D;
import static com.ja3son.libdemo.Sample5_8.Constant.W_3D;
import static com.ja3son.libdemo.Sample5_8.Constant.blockSize;
import static com.ja3son.libdemo.Sample5_8.Constant.nCols;
import static com.ja3son.libdemo.Sample5_8.Constant.nRows;

public class Camera {
    Point3 eye, look, up;
    Vector3 u, v, n;
    Light light;

    public Camera(Light light) {
        this.light = light;
        eye = new Point3();
        look = new Point3();
        up = new Point3();
        u = new Vector3();
        v = new Vector3();
        n = new Vector3();
    }

    private float[] vMatrix = new float[16];


    public void setMyCamera
            (
                    float cx, float cy, float cz,
                    float tx, float ty, float tz,
                    float upx, float upy, float upz
            ) {
        Matrix.setLookAtM
                (
                        vMatrix,
                        0,
                        cx, cy, cz,
                        tx, ty, tz,
                        upx, upy, upz
                );
        eye.x = cx;
        eye.y = cy;
        eye.z = cz;
        look.x = tx;
        look.y = ty;
        look.z = tz;
        up.x = upx;
        up.y = upy;
        up.z = upz;

        u.x = vMatrix[0];
        u.y = vMatrix[4];
        u.z = vMatrix[8];
        v.x = vMatrix[1];
        v.y = vMatrix[5];
        v.z = vMatrix[9];
        n.x = vMatrix[2];
        n.y = vMatrix[6];
        n.z = vMatrix[10];
    }


    public void raytrace(Scene scn, ColorRect rect) {

        System.out.println("start...");
        long start = System.currentTimeMillis();

        Ray theRay = new Ray();
        theRay.setStart(eye);


        for (int col = 0; col < nCols; col += blockSize) {
            for (int row = 0; row < nRows; row += blockSize) {

                Vector3 dir1 = n.multiConst(-N_3D);
                Vector3 dir2 = u.multiConst(W_3D * (2 * col / nCols - 1));
                Vector3 dir3 = v.multiConst(H_3D * (2 * row / nRows - 1));
                Vector3 dir = dir1.add(dir2).add(dir3);


                theRay.setDir(dir);
                Color3f clr = new Color3f();
                Point3 vertexPos = new Point3();
                Vector3 normal = new Vector3();

                int isShadowFlag = scn.shade(theRay, clr, vertexPos, normal);

                if (isShadowFlag == -1) {
                    continue;
                }
                rect.setColor(clr.red, clr.green, clr.blue);
                rect.setPos3D(vertexPos.x, vertexPos.y, vertexPos.z);
                rect.setNormal3D(normal.x, normal.y, normal.z);
                rect.setLightPos3D(light.pos.x, light.pos.y, light.pos.z);
                rect.setCameraPos3D(eye.x, eye.y, eye.z);
                rect.setShadow(isShadowFlag);

                rect.setColRow(col, row);
                rect.drawSelf();
            }
        }


        long end = System.currentTimeMillis();
        System.out.println("time=" + (end - start) / 1000.0 + "s");
        System.out.println("finish...");
    }
}
