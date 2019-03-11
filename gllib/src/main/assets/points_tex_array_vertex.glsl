#version 320 es
uniform mat4 uMVPMatrix;
uniform float uPointSize;
layout (location = 0) in vec3 aPosition;
out float vid;

void main() {
   gl_Position = uMVPMatrix * vec4(aPosition,1);
   gl_PointSize = uPointSize;
   vid = float(gl_VertexID);
}
