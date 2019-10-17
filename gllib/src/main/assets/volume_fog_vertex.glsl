#version 320 es
uniform mat4 uMVPMatrix;
uniform mat4 uMMatrix;

layout (location = 0) in vec3 aPosition;
layout (location = 1) in vec2 aTexCoor;

out vec2 vTextureCoord;
out float currY;
out vec4 pLocation;

void main() {
    vTextureCoord = aTexCoor;
    currY = aPosition.y;
    gl_Position = uMVPMatrix * vec4(aPosition, 1.0);
    pLocation = uMMatrix * vec4(aPosition, 1.0);
}
