#version 300 es
uniform mat4 uMVPMatrix;
uniform mat4 uMMatrix;
in vec3 aPosition;
in vec2 aTexCoor;
in vec3 aNormal;
out vec3 fNormal;
out vec2 vTextureCoord;
out vec4 vPosition;
out vec3 mvPosition;
out mat4 vMMatrix;
void main(){
    gl_Position = uMVPMatrix * vec4(aPosition, 1);
    vPosition=uMMatrix*vec4(aPosition, 1);
    vTextureCoord=aTexCoor;
    mvPosition=aPosition;
    fNormal=aNormal;
    vMMatrix=uMMatrix;
}
                 