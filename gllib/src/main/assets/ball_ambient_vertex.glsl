#version 320 es
uniform mat4 uMVPMatrix;
layout (location = 0) in vec3 aPosition;
out vec3 vPosition;
out vec4 vAmbient;

void main() {
   gl_Position = uMVPMatrix * vec4(aPosition,1);
   vPosition = aPosition;
   vAmbient = vec4(0.15, 0.15, 0.15, 1.0);
}
