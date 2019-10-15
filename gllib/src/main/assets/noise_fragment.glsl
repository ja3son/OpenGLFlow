#version 300 es
precision mediump float;
uniform sampler3D sTexture;

in vec4 ambient;
in vec4 diffuse;
in vec4 specular;
in vec3 vPosition;
out vec4 fragColor;
void main() {
    const vec4 lightWood=vec4(0.6, 0.3, 0.1, 1.0);
    const vec4 darkWood=vec4(0.4, 0.2, 0.07, 1.0);
    vec3 texCoor=vec3(((vPosition.x/0.52)+1.0)/2.0, vPosition.y/0.4, ((vPosition.z/0.52)+1.0)/2.0);
    vec4 noiseVec=texture(sTexture, texCoor);
    vec3 location=vPosition+noiseVec.rgb*0.05;
    float dis=distance(location.xz, vec2(0, 0));
    dis *=2.0;
    float r=fract(dis+noiseVec.r+noiseVec.g*0.5+noiseVec.b*0.25+noiseVec.a*0.125)*2.0;
    if (r>1.0) {
        r=2.0-r;
    }
    vec4 color=mix(lightWood, darkWood, r);
    r=fract((location.y+location.x+location.z)*25.0+0.5);
    noiseVec.a*=r;
    if (r<0.5) {
        color+=lightWood*1.0*noiseVec.a;
    } else {
        color-=lightWood*0.02*noiseVec.a;
    }
    fragColor =color*ambient+color*specular+color*diffuse;
}