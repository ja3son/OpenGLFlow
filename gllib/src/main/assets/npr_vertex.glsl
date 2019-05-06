#version 320 es
uniform mat4 uMVPMatrix;
uniform mat4 uMMatrix;
uniform vec3 uLightLocation;
uniform vec3 uCamera;
in vec3 aPosition;
in vec3 aNormal;
out float vEdge;
out vec2 vTextureCoord;

void pointLight(
in vec3 normal,
out float diffuse,
out float specular,
out float edge,
in vec3 lightLocation,
in float lightDiffuse,
in float lightSpecular
) {
    vec3 normalTarget=aPosition+normal;
    vec3 newNormal=(uMMatrix*vec4(normalTarget, 1)).xyz-(uMMatrix*vec4(aPosition, 1)).xyz;
    newNormal=normalize(newNormal);

    vec3 eye= normalize(uCamera-(uMMatrix*vec4(aPosition, 1)).xyz);

    edge = max(0.0, dot(newNormal, eye));

    vec3 vp= normalize(lightLocation-(uMMatrix*vec4(aPosition, 1)).xyz);
    vp=normalize(vp);
    vec3 halfVector=normalize(vp+eye);
    float shininess=50.0;
    float nDotViewPosition=max(0.0, dot(newNormal, vp));
    diffuse=lightDiffuse*nDotViewPosition;
    float nDotViewHalfVector=dot(newNormal, halfVector);
    float powerFactor=max(0.0, pow(nDotViewHalfVector, shininess));
    specular=lightSpecular*powerFactor;
}

void main(){
    gl_Position = uMVPMatrix * vec4(aPosition, 1);
    float diffuse;
    float specular;
    pointLight(normalize(aNormal), diffuse, specular, vEdge, uLightLocation, 0.8, 0.9);
    float s=diffuse+specular;
    vTextureCoord=vec2(s, 0.5);
}