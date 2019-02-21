#version 320 es
precision mediump float;
uniform sampler2D sTexture;
in  vec2 vTexCoor;
out vec4 fragColor;
void main() {
   fragColor = texture(sTexture, vTexCoor);
}