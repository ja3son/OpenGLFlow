#version 300 es
precision mediump float;
in vec3 vNormal;
in vec3 vNoiseNormal;
in vec3 vPosition;
in vec2 vTextureCoord;

uniform sampler2D sTextureBG;
uniform sampler2D sTextureNormal;
uniform mat4 uMMatrix;
uniform vec3 uCamera;
uniform vec3 uLightLocation;

out vec4 fragColor;

void pointLight(
in vec3 normal,
out vec4 ambient,
out vec4 diffuse,
out vec4 specular,
in vec3 vp,
in vec3 eye,
in vec4 lightAmbient,
in vec4 lightDiffuse,
in vec4 lightSpecular
) {
    ambient = lightAmbient;
    vec3 halfVector = normalize(vp + eye);
    float shininess = 50.0;
    float nDotViewPosition = max(0.0, dot(normal, vp));
    diffuse = lightDiffuse * nDotViewPosition;
    float nDotViewHalfVector = dot(normal, halfVector);
    float powerFactor = max(0.0, pow(nDotViewHalfVector, shininess));
    specular = lightSpecular * powerFactor;
}

void main() {
    vec4 ambient, diffuse, specular;
    vec4 normalColor = texture(sTextureNormal, vTextureCoord);
    vec3 cNormal = vec3(2.0 * (normalColor.r - 0.5), 2.0 * (normalColor.g - 0.5), 2.0 * (normalColor.b - 0.5));
    cNormal = normalize(cNormal);
    vec3 normalTarget = vPosition + vNormal;
    vec3 newNormal = (uMMatrix * vec4(normalTarget, 1)).xyz - (uMMatrix * vec4(vPosition, 1)).xyz;
    newNormal = normalize(newNormal);
    vec3 tangentTarget = vPosition + vNoiseNormal;
    vec3 newTangent = (uMMatrix * vec4(tangentTarget, 1)).xyz - (uMMatrix * vec4(vPosition, 1)).xyz;
    newTangent = normalize(newTangent);
    vec3 binormal = normalize(cross(newTangent, newNormal));
    mat3 rotation = mat3(newTangent, binormal, newNormal);
    vec3 newPosition = (uMMatrix * vec4(vPosition, 1)).xyz;
    vec3 vp = normalize(uLightLocation - newPosition);
    vp = normalize(rotation * vp);
    vec3 eye = normalize(rotation * normalize(uCamera - newPosition));
    pointLight(cNormal, ambient, diffuse, specular, vp, eye,
    vec4(0.05, 0.05, 0.05, 1.0), vec4(1.0, 1.0, 1.0, 1.0), vec4(0.3, 0.3, 0.3, 1.0));
    vec4 finalColor = texture(sTextureBG, vTextureCoord);
    fragColor = finalColor * ambient + finalColor * specular + finalColor * diffuse;
}
