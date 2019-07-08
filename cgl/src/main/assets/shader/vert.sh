#version 300 es
uniform mat4 Modelview;
in vec3 Position;
in vec4 SourceColor;
out vec4 DestinationColor;
void main()
{
    DestinationColor = SourceColor;
    gl_Position = Modelview * vec4(Position,1);
}