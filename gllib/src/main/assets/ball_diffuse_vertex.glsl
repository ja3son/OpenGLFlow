#version 320 es
uniform mat4 uMVPMatrix;
uniform mat4 uMMatrix;
uniform vec3 uLightLocation;
layout (location = 0) in vec3 aPosition;
layout (location = 1) in vec3 aNormal;
out vec3 vPosition;
out vec4 vDiffuse;

void pointLight(in vec3 normal,
    inout vec4 diffuse,
    in vec3 lightLocation,
    in vec4 lightDiffuse
) {
    vec3 normalTarget = aPosition + normal;
    vec3 newNormal = (uMMatrix * vec4(normalTarget, 1)).xyz - (uMMatrix * vec4(aPosition, 1)).xyz;
    newNormal = normalize(newNormal);
    vec3 vp = normalize(lightLocation - (uMMatrix * vec4(aPosition, 1)).xyz);
    vp = normalize(vp);
    float nDotViewPosition = max(0.0, dot(newNormal, vp));
    diffuse = lightDiffuse * nDotViewPosition;
}

void main() {
   gl_Position = uMVPMatrix * vec4(aPosition,1);
   vec4 diffuseTemp = vec4(0.0, 0.0, 0.0, 0.0);
   pointLight(normalize(aNormal), diffuseTemp, uLightLocation, vec4(0.8, 0.8, 0.8, 1.0));
   vDiffuse = diffuseTemp;
   vPosition = aPosition;
}
