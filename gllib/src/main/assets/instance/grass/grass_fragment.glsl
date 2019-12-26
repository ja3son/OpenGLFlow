#version 300 es
precision mediump float;
uniform sampler2D sourceTex;
in vec2 vTextureCoord;
in vec4 changeTexture;
out vec4 fragColor;

void main() {
    vec4 finalColor= texture(sourceTex, vTextureCoord);
    fragColor =finalColor*changeTexture;
}