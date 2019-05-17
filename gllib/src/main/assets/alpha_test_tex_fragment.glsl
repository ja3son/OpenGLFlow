#version 320 es
precision mediump float;
uniform sampler2D sTexture;
in  vec2 vTexCoor;
out vec4 fragColor;
void main() {
    vec4 currentColor = texture(sTexture, vTexCoor);
    if (currentColor.a < 0.6) {
        discard;
    } else {
        fragColor = currentColor;
    }
}