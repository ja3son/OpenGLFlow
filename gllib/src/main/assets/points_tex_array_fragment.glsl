#version 320 es
precision mediump float;
uniform mediump sampler2DArray sTexture;
out vec4 fragColor;
in float vid;
void main() {
   vec3 vTexCoor = vec3(gl_PointCoord.st, vid);
   fragColor = texture(sTexture, vTexCoor);
}