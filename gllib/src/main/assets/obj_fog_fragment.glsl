#version 320 es
precision mediump float;
in vec4 ambient;
in vec4 diffuse;
in vec4 specular;
in float vFogFactor;
out vec4 fragColor;
void main() {
    vec4 objectColor=vec4(0.95, 0.95, 0.95, 1.0);
    vec4 fogColor = vec4(0.97, 0.76, 0.03, 1.0);
    if (vFogFactor != 0.0) {
        objectColor = objectColor*ambient+objectColor*specular+objectColor*diffuse;
        fragColor = objectColor*vFogFactor + fogColor*(1.0-vFogFactor);
    } else {
        fragColor=fogColor;
    }
}