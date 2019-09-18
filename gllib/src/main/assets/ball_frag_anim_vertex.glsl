#version 320 es
uniform mat4 uMVPMatrix;
in vec3 aPosition;
in vec2 aLongLat;
out vec2 vLongLat;

void main() {
    gl_Position = uMVPMatrix * vec4(aPosition, 1);
    vLongLat = aLongLat;
}
