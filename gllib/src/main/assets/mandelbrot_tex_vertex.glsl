#version 320 es
uniform mat4 uMVPMatrix;
layout (location = 0) in vec3 aPosition;
layout (location = 1) in vec2 aTexCoor;
out vec2 vTexCoor;

void main() {
    gl_Position = uMVPMatrix * vec4(aPosition, 1);
    vTexCoor = (aTexCoor - 0.5) * 5.0;
}
