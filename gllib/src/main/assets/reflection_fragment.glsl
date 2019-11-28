#version 300 es
precision mediump float;
uniform samplerCube sTexture;
in vec3 vTextureCoord;
out vec4 fragColor;
void main() {
    fragColor=texture(sTexture, vTextureCoord);
}