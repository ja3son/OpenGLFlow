package com.ja3son.libdemo.Sample6_6;


import com.ja3son.libdemo.util.Vector3f;

import static com.ja3son.libdemo.Sample6_6.Constant.COLLISIONTOLERANCE;
import static com.ja3son.libdemo.Sample6_6.Constant.CSTER;
import static com.ja3son.libdemo.Sample6_6.Constant.DRAGCOEFFICIENT;
import static com.ja3son.libdemo.Sample6_6.Constant.FLAGPOLERADIUS;
import static com.ja3son.libdemo.Sample6_6.Constant.FRICTIONFACTOR;
import static com.ja3son.libdemo.Sample6_6.Constant.GRAVITY;
import static com.ja3son.libdemo.Sample6_6.Constant.KRESTITUTION;
import static com.ja3son.libdemo.Sample6_6.Constant.NUMCOLS;
import static com.ja3son.libdemo.Sample6_6.Constant.NUMROWS;
import static com.ja3son.libdemo.Sample6_6.Constant.NUMSPTINGS;
import static com.ja3son.libdemo.Sample6_6.Constant.NUMVERTICES;
import static com.ja3son.libdemo.Sample6_6.Constant.RSTER;
import static com.ja3son.libdemo.Sample6_6.Constant.SPRING_SHEAR_CONSTANT;
import static com.ja3son.libdemo.Sample6_6.Constant.WindForce;
import static com.ja3son.libdemo.Sample6_6.Constant.isC;

public class ParticleControl {
    Particle particles[][] = new Particle[NUMROWS + 1][NUMCOLS + 1];
    Spring springs[] = new Spring[NUMSPTINGS];
    float vertices[] = new float[NUMCOLS * NUMROWS * 2 * 3 * 3];
    Collision collisions[] = new Collision[NUMVERTICES * 2];
    Vector3f temp = new Vector3f(0, 0, 0);
    Vector3f temp2 = new Vector3f(0, 0, 0);

    public ParticleControl() {
        initalize();
    }

    public float[] getVerties() {
        int count = 0;
        for (int r = 0; r < NUMROWS; r++) {
            for (int c = 0; c < NUMCOLS; c++) {
                vertices[count++] = particles[r][c].pvPosition.x;
                vertices[count++] = particles[r][c].pvPosition.y;
                vertices[count++] = particles[r][c].pvPosition.z;

                vertices[count++] = particles[r + 1][c].pvPosition.x;
                vertices[count++] = particles[r + 1][c].pvPosition.y;
                vertices[count++] = particles[r + 1][c].pvPosition.z;

                vertices[count++] = particles[r][c + 1].pvPosition.x;
                vertices[count++] = particles[r][c + 1].pvPosition.y;
                vertices[count++] = particles[r][c + 1].pvPosition.z;

                vertices[count++] = particles[r][c + 1].pvPosition.x;
                vertices[count++] = particles[r][c + 1].pvPosition.y;
                vertices[count++] = particles[r][c + 1].pvPosition.z;

                vertices[count++] = particles[r + 1][c].pvPosition.x;
                vertices[count++] = particles[r + 1][c].pvPosition.y;
                vertices[count++] = particles[r + 1][c].pvPosition.z;

                vertices[count++] = particles[r + 1][c + 1].pvPosition.x;
                vertices[count++] = particles[r + 1][c + 1].pvPosition.y;
                vertices[count++] = particles[r + 1][c + 1].pvPosition.z;
            }
        }
        return vertices;
    }

