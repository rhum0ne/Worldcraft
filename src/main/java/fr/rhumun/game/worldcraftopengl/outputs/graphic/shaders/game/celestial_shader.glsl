#version 330 core

out vec2 TexCoord;
flat out float TypeID;

uniform mat4 view;
uniform mat4 projection;
uniform vec3 camPos;
uniform float angle;

const vec2 QUAD[6] = vec2[](
    vec2(-1.0,  1.0),
    vec2( 1.0,  1.0),
    vec2(-1.0, -1.0),
    vec2(-1.0, -1.0),
    vec2( 1.0,  1.0),
    vec2( 1.0, -1.0)
);

const vec2 UV[6] = vec2[](
    vec2(0.0, 0.0),
    vec2(1.0, 0.0),
    vec2(0.0, 1.0),
    vec2(0.0, 1.0),
    vec2(1.0, 0.0),
    vec2(1.0, 1.0)
);

const float RADIUS = 200.0;
const float HEIGHT = 50.0;
const float SIZE = 10.0;

void main() {
    int idx = gl_VertexID % 6;
    int quad = gl_VertexID / 6;

    float a = radians(angle + (quad == 1 ? 180.0 : 0.0));
    vec3 center = camPos + vec3(0.0, RADIUS * sin(a), -RADIUS * cos(a));

    vec3 right = vec3(1.0, 0.0, 0.0) * SIZE;
    vec3 up = vec3(view[0][1], view[1][1], view[2][1]) * SIZE;

    vec2 local = QUAD[idx];
    vec3 worldPos = center + local.x * right + local.y * up;

    gl_Position = projection * view * vec4(worldPos, 1.0);
    TexCoord = UV[idx];
    TypeID = float(quad);
}