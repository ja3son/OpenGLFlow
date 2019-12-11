package com.ja3son.libdemo.Sample6_6;


import com.ja3son.libdemo.util.Vector3f;

public class Particle {
    float pfMass;
    float pfInvMass;
    Vector3f pvPosition;
    Vector3f pvVelocity;
    Vector3f pvAcceleration;
    Vector3f pvForces;
    boolean bLocked;

    public Particle() {
        this.pvPosition = new Vector3f(0, 0, 0);
        this.pvVelocity = new Vector3f(0, 0, 0);
        this.pvAcceleration = new Vector3f(0, 0, 0);
        this.pvForces = new Vector3f(0, 0, 0);
    }
}
