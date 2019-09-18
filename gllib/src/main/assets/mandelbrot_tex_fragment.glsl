#version 320 es
precision mediump float;

in  vec2 vTexCoor;
out vec4 fragColor;


const float maxIterations = 88.0;
const float zoom = 1.0;
const float xCenter = 0.0;
const float yCenter = 0.0;
const vec3 innerColor = vec3(0.0, 0.0, 1.0);
const vec3 outerColor1 = vec3(1.0, 0.0, 0.0);
const vec3 outerColor2 = vec3(0.0, 1.0, 0.0);

void main() {
    float real = vTexCoor.x * zoom + xCenter;
    float imag = vTexCoor.y * zoom + yCenter;
    float cReal = real;
    float cImag = imag;
    float r2 = 0.0;
    float i;
    for (i=0.0; i<maxIterations && r2<4.0; i++){
        float tmpReal = real;
        real = (tmpReal * tmpReal) - (imag * imag) +cReal;
        imag = 2.0 *tmpReal * imag +cImag;
        r2 = (real * real) + (imag * imag);
    }
    vec3 color;
    if (r2 < 4.0) {
        color = innerColor;
    } else {
        color = mix(outerColor1, outerColor2, fract(i * 0.07));
    }
    fragColor = vec4(color, 1.0);
}