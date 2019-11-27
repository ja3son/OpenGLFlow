#version 300 es
uniform int group_count_feedback;
uniform int count_feedback;
uniform float life_span_step_feedback;

in vec4 speedx_pos_feedback;
in vec4 speedy_pos_feedback;
out vec4 vPosition;

void main() {
    vec4 position = speedx_pos_feedback;
    if (speedx_pos_feedback.w == 10.0) {
        int id = gl_VertexID;
        if ((count_feedback * group_count_feedback) <= id &&
        id < (count_feedback * group_count_feedback + group_count_feedback)) {
            position.w = life_span_step_feedback;
        }
        vPosition = position;
    } else {
        position.w = position.w + life_span_step_feedback;
        if (position.w > speedy_pos_feedback.w) {
            position.x = speedy_pos_feedback.x;
            position.y = speedy_pos_feedback.y;
            position.w = 10.0;
        } else {
            position.x = position.x + position.z;
            position.y = position.y + speedy_pos_feedback.z;
        }
        vPosition = position;
    }
}