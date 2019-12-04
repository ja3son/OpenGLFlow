#version 300 es
precision highp float;
in vec2 vTextureCoord;

uniform sampler2D sTexture;

out vec4 fragColor;

void main() {
    float depthValue = texture(sTexture, vTextureCoord).r / 100.0;
    fragColor = vec4(vec3(depthValue), 1.0);
}