    public void initalize() {
        for (int r = 0; r <= NUMROWS; r++) {
            for (int c = 0; c <= NUMCOLS; c++) {
                particles[r][c] = new Particle();
                float f;
                if (((r == 0) && (c == 0)) || ((r == NUMROWS) && (c == NUMCOLS))) {
                    f = 1;
                } else if (((r == NUMROWS) && (c == 0)) || ((r == 0) && (c == NUMCOLS))) {
                    f = 2;
                } else if (((r == 0) && ((c != 0) && (c != NUMCOLS))) || ((r == NUMROWS) && ((c != 0) && (c != NUMCOLS)))) {
                    f = 3;
                } else {
                    f = 6;
                }
                f = f / 8;

                particles[r][c].pfMass = f;
                particles[r][c].pfInvMass = 1 / particles[r][c].pfMass;

                particles[r][c].pvPosition.x = FLAGPOLERADIUS + c * CSTER;
                particles[r][c].pvPosition.y = RSTER * NUMROWS / 2 - r * RSTER;
                particles[r][c].pvPosition.z = 0;
                if ((r == 0 && c == 0) || (r == NUMROWS && c == 0)) {
                    particles[r][c].bLocked = true;
                } else {
                    particles[r][c].bLocked = false;
                }
            }
        }

        int count = 0;
        for (int r = 0; r <= NUMROWS; r++) {
            for (int c = 0; c <= NUMCOLS; c++) {
                if (c < NUMCOLS) {
                    springs[count] = new Spring();
                    springs[count].p1.r = r;
                    springs[count].p1.c = c;
                    springs[count].p2.r = r;
                    springs[count].p2.c = c + 1;
                    temp.voluation(particles[r][c].pvPosition);
                    temp.sub(particles[r][c + 1].pvPosition);
                    springs[count].L = temp.module();
                    count++;
                }
                if (r < NUMROWS) {
                    springs[count] = new Spring();
                    springs[count].p1.r = r;
                    springs[count].p1.c = c;
                    springs[count].p2.r = r + 1;
                    springs[count].p2.c = c;
                    temp.voluation(particles[r][c].pvPosition);
                    temp.sub(particles[r + 1][c].pvPosition);
                    springs[count].L = temp.module();
                    count++;
                }
                if (r < NUMROWS && c < NUMCOLS) {
                    springs[count] = new Spring();
                    springs[count].k = SPRING_SHEAR_CONSTANT;
                    springs[count].p1.r = r;
                    springs[count].p1.c = c;
                    springs[count].p2.r = r + 1;
                    springs[count].p2.c = c + 1;
                    temp.voluation(particles[r][c].pvPosition);
                    temp.sub(particles[r + 1][c + 1].pvPosition);
                    springs[count].L = temp.module();
                    count++;
                }
                if (r < NUMROWS && c > 0) {
                    springs[count] = new Spring();
                    springs[count].k = SPRING_SHEAR_CONSTANT;
                    springs[count].p1.r = r;
                    springs[count].p1.c = c;
                    springs[count].p2.r = r + 1;
                    springs[count].p2.c = c - 1;
                    temp.voluation(particles[r][c].pvPosition);
                    temp.sub(particles[r + 1][c - 1].pvPosition);
                    springs[count].L = temp.module();
                    count++;
                }
            }
        }
        for (int i = 0; i < NUMVERTICES * 2; i++) {
            collisions[i] = new Collision();
        }
    }

    public void calcForces() {
        for (int r = 0; r <= NUMROWS; r++) {
            for (int c = 0; c <= NUMCOLS; c++) {
                particles[r][c].pvForces.x = 0;
                particles[r][c].pvForces.y = 0;
                particles[r][c].pvForces.z = 0;
            }
        }
        for (int r = 0; r <= NUMROWS; r++) {
            for (int c = 0; c <= NUMCOLS; c++) {
                if (!particles[r][c].bLocked) {
                    particles[r][c].pvForces.y += GRAVITY * particles[r][c].pfMass;
                    temp.voluation(particles[r][c].pvVelocity);
                    temp.normalize();

                    temp.scale(-particles[r][c].pvVelocity.moduleSq() * DRAGCOEFFICIENT);
                    particles[r][c].pvForces.add(temp);

                    temp.voluation((float) (Math.random() * 1), 0, (float) (Math.random() * 0.3f));
                    temp.scale((float) (Math.random() * WindForce));
                    particles[r][c].pvForces.add(temp);
                }
            }
        }
        for (int i = 0; i < NUMSPTINGS; i++) {
            int r1 = (int) springs[i].p1.r;
            int c1 = (int) springs[i].p1.c;
            int r2 = (int) springs[i].p2.r;
            int c2 = (int) springs[i].p2.c;
            temp.voluation(particles[r1][c1].pvPosition);
            temp.sub(particles[r2][c2].pvPosition);
            float pd = temp.module();
            temp2.voluation(particles[r1][c1].pvVelocity);
            temp2.sub(particles[r2][c2].pvVelocity);
            float L = springs[i].L;

            float t = -(springs[i].k * (pd - L) + springs[i].d * (temp.dotProduct(temp2) / pd)) / pd;
            temp.scale(t);
            if (!particles[r1][c1].bLocked) {
                particles[r1][c1].pvForces.add(temp);
            }
            if (!particles[r2][c2].bLocked) {
                temp.scale(-1);
                particles[r2][c2].pvForces.add(temp);
            }

        }
    }

