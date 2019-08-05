#version 320 es
uniform mat4 uMVPMatrix;
in vec3 aPosition;
in vec2 aTexCoor;
out vec2 vTextureCoord;

void main() {
    gl_Position = uMVPMatrix * vec4(aPosition, 1.0);
    vTextureCoord = aTexCoor;
}
