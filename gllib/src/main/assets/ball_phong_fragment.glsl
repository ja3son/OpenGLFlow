#version 320 es
precision mediump float;
uniform float uR;
uniform mat4 uMMatrix;
uniform vec3 uLightLocation;
uniform vec3 uCamera;
in  vec3 vPosition;
in vec3 vNormal;

out vec4 fragColor;

void pointLight(in vec3 normal,
    inout vec4 ambient,
    inout vec4 diffuse,
    inout vec4 specular,
    in vec3 lightLocation,
    in vec4 lightAmbient,
    in vec4 lightDiffuse,
    in vec4 lightSpecular
) {
    ambient=lightAmbient;
    vec3 normalTarget = vPosition + normal;
    vec3 newNormal = (uMMatrix * vec4(normalTarget, 1)).xyz - (uMMatrix * vec4(vPosition, 1)).xyz;
    newNormal = normalize(newNormal);
    vec3 vp = normalize(lightLocation - (uMMatrix * vec4(vPosition, 1)).xyz);
    vp = normalize(vp);
    float nDotViewPosition=max(0.0,dot(newNormal,vp));
    diffuse=lightDiffuse*nDotViewPosition;
    vec3 eye = normalize(uCamera - (uMMatrix * vec4(vPosition, 1)).xyz);
    vec3 halfVector = normalize(vp + eye);
    float shininess = 50.0;
    float nDotViewHalfVector = dot(newNormal, halfVector);
    float powerVector = max(0.0, pow(nDotViewHalfVector, shininess));
    specular = lightSpecular * powerVector;
}


void main() {
    vec3 color;
    float n = 8.0;
    float span = 2.0 * uR / n;
    int i = int((vPosition.x + uR) / span);
    int j = int((vPosition.y + uR) / span);
    int k = int((vPosition.z + uR) / span);

    int whichColor = int(mod(float( i + j + k), 2.0));

    if(whichColor == 1) {
        color = vec3(0.678,0.231,0.129);
    } else {
        color = vec3(1.0,1.0,1.0);
    }
    vec4 finalColor = vec4(color, 0);

   vec4 ambient,diffuse,specular;
   pointLight(normalize(vNormal), ambient, diffuse, specular, uLightLocation,
   vec4(0.15,0.15,0.15,1.0),vec4(0.8,0.8,0.8,1.0),vec4(0.7,0.7,0.7,1.0));
   fragColor = finalColor * ambient + finalColor * diffuse + finalColor * specular;
}