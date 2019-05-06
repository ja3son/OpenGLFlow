#version 320 es
uniform mat4 uMVPMatrix;
in vec3 aPosition;
in vec3 aNormal;

void main() {
    vec3 position = aPosition;
    vec4 originView = uMVPMatrix * vec4(0, 0, 0, 1);
    vec4 normalView = uMVPMatrix * vec4(aNormal.xyz, 1.0);
    vec2 finalNormal = normalView.xy - originView.xy;
    finalNormal = normalize(finalNormal);
    vec4 finalPosition = uMVPMatrix * vec4(position.xyz, 1);
    finalPosition = finalPosition / finalPosition.w;
    gl_Position = finalPosition + vec4(finalNormal.xy, 1.0, 1.0) * 0.01;
}