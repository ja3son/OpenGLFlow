#version 320 es
precision mediump float;
in vec2 vTextureCoord;
in vec4 vAmbient;
in vec4 vDiffuse;
in vec4 vSpecular;
out vec4 fragColor;
uniform sampler2D sTexture;
void main() {
  vec4 finalColor = texture(sTexture, vTextureCoord);
  fragColor = finalColor*vAmbient+finalColor*vSpecular+finalColor*vDiffuse;
}