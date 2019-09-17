#version 320 es
precision mediump float;

in vec2 vTextureCoord;
out vec4 fragColor;

uniform sampler2D sTexture;

void main() {
    fragColor = texture(sTexture, vTextureCoord);
}