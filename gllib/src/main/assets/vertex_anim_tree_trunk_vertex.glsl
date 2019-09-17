#version 320 es
layout (location = 0) in vec3 aPosition;
layout (location = 1) in vec2 aTexCoor;
out vec2 vTexCoor;

uniform mat4 uMVPMatrix;
uniform float uBendR;
uniform float uDirectionDegree;

void main() {
    float curr_radian = aPosition.y / uBendR;
    float result_height = uBendR * sin(curr_radian);
    float increase = uBendR - uBendR * cos(curr_radian);
    float result_X = aPosition.x + increase * sin(radians(uDirectionDegree));
    float result_Z = aPosition.z + increase * cos(radians(uDirectionDegree));
    vec4 result_point = vec4(result_X, result_height, result_Z, 1.0);
    gl_Position = uMVPMatrix * result_point;
    vTexCoor = aTexCoor;
}
