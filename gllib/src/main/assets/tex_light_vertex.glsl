#version 320 es
uniform mat4 uMVPMatrix;
uniform mat4 uMMatrix;
uniform vec3 uLightLocation;
uniform vec3 uCamera;
in vec3 aPosition;
in vec3 aNormal;
in vec2 aTexCoor;
out vec2 vTextureCoord;
out vec4 vambient;
out vec4 vdiffuse;
out vec4 vspecular;

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
  vec3 normalTarget=aPosition+normal;
  vec3 newNormal=(uMMatrix*vec4(normalTarget,1)).xyz-(uMMatrix*vec4(aPosition,1)).xyz;
  newNormal=normalize(newNormal);

  vec3 eye= normalize(uCamera-(uMMatrix*vec4(aPosition,1)).xyz);

  vec3 vp= normalize(lightLocation-(uMMatrix*vec4(aPosition,1)).xyz);
  vp=normalize(vp);
  vec3 halfVector=normalize(vp+eye);
  float shininess=50.0;
  float nDotViewPosition=max(0.0,dot(newNormal,vp));
  diffuse=lightDiffuse*nDotViewPosition;
  float nDotViewHalfVector=dot(newNormal,halfVector);
  float powerFactor=max(0.0,pow(nDotViewHalfVector,shininess));
  specular=lightSpecular*powerFactor;
}

void main() {
   gl_Position = uMVPMatrix * vec4(aPosition,1);
   pointLight(normalize(aNormal),vambient,vdiffuse,vspecular,uLightLocation,vec4(0.3,0.3,0.3,1.0),vec4(0.7,0.7,0.7,1.0),vec4(0.3,0.3,0.3,1.0));
   vTextureCoord = aTexCoor;
}