#version 300 es
precision mediump float;
uniform vec4 uStartColor;
uniform vec4 uEndColor;
uniform float uRadius;
uniform sampler2D sTexture;

in float reduceFactor;
in vec4 vPosition;
out vec4 fragColor;

void main() {
    if (vPosition.w == 10.0) {
        fragColor = vec4(0.0);
    } else {
        //从内建变量获取纹理坐标
        vec2 texCoor = gl_PointCoord;
        vec4 currColor = texture(sTexture, texCoor);
        vec4 finalColor = vec4(0.0);
        float dis = distance(vPosition.xyz, vec3(0.0));
        float scaleFactor = (1.0 - dis / uRadius) * reduceFactor;
        vec4 scaleVec = vec4(scaleFactor);
        finalColor = clamp(scaleVec, uEndColor, uStartColor);
        finalColor = finalColor * currColor.a;
        fragColor = finalColor;
    }
}