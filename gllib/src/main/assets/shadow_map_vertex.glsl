#version 300 es
uniform mat4 uMVPMatrix;
uniform mat4 uMMatrix;

in vec3 aPosition;
out vec4 vPosition;

void main() {
    gl_Position = uMVPMatrix * vec4(aPosition, 1.0);
    vPosition = uMMatrix * vec4(aPosition, 1.0);
}