#version 320 es
uniform mat4 uMVPMatrix;
layout (location = 0) in vec3 aPosition;
layout (location = 1) in vec3 aNormal;
out vec3 vPosition;
out vec3 vNormal;

void main() {
   gl_Position = uMVPMatrix * vec4(aPosition,1);
   vPosition = aPosition;
   vNormal = aNormal;
}
