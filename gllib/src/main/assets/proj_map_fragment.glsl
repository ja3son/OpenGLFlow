#version 300 es
precision mediump float;
uniform sampler2D sTexture;
in vec4 ambient;
in vec4 diffuse;
in vec4 specular;
in vec4 vPosition;
out vec4 fragColor;
uniform highp mat4 uViewProjatrix;
void main(){
    vec4 gytyPosition = uViewProjatrix * vec4(vPosition.xyz, 1);
    gytyPosition=gytyPosition/gytyPosition.w;
    float s=gytyPosition.s+0.5;
    float t=gytyPosition.t+0.5;
    vec4 finalColor=vec4(0.8, 0.8, 0.8, 1.0);
    if (s>=0.0&&s<=1.0&&t>=0.0&&t<=1.0){
        vec4 projColor=texture(sTexture, vec2(s, t));
        vec4 specularTemp=projColor*specular;
        vec4 diffuseTemp=projColor*diffuse;
        fragColor=finalColor*ambient+finalColor*specularTemp+finalColor*diffuseTemp;
    } else {
        fragColor = finalColor*ambient+finalColor*specular+finalColor*diffuse;
    }
}