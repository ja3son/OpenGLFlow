#version 300 es
precision mediump float;
in vec2 vTextureCoord;

uniform sampler2D sTexture;
uniform sampler2D depthTexture;
uniform mat4 uViewProjectionInverseMatrix;
uniform mat4 uPreviousProjectionMatrix;
uniform int g_numSamples;

out vec4 fragColor;
void main() {
    vec2 textureCoord=vTextureCoord;

    float zOverW = texture(depthTexture, textureCoord).r;

    vec4 H = vec4(textureCoord.x*2.0-1.0, (1.0-textureCoord.y)*2.0-1.0, zOverW, 1.0);

    vec4 D = uViewProjectionInverseMatrix*H;
    vec4 worldPos= D/D.w;

    vec4 currentPos=H;

    vec4 previousPos=uPreviousProjectionMatrix*worldPos;

    previousPos=previousPos/previousPos.w;

    vec2 velocity=((previousPos-currentPos)/float(g_numSamples)).xy;

    vec4 color=texture(sTexture, textureCoord);
    textureCoord+=velocity;
    for (int i=1;i<g_numSamples;i++, textureCoord+=velocity) {
        vec4 currentColor=texture(sTexture, textureCoord);
        color+=currentColor;
    }

    fragColor=color/float(g_numSamples);
}