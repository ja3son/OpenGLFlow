#version 320 es
uniform mat4 uMVPMatrix;
uniform float ratio;
in vec3 aPosition;
in vec2 aTexCoor;
out vec2 vTextureCoord;

const float pi = 3.1415926;

void main() {
    float center_x = 0.0;
    float center_y = -5.0;

    float curr_x = aPosition.x;
    float curr_y = aPosition.y;

    float span_x = curr_x - center_x;
    float span_y = curr_y - center_y;

    float curr_radius = sqrt(span_x * span_x + span_y * span_y);
    float curr_radians = 0.0;

    if (span_x != 0.0) {
        curr_radians = atan(span_y, span_x);
    } else {
        curr_radians = span_y > 0.0 ? (pi / 2.0) : (3.0 * pi / 2.0);
    }

    float result_radians = curr_radians + ratio * curr_radius;
    float resut_x = center_x + curr_radius * cos(result_radians);
    float resut_y = center_y + curr_radius * sin(result_radians);
    gl_Position = uMVPMatrix * vec4(resut_x, resut_y, 0.0, 1.0);
    vTextureCoord = aTexCoor;
}
