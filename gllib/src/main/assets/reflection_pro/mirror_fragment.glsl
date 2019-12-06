#version 300 es
precision mediump float;
uniform highp mat4 uViewProjMatrix;
uniform sampler2D sTexture;
in vec4 vPosition;
out vec4 fragColor;
void main() {
    vec4 gytyPosition=uViewProjMatrix * vec4(vPosition.xyz, 1);
    gytyPosition=gytyPosition/gytyPosition.w;
    float s=(gytyPosition.s+1.0)/2.0;
    float t=(gytyPosition.t+1.0)/2.0;

    if (s>=0.0&&s<=1.0&&t>=0.0&&t<=1.0) {
        vec4 finalColor=texture(sTexture, vec2(s, t));
        fragColor = finalColor;
    } else {
        fragColor=vec4(0.0, 0.0, 0.0, 1.0);
    }
}