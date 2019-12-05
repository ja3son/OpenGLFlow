#version 300 es
precision mediump float;
uniform sampler2D sTexture;
in vec4 ambient;
in vec4 diffuse;
in vec4 specular;
in vec4 vPosition;
uniform highp mat4 uLightViewProjatrix;
out vec4 fragColor;
void main() {
    vec4 gytyPosition=uLightViewProjatrix * vec4(vPosition.xyz, 1);
    gytyPosition=gytyPosition/gytyPosition.w;
    float s=gytyPosition.s+0.5;
    float t=gytyPosition.t+0.5;
    vec4 finalcolor=vec4(0.8, 0.8, 0.8, 1.0);
    vec4 colorA=finalcolor*ambient+finalcolor*specular+finalcolor*diffuse;
    vec4 colorB=vec4(0.1, 0.1, 0.1, 0.0);
    if (s>=0.0&&s<=1.0&&t>=0.0&&t<=1.0) {
        vec4 projColor=texture(sTexture, vec2(s, t));
        float a=step(0.9999, projColor.r);
        float b=step(0.0001, projColor.r);
        float c=1.0-sign(a);
        fragColor =a*colorA+(1.0-b)*colorB+b*c*mix(
        colorB, colorA, smoothstep(0.0, 1.0, projColor.r));
    } else {
        fragColor = colorA;
    }
}
