#version 320 es
precision mediump float;
in vec2 vLongLat;
out vec4 fragColor;

const vec3 brick_color = vec3(0.678, 0.231, 0.129);
const vec3 cement_color = vec3(0.763, 0.657, 0.614);

void main() {
    vec3 finalColor = vec3(0.0);
    int row = int(mod((vLongLat.y + 90.0) / 12.0, 2.0));
    float temp_y = mod(vLongLat.y + 90.0, 12.0);
    float offset = 0.0f;
    float temp_x = 0.0;

    if (temp_y > 10.0) {
        finalColor = cement_color;
    } else {
        if (row == 1) {
            offset = 11.0;
        }
        temp_x = mod(vLongLat.x + offset, 22.0);
        if (temp_x > 20.0) {
            finalColor = cement_color;
        } else {
            finalColor = brick_color;
        }
    }

    fragColor = vec4(finalColor, 1.0);
}