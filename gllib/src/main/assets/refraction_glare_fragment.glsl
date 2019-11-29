#version 300 es
precision mediump float;
uniform samplerCube sTexture;

in vec3 eyeVary;
in vec3 finalNormalVary;
out vec4 fragColor;

vec4 refraction(float ratio) {
    return texture(sTexture, refract(eyeVary, finalNormalVary, ratio));
}

void main() {
    vec4 finalColor = vec4(0.0);
    finalColor.r = refraction(0.97).r;
    finalColor.g = refraction(0.955).g;
    finalColor.b = refraction(0.94).b;
    fragColor = vec4(finalColor.rgb, 1.0);
}