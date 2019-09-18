#version 320 es
precision mediump float;
in vec2 vLongLat;
out vec4 fragColor;

const vec3 brick_color = vec3(0.678, 0.231, 0.129);
const vec3 cement_color = vec3(0.763, 0.657, 0.614);

void main() {
    vec3 finalColor = vec3(0.0);

    if (abs(vLongLat.y) > 75.0) {
        finalColor = vec3(1.0);
    } else {
        int color_num = int(vLongLat.x / 45.0);
        if (color_num == 0) {
            finalColor = vec3(1.0, 0.0, 0.0);
        } else if (color_num == 1) {
            finalColor = vec3(0.0, 1.0, 0.0);
        } else if (color_num == 2) {
            finalColor = vec3(0.0, 0.0, 1.0);
        } else if (color_num == 3) {
            finalColor = vec3(1.0, 1.0, 0.0);
        } else if (color_num == 4) {
            finalColor = vec3(1.0, 0.0, 1.0);
        } else if (color_num == 5) {
            finalColor = vec3(0.0, 1.0, 1.0);
        } else if (color_num == 6) {
            finalColor = vec3(0.3, 0.4, 0.7);
        } else if (color_num == 7) {
            finalColor = vec3(0.3, 0.7, 0.2);
        }
    }

    fragColor = vec4(finalColor, 1.0);
}