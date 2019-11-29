#version 300 es
precision mediump float;
uniform samplerCube sTexture;

in vec3 eyeVary;
in vec3 finalNormalVary;
out vec4 fragColor;

const float max_h = 0.7;
const float min_h = 0.2;

vec4 fresnel(float ratio) {
    vec3 coord;
    vec4 finalColor;
    float eyeAngle = abs(dot(-eyeVary, finalNormalVary));
    if (eyeAngle > max_h) {
        coord = refract(eyeVary, finalNormalVary, ratio);
        finalColor = texture(sTexture, coord);
    } else if (eyeAngle <= max_h && eyeAngle >= min_h) {
        float size_h = max_h - min_h;
        coord = reflect(eyeVary, finalNormalVary);
        vec4 reflectionColor = texture(sTexture, coord);
        coord = refract(eyeVary, finalNormalVary, ratio);
        vec4 refractionColor = texture(sTexture, coord);
        float persent = (eyeAngle - min_h) / size_h;
        finalColor = refractionColor * persent + (1.0 - persent) * reflectionColor;
    } else {
        coord = reflect(eyeVary, finalNormalVary);
        finalColor = texture(sTexture, coord);
    }
    return finalColor;
}

void main() {
    vec4 finalColor = vec4(0.0);
    finalColor.r = fresnel(0.97).r;
    finalColor.g = fresnel(0.955).g;
    finalColor.b = fresnel(0.94).b;
    fragColor = vec4(finalColor.rgb, 1.0);
}