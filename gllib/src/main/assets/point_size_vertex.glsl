#version 320 es
uniform mat4 uMVPMatrix;
layout (location = 0) in vec3 aPosition;
layout (location = 1) in vec4 aColor;
out vec4 vColor;

void main() {
   gl_Position = uMVPMatrix * vec4(aPosition,1);
   gl_PointSize=10.0;
   vColor = aColor;
}
