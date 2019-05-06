#version 320 es
precision mediump float;
uniform sampler2D sTexture;
in float vEdge;
in vec2 vTextureCoord;
out vec4 fragColor;

#define theshold_temp 5.0

void main() {
    vec4 finalColor=vec4(floor(vTextureCoord.x * theshold_temp) / theshold_temp, 0.0, 0.0, 1.0);
    const vec4 edgeColor=vec4(0.0);
    float mbFactor=step(0.2, vEdge);
    fragColor=(1.0-mbFactor)*edgeColor+mbFactor*finalColor;
}