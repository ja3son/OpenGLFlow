#version 300 es
precision highp float;
uniform highp sampler2D sTexture;
uniform highp vec3 uLightLocation;
uniform highp mat4 uViewProjatrix;
in vec4 ambient;
in vec4 diffuse;
in vec4 specular;
in vec4 vPosition;
out vec4 fragColor;
void main(){
    vec4 gytyPosition=uViewProjatrix * vec4(vPosition.xyz, 1);
    gytyPosition=gytyPosition/gytyPosition.w;
    float s=(gytyPosition.s+1.0)/2.0;
    float t=(gytyPosition.t+1.0)/2.0;
    float minDis=texture(sTexture, vec2(s, t)).r;
    float currDis=distance(vPosition.xyz, uLightLocation);
    vec4 finalColor=vec4(0.95, 0.95, 0.95, 1.0);
    float a=currDis-3.0;
    const float p=0.8;
    const float doublep=1.6;
    if (s>=0.0&&s<=1.0&&t>=0.0&&t<=1.0) {
        if (minDis<=a-p) {
            fragColor=finalColor*ambient;
        } else if (minDis>=a+p){
            fragColor = finalColor*ambient+finalColor*specular+finalColor*diffuse;
        } else {
            vec4 color = finalColor*ambient;
            vec4 color2 = finalColor*ambient+finalColor*specular+finalColor*diffuse;
            float b=(a+p-minDis)/doublep;
            fragColor=b*color+(1.0-b)*color2;
        }
    } else {
        fragColor = finalColor*ambient+finalColor*specular+finalColor*diffuse;
    }
}
