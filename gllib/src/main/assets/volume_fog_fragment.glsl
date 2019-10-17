#version 320 es
precision mediump float;
in vec2 vTextureCoord;
in float currY;
in vec4 pLocation;

uniform float slabY;
uniform float startAngle;
uniform vec3 uCamaraLocation;
uniform sampler2D sTextureGrass;
uniform sampler2D sTextureRock;
uniform float landStartY;
uniform float landYSpan;

out vec4 fragColor;

float tjFogCal(vec4 pLocation){
    float xAngle=pLocation.x/16.0*3.1415926;
    float zAngle=pLocation.z/20.0*3.1415926;
    float slabYFactor=sin(xAngle+zAngle+startAngle);
    float t=(slabY+slabYFactor-uCamaraLocation.y)/(pLocation.y-uCamaraLocation.y);
    if (t>0.0&&t<1.0){
        float xJD=uCamaraLocation.x+(pLocation.x-uCamaraLocation.x)*t;
        float zJD=uCamaraLocation.z+(pLocation.z-uCamaraLocation.z)*t;
        vec3 locationJD=vec3(xJD, slabY, zJD);
        float L=distance(locationJD, pLocation.xyz);
        float L0=10.0;
        return L0/(L+L0);
    } else {
        return 1.0;
    }
}

void main(){
    vec4 gColor=texture(sTextureGrass, vTextureCoord);
    vec4 rColor=texture(sTextureRock, vTextureCoord);
    vec4 finalColor;
    if (currY<landStartY){
        finalColor=gColor;
    } else if (currY>landStartY+landYSpan){
        finalColor=rColor;
    } else {
        float currYRatio=(currY-landStartY)/landYSpan;
        finalColor= currYRatio*rColor+(1.0- currYRatio)*gColor;
    }
    float fogFactor=tjFogCal(pLocation);
    fragColor=fogFactor*finalColor+ (1.0-fogFactor)*vec4(0.9765, 0.7490, 0.0549, 0.0);
}