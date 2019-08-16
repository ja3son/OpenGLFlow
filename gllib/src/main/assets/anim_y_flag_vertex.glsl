#version 320 es
uniform mat4 uMVPMatrix;
uniform float uStartAngle;
uniform float uWidthSpan;

layout (location = 0) in vec3 aPosition;
layout (location = 1) in vec2 aTexCoor;
out vec2 vTexCoor;

const float angleSpanH = 4.0 * 3.14159265;

void main() {
    float startX = uWidthSpan / 2.0;
    float curAngleX = uStartAngle + ((aPosition.x - startX) / uWidthSpan) * angleSpanH;

    float heightSpan = uWidthSpan * 0.75;
    float startY = -heightSpan / 2.0;
    float curAngleY = ((aPosition.y - startY) / heightSpan) * angleSpanH;

    float finalZ = sin(curAngleY - curAngleX) * 0.1;

    gl_Position = uMVPMatrix * vec4(aPosition.x, aPosition.y, finalZ, 1.0);
    vTexCoor = aTexCoor;
}
