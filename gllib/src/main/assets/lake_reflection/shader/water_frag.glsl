#version 300 es
precision mediump float;
uniform highp mat4 uMVPMatrixMirror;

uniform sampler2D sTextureDY;
uniform sampler2D sTextureWater;
uniform sampler2D sTextureNormal;

uniform vec3 uCamera;
uniform vec3 uLightLocation;

in mat4 vMMatrix;
in vec4 vPosition;
in vec2 vTextureCoord;
in vec3 fNormal;
in vec3 mvPosition;
out vec4 fragColor;

void pointLight(
in vec3 normal,
inout vec4 ambient,
inout vec4 diffuse,
inout vec4 specular,
in vec3 lightLocation,
in vec4 lightAmbient,
in vec4 lightDiffuse,
in vec4 lightSpecular
){
    ambient=lightAmbient;
    vec3 normalTarget=mvPosition+normal;
    vec3 newNormal=(vMMatrix*vec4(normalTarget, 1)).xyz-(vMMatrix*vec4(mvPosition, 1)).xyz;
    newNormal=normalize(newNormal);

    vec3 eye= normalize(uCamera-(vMMatrix*vec4(mvPosition, 1)).xyz);

    vec3 vp= normalize(lightLocation-(vMMatrix*vec4(mvPosition, 1)).xyz);
    vp=normalize(vp);
    vec3 halfVector=normalize(vp+eye);

    float nDotViewPosition=max(0.0, dot(newNormal, vp));
    diffuse=lightDiffuse*nDotViewPosition;

    float nDotViewHalfVector=dot(newNormal, halfVector);
    float shininess=50.0;
    float powerFactor=max(0.0, pow(nDotViewHalfVector, shininess));

    specular=lightSpecular*powerFactor;
}
void main() {
    vec4 gytyPosition=uMVPMatrixMirror * vec4(vPosition.xyz, 1);
    gytyPosition=gytyPosition/gytyPosition.w;
    float s=(gytyPosition.s+1.0)/2.0;
    float t=(gytyPosition.t+1.0)/2.0;

    vec4 ambient, diffuse, specular;
    pointLight(normalize(fNormal), ambient, diffuse, specular, uLightLocation,
    vec4(0.9, 0.9, 0.9, 1.0), vec4(0.1, 0.1, 0.1, 1.0), vec4(0.9, 0.9, 0.9, 1.0));

    vec4 normalColor = texture(sTextureNormal, vTextureCoord);

    vec3 cNormal=vec3(2.0*(normalColor.r-0.5), 2.0*(normalColor.g-0.5), 2.0*(normalColor.b-0.5));
    cNormal=normalize(cNormal);

    const float mPerturbationAmt=0.02;
    s=s+mPerturbationAmt*s*cNormal.x;
    t=t+mPerturbationAmt*t*cNormal.y;


    vec4 dyColor=texture(sTextureDY, vec2(s, 1.0 - t));

    vec4 waterColor=texture(sTextureWater, vTextureCoord);

    vec4 dyAndWaterColor=mix(waterColor, dyColor, 0.7);

    fragColor=dyAndWaterColor*ambient+dyAndWaterColor*specular+dyAndWaterColor*diffuse;
}   