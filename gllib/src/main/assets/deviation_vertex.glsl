#version 300 es
in vec3 aPosition;
in vec2 aTexCoor;
in vec3 aNormal;
in vec3 aNoiseNormal;

uniform mat4 uMVPMatrix;

out vec3 vNormal;
out vec3 vNoiseNormal;
out vec3 vPosition;
out vec2 vTextureCoord;

void main() {
    gl_Position = uMVPMatrix * vec4(aPosition, 1.0);
    vNormal = aNormal;
    vNoiseNormal = aNoiseNormal;
    vPosition = aPosition;
    vTextureCoord = aTexCoor;
}
