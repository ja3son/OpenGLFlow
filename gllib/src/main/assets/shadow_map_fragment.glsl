#version 300 es
precision highp float;

in vec4 vPosition;
out float fragColor;

uniform highp vec3 uLightLocation;

void main() {
    float dis = distance(vPosition.xyz, uLightLocation);
    fragColor = dis;
}