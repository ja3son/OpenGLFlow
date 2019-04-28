#version 320 es
precision mediump float;

uniform sampler2D sTextureOne;
uniform sampler2D sTextureTwo;

uniform float startY;
uniform float ySpan;

in  vec2 vTexCoor;
in float currY;

out vec4 fragColor;

void main() {
    vec4 colorOne = texture(sTextureOne, vTexCoor);
    vec4 colorTwo = texture(sTextureTwo, vTexCoor);

    vec4 finalColor;
    if (currY < startY) {
        finalColor = colorOne;
    } else if (currY > startY + ySpan) {
        finalColor = colorTwo;
    } else {
        float currYRatio = (currY - startY) / ySpan;
        finalColor = mix(colorOne, colorTwo, currYRatio);
    }
    fragColor = finalColor;
}