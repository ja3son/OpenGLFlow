#version 300 es
uniform mat4 uMVPMatrix;
uniform vec3 uCamera;
uniform mat4 uMMatrix;
uniform mediump float uRadius;
uniform float uMaxLifeSpan;

in vec4 aPosition;
out float reduceFactor;
out vec4 vPosition;

void main() {
    vec4 currPosition = uMMatrix * vec4(aPosition.xy, 0.0, 1);
    float dis = distance(currPosition.xyz, uCamera);
    // 求出距离缩放因子S的平方分之1
    float scale_factor = 1.0 / sqrt(0.001 * dis * dis + 0.05 * dis + 0.01);
    gl_PointSize = uRadius * scale_factor;

    gl_Position = uMVPMatrix * vec4(aPosition.xy, 0.0, 1);
    vPosition = vec4(aPosition.xy, 0.0, aPosition.w);
    reduceFactor = (uMaxLifeSpan - aPosition.w) / uMaxLifeSpan;
}