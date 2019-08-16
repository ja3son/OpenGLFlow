#version 320 es
precision mediump float;
uniform sampler2D sTexture;
in vec4 ambient;
in vec4 diffuse;
in vec4 specular;
in vec2 vTextureCoord;

layout(location = 0) out vec4 fragColor0;
layout(location = 1) out vec4 fragColor1;
layout(location = 2) out vec4 fragColor2;
layout(location = 3) out vec4 fragColor3;

void main() {
   vec4 finalColor=texture(sTexture, vTextureCoord);
   vec4 fragColor = finalColor*ambient+finalColor*specular+finalColor*diffuse;

   fragColor0 = fragColor;
   fragColor1 = vec4(fragColor.r, 0.0, 0.0, 1.0);
   fragColor2 = vec4(0.0, fragColor.g, 0.0, 1.0);
   fragColor3 = vec4(0.0, 0.0, fragColor.b, 1.0);
}