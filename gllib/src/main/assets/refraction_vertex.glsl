#version 300 es
uniform mat4 uMVPMatrix;
uniform mat4 uMMatrix;
uniform vec3 uCamera;
in vec3 aPosition;
in vec3 aNormal;

out vec3 eyeVary;
out vec3 finalNormalVary;

void main() {
    gl_Position = uMVPMatrix * vec4(aPosition, 1.0);
    vec3 normalTarget = aPosition + aNormal;
    vec3 finalNormal = normalize((uMMatrix * vec4(normalTarget, 1.0)).xyz - (uMMatrix * vec4(aPosition, 1.0)).xyz);
    vec3 eye = -normalize(uCamera - (uMMatrix * vec4(aPosition, 1.0)).xyz);

    eyeVary = eye;
    finalNormalVary = finalNormal;
}