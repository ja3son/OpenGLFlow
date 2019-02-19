#version 320 es
uniform mat4 uMVPMatrix;
uniform mat4 uMMatrix;
uniform vec3 uLightLocation;
uniform vec3 uCamera;
layout (location = 0) in vec3 aPosition;
layout (location = 1) in vec3 aNormal;
out vec3 vPosition;
out vec4 vSpecular;

void pointLight(in vec3 normal,
    inout vec4 specular,
    in vec3 lightLocation,
    in vec4 lightSpecular
) {
    vec3 normalTarget = aPosition + normal;
    vec3 newNormal = (uMMatrix * vec4(normalTarget, 1)).xyz - (uMMatrix * vec4(aPosition, 1)).xyz;
    newNormal = normalize(newNormal);
    vec3 vp = normalize(lightLocation - (uMMatrix * vec4(aPosition, 1)).xyz);
    vp = normalize(vp);
    vec3 eye = normalize(uCamera - (uMMatrix * vec4(aPosition, 1)).xyz);
    vec3 halfVector = normalize(vp + eye);
    float shininess = 50.0;
    float nDotViewHalfVector = dot(newNormal, halfVector);
    float powerVector = max(0.0, pow(nDotViewHalfVector, shininess));
    specular = lightSpecular * powerVector;
}

void main() {
   gl_Position = uMVPMatrix * vec4(aPosition,1);
   vec4 specularTemp = vec4(0.0, 0.0, 0.0, 0.0);
   pointLight(normalize(aNormal), specularTemp, uLightLocation, vec4(0.7, 0.7, 0.7, 1.0));
   vSpecular = specularTemp;
   vPosition = aPosition;
}
