#version 320 es
uniform mat4 uMVPMatrix;
uniform float flag;
in vec3 aOnePosition;
in vec3 aTwoPosition;
in vec3 aThreePosition;
in vec2 aTexCoor;
out vec2 vTextureCoord;

void main() {
    vec3 finalPos = vec3(0.0);
    if (flag <= 1.0) {
        finalPos = mix(aOnePosition, aTwoPosition, flag);
    } else {
        finalPos = mix(aTwoPosition, aThreePosition, flag);
    }
    gl_Position = uMVPMatrix * vec4(finalPos, 1.0);
    vTextureCoord = aTexCoor;
}