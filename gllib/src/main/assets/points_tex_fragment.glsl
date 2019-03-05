#version 320 es
precision mediump float;
uniform sampler2D sTexture;
out vec4 fragColor;
void main() {
   vec2 vTexCoor = gl_PointCoord;
   fragColor = texture(sTexture, vTexCoor);
}