package com.ja3son.libdemo.Sample5_8;

import java.util.ArrayList;
import java.util.List;

import static com.ja3son.libdemo.Sample5_8.Constant.*;

public class Scene {
    Camera cam;
    Light light;
    Ray feeler = new Ray();
    List<HitObject> hitObjects;

    Ball ball1;
    Ball ball2;
    Square sqare;

    public Scene(Camera cam, Light light) {
        this.cam = cam;
        this.light = light;
        hitObjects = new ArrayList<>();
        ball1 = new Ball(cam, new Color3f(BALL1_COLOR));
        ball2 = new Ball(cam, new Color3f(BALL2_COLOR));
        sqare = new Square(cam, new Color3f(PLANE_COLOR));
        hitObjects.add(ball1);
        hitObjects.add(ball2);
        hitObjects.add(sqare);
    }

    public void transform() {
        for (HitObject pObj : hitObjects) {
            pObj.initMyMatrix();
        }

        sqare.rotate(-90, 1, 0, 0);
        sqare.scale(PLANE_WIDTH / 2.0f, PLANE_HEIGHT / 2.0f, 1);

        ball1.translate(-CENTER_DIS, R, 0);
        ball1.scale(R, R, R);

        ball2.translate(CENTER_DIS, R, 0);
        ball2.scale(R, R, R);
    }

    public int shade(
            Ray ray,
            Color3f color,
            Point3 vetex,
            Vector3 normal
    ) {
        Intersection best = new Intersection();
        getFirstHit(ray, best);
        if (best.numHits == 0) {
            return -1;
        }

        color.set(best.hit[0].hitObject.getColor());
        vetex.set(best.hit[0].hitPoint);

        float[] inverTranspM = best.hit[0].hitObject.getInvertTransposeMatrix();
        Vector3 preN = best.hit[0].hitNormal;
        best.hit[0].hitObject.xfrmNormal(normal, inverTranspM, preN);

        Point3 hitPoint = best.hit[0].hitPoint;

        feeler.start.set(hitPoint.minus(ray.dir.multiConst(MNIMUM)));

        feeler.dir = light.pos.minus(hitPoint);
        if (isInShadow(feeler)) {
            return 1;
        }
        return 0;
    }

    public void getFirstHit(Ray ray, Intersection best) {
        Intersection inter = new Intersection();
        best.numHits = 0;

        for (HitObject pObj : hitObjects) {
            if (!pObj.hit(ray, inter)) {
                continue;
            }
            if (best.numHits == 0 ||
                    inter.hit[0].hitTime < best.hit[0].hitTime) {
                best.set(inter);
            }
        }
    }

    public boolean isInShadow(Ray feeler) {
        for (HitObject pObj : hitObjects) {
            if (pObj.hit(feeler)) {
                return true;
            }
        }
        return false;
    }
}