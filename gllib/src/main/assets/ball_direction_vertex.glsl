#version 320 es
uniform mat4 uMVPMatrix;
uniform mat4 uMMatrix;
uniform vec3 uLightDirection;
uniform vec3 uCamera;
layout (location = 0) in vec3 aPosition;
layout (location = 1) in vec3 aNormal;
out vec3 vPosition;
out vec4 vAmbient;
out vec4 vDiffuse;
out vec4 vSpecular;

void pointLight(in vec3 normal,
    inout vec4 ambient,
    inout vec4 diffuse,
    inout vec4 specular,
    in vec3 lightDirection,
    in vec4 lightAmbient,
    in vec4 lightDiffuse,
    in vec4 lightSpecular
) {
    ambient=lightAmbient;
    vec3 normalTarget = aPosition + normal;
    vec3 newNormal = (uMMatrix * vec4(normalTarget, 1)).xyz - (uMMatrix * vec4(aPosition, 1)).xyz;
    newNormal = normalize(newNormal);
    vec3 vp = normalize(lightDirection);
    float nDotViewPosition=max(0.0,dot(newNormal,vp));
    diffuse=lightDiffuse*nDotViewPosition;
    vec3 eye = normalize(uCamera - (uMMatrix * vec4(aPosition, 1)).xyz);
    vec3 halfVector = normalize(vp + eye);
    float shininess = 50.0;
    float nDotViewHalfVector = dot(newNormal, halfVector);
    float powerVector = max(0.0, pow(nDotViewHalfVector, shininess));
    specular = lightSpecular * powerVector;
}

void main() {
   gl_Position = uMVPMatrix * vec4(aPosition,1);
   vec4 ambientTemp,diffuseTemp,specularTemp;
   pointLight(normalize(aNormal), ambientTemp, diffuseTemp, specularTemp, uLightDirection,
   vec4(0.15,0.15,0.15,1.0),vec4(0.8,0.8,0.8,1.0),vec4(0.7,0.7,0.7,1.0));
   vAmbient = ambientTemp;
   vDiffuse = diffuseTemp;
   vSpecular = specularTemp;
   vPosition = aPosition;
}
