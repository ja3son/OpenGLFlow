#version 320 es
uniform mat4 uMVPMatrix;

uniform sampler2D sTextureOne;

uniform float lowest;
uniform float highest;

uniform float texCount;

layout (location = 0) in vec3 aPosition;
layout (location = 1) in vec2 aTexCoor;

out vec2 vTexCoor;
out float currY;

void main() {
    vec4 landColor = texture(sTextureOne, aTexCoor);
    currY = ((landColor.r + landColor.g + landColor.b) / 3.0) * highest + lowest;

    gl_Position = uMVPMatrix * vec4(aPosition.x, currY, aPosition.z, 1);

    vTexCoor = aTexCoor * texCount;
}