    public boolean checkForCollisions() {
        int count = 0;
        boolean state = false;

        for (int i = 0; i < collisions.length; i++) {
            collisions[i].r = -1;
        }

        for (int r = 0; r <= NUMROWS; r++) {
            for (int c = 0; c <= NUMCOLS; c++) {
                if (!particles[r][c].bLocked) {
                    if ((particles[r][c].pvPosition.y <= COLLISIONTOLERANCE) && (particles[r][c].pvVelocity.y < 0f)) {
                        state = true;
                        collisions[count].r = r;
                        collisions[count].c = c;
                        collisions[count].n.x = 0.0f;
                        collisions[count].n.y = 1.0f;
                        collisions[count].n.z = 0.0f;
                        count++;
                    }
                }
            }
        }

        for (int r = 0; r <= NUMROWS; r++) {
            for (int c = 0; c <= NUMCOLS; c++) {
                if (!particles[r][c].bLocked) {

                    float fd = (particles[r][c].pvPosition.x) * (particles[r][c].pvPosition.x) +
                            (particles[r][c].pvPosition.z) * (particles[r][c].pvPosition.z);
                    temp.voluation(particles[r][c].pvPosition.x, 0, particles[r][c].pvPosition.z);
                    if ((fd <= FLAGPOLERADIUS) && (temp.dotProduct(particles[r][c].pvVelocity) > 0f)) {
                        state = true;
                        collisions[count].r = r;
                        collisions[count].c = c;
                        collisions[count].n.voluation(temp);
                        collisions[count].n.normalize();
                        count++;
                    }
                }
            }
        }
        return state;
    }

    public void resolveCollisions() {
        for (int i = 0; i < collisions.length; i++) {
            if (collisions[i].r != -1) {
                int r = collisions[i].r;
                int c = collisions[i].c;
                temp.voluation(collisions[i].n);

                temp.scale(temp.dotProduct(particles[r][c].pvVelocity));
                temp2.voluation(particles[r][c].pvVelocity);
                temp2.sub(temp);
                temp.scale(-KRESTITUTION);
                temp2.scale(FRICTIONFACTOR);
                temp.add(temp2);
                particles[r][c].pvVelocity.voluation(temp);
            }
        }
    }

    public void stepSimulation(float dt) {
        calcForces();
        for (int r = 0; r <= NUMROWS; r++) {
            for (int c = 0; c <= NUMCOLS; c++) {
                temp.voluation(particles[r][c].pvForces);
                temp.scale(particles[r][c].pfInvMass);
                particles[r][c].pvAcceleration.voluation(temp);
                temp.scale(dt);
                particles[r][c].pvVelocity.add(temp);
                temp.voluation(particles[r][c].pvVelocity);
                temp.scale(dt);
                particles[r][c].pvPosition.add(temp);
                if (particles[r][c].pvPosition.y < COLLISIONTOLERANCE) {
                    particles[r][c].pvPosition.y = COLLISIONTOLERANCE;
                }
            }
        }
        if (isC) {
            if (checkForCollisions()) {
                resolveCollisions();
            }
        }
    }
}
