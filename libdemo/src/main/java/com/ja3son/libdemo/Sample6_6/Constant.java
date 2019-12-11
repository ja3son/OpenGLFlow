package com.ja3son.libdemo.Sample6_6;

import java.nio.FloatBuffer;

public class Constant {
    static FloatBuffer mVertexBufferForFlag;
    static Object lockA = new Object();
    static Object lockB = new Object();
    final static int NUMROWS = 7;
    final static int NUMCOLS = 10;
    final static int NUMVERTICES = (NUMROWS + 1) * (NUMCOLS + 1);
    final static int NUMSPTINGS = (NUMROWS * (NUMCOLS + 1) + (NUMROWS + 1) * NUMCOLS + 2 * NUMROWS * NUMCOLS);
    final static float RSTER = 0.75f / NUMROWS;
    final static float CSTER = 1.0f / NUMCOLS;
    final static float KRESTITUTION = 0.3f;
    final static float COLLISIONTOLERANCE = -6.6f;
    final static float FRICTIONFACTOR = 0.9f;
    final static float FLAGPOLERADIUS = 0.04f;
    final static float GRAVITY = -0.7f;
    final static float SPRING_TENSION_CONSTANT = 500.f;
    final static float SPRING_SHEAR_CONSTANT = 300.f;
    final static float SPRING_DAMPING_CONSTANT = 2.f;
    static boolean isC = false;
    static float WindForce = 2.0f;
    final static float DRAGCOEFFICIENT = 0.01f;
}
