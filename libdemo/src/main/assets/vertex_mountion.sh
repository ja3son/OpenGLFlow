#version 300 es
uniform mat4 uMVPMatrix;
in vec3 aPosition;
in vec2 aTexCoor;
out vec2 vTextureCoord;
out float currY;
void main()     
{                            		
   gl_Position = uMVPMatrix * vec4(aPosition,1);
   vTextureCoord = aTexCoor;
   currY=aPosition.y;
}                      