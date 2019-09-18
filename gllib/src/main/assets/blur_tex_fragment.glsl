#version 320 es
precision mediump float;
uniform sampler2D sTexture;
in  vec2 vTexCoor;
out vec4 fragColor;

const float uQuantize = 18.0;
const float uMagTol = 0.2;

void main() {
    if (vTexCoor.x < 0.5) {
        fragColor = texture(sTexture, vTexCoor);
        return;
    }

    ivec2 i_resolution = textureSize(sTexture, 0);
    float res_s = float(i_resolution.s);
    float res_t = float(i_resolution.t);
    vec3 rgb = texture(sTexture, vTexCoor).rgb;

    vec2 transverse = vec2(1.0 / res_s, 0.0);
    vec2 portrait = vec2(0.0, 1.0 / res_t);

    vec2 trans_step = vec2(1.0 / res_s, 1.0 / res_t);
    vec2 port_step = vec2(1.0 / res_s, -1.0 / res_t);

    vec3 W = vec3(0.2125, 0.7154, 0.0721);

    float im1m1=dot(texture(sTexture, vTexCoor-trans_step).rgb, W);
    float ip1p1=dot(texture(sTexture, vTexCoor+trans_step).rgb, W);
    float im1p1=dot(texture(sTexture, vTexCoor-port_step).rgb, W);
    float ip1m1=dot(texture(sTexture, vTexCoor+port_step).rgb, W);
    float im10=dot(texture(sTexture, vTexCoor-transverse).rgb, W);
    float ip10=dot(texture(sTexture, vTexCoor+transverse).rgb, W);
    float i0m1=dot(texture(sTexture, vTexCoor-portrait).rgb, W);
    float i0p1=dot(texture(sTexture, vTexCoor+portrait).rgb, W);

    float h = -1.0 * im1p1 - 2.0 * i0p1 - 1.0 * ip1p1 + 1.0 * im1m1 + 2.0 * i0m1 + 1.0 * ip1m1;
    float v = -1.0 * im1m1 - 2.0 * im10 - 1.0 * im1p1 + 1.0 * ip1m1 + 2.0 * ip10 + 1.0 * ip1p1;
    float mag=length(vec2(h, v));

    if (mag>uMagTol) {
        fragColor=vec4(0.0, 0.0, 0.0, 1.0);
    } else {
        rgb.rgb *= uQuantize;
        rgb.rgb += vec3(0.4);
        ivec3 intrgb = ivec3(rgb.rgb);
        rgb.rgb = vec3(intrgb) / uQuantize;
        fragColor=vec4(rgb, 1.0);
    }
}