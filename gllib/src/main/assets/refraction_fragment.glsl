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
    fragColor = refraction(0.94);
}