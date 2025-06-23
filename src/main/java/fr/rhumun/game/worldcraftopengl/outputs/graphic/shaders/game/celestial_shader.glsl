#version 330 core
layout(location = 0) in vec3 aPos;
layout(location = 1) in vec2 aTexCoord;
layout(location = 2) in float aType;

out vec2 TexCoord;
flat out float TypeID;

uniform mat4 view;
uniform mat4 projection;
uniform float angle;

const float RADIUS = 400.0;
const float HEIGHT = 200.0;
const float SIZE = 30.0;

void main() {
    float a = radians(angle + (aType > 0.5 ? 180.0 : 0.0));
    vec3 center = vec3(RADIUS * cos(a), HEIGHT, RADIUS * sin(a));

    vec3 right = vec3(view[0][0], view[1][0], view[2][0]) * SIZE;
    vec3 up = vec3(view[0][1], view[1][1], view[2][1]) * SIZE;

    vec3 worldPos = center + aPos.x * right + aPos.y * up;

    gl_Position = projection * view * vec4(worldPos, 1.0);
    TexCoord = aTexCoord;
    TypeID = aType;
}
