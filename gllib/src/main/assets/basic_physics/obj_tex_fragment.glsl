#version 300 es
precision mediump float;
in vec4 ambient;
in vec4 diffuse;
in vec4 specular;
in vec2 vTextureCoord;
out vec4 fragColor;
void main() {
   vec4 finalColor=vec4(1.0);
   fragColor = finalColor*ambient+finalColor*specular+finalColor*diffuse;
}