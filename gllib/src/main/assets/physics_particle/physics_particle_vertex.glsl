#version 300 es
uniform mat4 uMVPMatrix;
uniform float uPointSize;
uniform float uTime;
in vec3 aPosition;
void main() {
    float currTime=mod(uTime, 10.0);
    float px=aPosition.x * currTime;
    float py=aPosition.y * currTime-0.5*1.5*currTime*currTime+3.0;
    float pz=aPosition.z * currTime;
    gl_Position = uMVPMatrix * vec4(px, py, pz, 1);
    gl_PointSize=uPointSize;
}