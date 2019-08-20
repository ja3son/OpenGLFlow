#version 320 es
uniform mat4 uMVPMatrix;
uniform float uAngleSpan;
uniform float uYSpan;
uniform float uYStart;

layout (location = 0) in vec3 aPosition;
layout (location = 1) in vec2 aTexCoor;
out vec2 vTexCoor;

void main() {
    vec3 tempPos = aPosition;
    float currAngle = uAngleSpan * (tempPos.y - uYStart) / uYSpan;

    if (tempPos.y > uYStart) {
        tempPos.x = (cos(currAngle) * aPosition.x - sin(currAngle) * aPosition.z);
        tempPos.z = (sin(currAngle) * aPosition.x + cos(currAngle) * aPosition.z);
    }

    gl_Position = uMVPMatrix * vec4(tempPos, 1.0);
    vTexCoor = aTexCoor;
}
