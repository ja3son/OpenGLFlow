#version 320 es
precision mediump float;

in vec4 ambientZM;
in vec4 diffuseZM;
in vec4 specularZM;
in vec4 ambientFM;
in vec4 diffuseFM;
in vec4 specularFM;
out vec4 fragColor;

void main() {
    vec4 finalColor=vec4(0.9, 0.9, 0.9, 1.0);
    if (gl_FrontFacing) {
        fragColor = finalColor*ambientZM+finalColor*specularZM+finalColor*diffuseZM;
    } else {
        fragColor = finalColor*ambientFM+finalColor*specularFM+finalColor*diffuseFM;
    }
}