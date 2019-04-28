#version 300 es
precision mediump float;
in vec2 vTextureCoord;
uniform sampler2D sTexture;
in float currY;
uniform sampler2D sTextureGrass;
uniform sampler2D sTextureRock;
uniform float landStartY;
uniform float landYSpan;

out vec4 fragColor;
void main() {
   	   float min=0.25;
	   float max=0.7;
	   
	   float currYRatio=(currY-landStartY)/landYSpan;
	   
	   vec4 gColor=texture(sTextureGrass, vTextureCoord); 
	   vec4 rColor=texture(sTextureRock, vTextureCoord); 
	   
	   vec4 finalColor;
	   
	   if(currYRatio<min)
	   {
	      finalColor=gColor;
	   }
	   else if(currYRatio>max)
	   {
	      finalColor=rColor;
	   }
	   else
	   {
	      float rockBL=(currYRatio-min)/(max-min);
	      finalColor=rockBL*rColor+(1.0-rockBL)*gColor;
	   }
	   fragColor = finalColor;
}              