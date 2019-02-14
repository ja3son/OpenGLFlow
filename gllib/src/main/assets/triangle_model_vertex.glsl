#version 320 es
uniform mat4 uMVPMatrix;
uniform mat4 uMMatrix;
layout (location = 0) in vec3 aPosition;
layout (location = 1) in vec4 aColor;
out vec4 vColor;
out vec3 vPosition;

void main() {
   gl_Position = uMVPMatrix * vec4(aPosition,1);
   vColor = aColor;
   vPosition = (uMMatrix * vec4(aPosition, 1)).xyz;
}
