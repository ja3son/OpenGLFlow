#version 300 es
uniform mat4 uViewProjMatrix;
uniform mat4 uMMatrix;
uniform float totalNum;
uniform sampler2D noiseTex;
uniform sampler2D gradualTex;
in vec3 aPosition;
in vec2 aTexCoor;
out vec4 changeTexture;
out vec2 vTextureCoord;
void xz_Matrix(
in float posx,
in float posy,
in float posz,
in float xz_cos,
in float xz_sin,
out mat4 mmtrix
){
    mmtrix=mat4(xz_cos+(1.0-xz_cos)*posx*posx, (1.0-xz_cos)*posy*posx - xz_sin*posz,
    (1.0-xz_cos)*posz*posx + xz_sin*posy, 0.0,
    (1.0-xz_cos)*posx* posy + xz_sin*posz, xz_cos + (1.0-xz_cos)*posy*posy,
    (1.0-xz_cos)*posz*posy - xz_sin*posx, 0.0,
    (1.0-xz_cos)*posx*posz - xz_sin*posy, (1.0-xz_cos)*posy* posz + xz_sin* (1.0-xz_cos)*
    posx, xz_cos +posz*posz, 0.0,
    0.0, 0.0, 0.0, 1.0);
}

void main(){
    int colCount=int(sqrt(totalNum));
    const float unitSize=0.3;
    int col=gl_InstanceID%colCount;
    int row=gl_InstanceID/colCount;
    float xtex=float(col)/float(colCount);
    float ytex=float(row)/float(colCount);
    vec4 noiseVec=texture(noiseTex, vec2(xtex, ytex));
    float dot_product = dot(vec4(noiseVec.rgb, gl_InstanceID), vec4(12.9898, 78.233, 45.164, 94.673));
    float random=fract(cos(dot_product) * 43758.5453);
    float size=random+1.0;
    mat4 pyMatrix=mat4(
    1, 0, 0, 0,
    0, 1, 0, 0,
    0, 0, 1, 0,
    unitSize*float(col)*size-unitSize, 0, unitSize*float(row)*size-unitSize, 1);
    mat4 xzMatrix_X;
    mat4 xzMatrix_Y;
    float cx_Angle=random*360.0;
    float zt_Angle=random*30.0;
    float xz_Cos=cos(radians(cx_Angle));
    float xz_Sin=sqrt(1.0-(xz_Cos*xz_Cos));
    xz_Matrix(0.0, 1.0, 0.0, xz_Cos, xz_Sin, xzMatrix_Y);
    xz_Cos=cos(radians(zt_Angle));
    xz_Sin=sqrt(1.0-(xz_Cos*xz_Cos));
    xz_Matrix(1.0, 0.0, 0.0, xz_Cos, xz_Sin, xzMatrix_X);
    mat4 MVPMatrix=uViewProjMatrix*uMMatrix*pyMatrix*xzMatrix_Y*xzMatrix_X;
    gl_Position = MVPMatrix * vec4(aPosition, 1.0);
    vTextureCoord = aTexCoor;
    if (ytex<1.0/totalNum){
        ytex=0.2;
    }
    vec4 jbTex=texture(gradualTex, vec2(xtex, ytex));
    changeTexture=jbTex;
}