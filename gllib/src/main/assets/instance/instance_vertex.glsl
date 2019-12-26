#version 300 es
uniform mat4 uViewProjMatrix;
uniform mat4 uMMatrix;
in vec3 aPosition;
in vec2 aTexCoor;
out vec2 vTextureCoord;
void main() {
   const int colCount=3;
   const float unitSize=4.0;
   int col=gl_InstanceID%colCount;
   int row=gl_InstanceID/colCount;
   mat4 pyMatrix=mat4(
   		1,0,0,0,
   		0,1,0,0,
   		0,0,1,0,
   		unitSize*float(col)-unitSize,unitSize*float(row)-unitSize,0,1
   );
   mat4 MVPMatrix=uViewProjMatrix*uMMatrix*pyMatrix;
   gl_Position = MVPMatrix * vec4(aPosition,1.0);
   vTextureCoord = aTexCoor;
}