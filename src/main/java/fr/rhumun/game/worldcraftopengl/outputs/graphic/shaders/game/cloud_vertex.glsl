#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texCoord; // unused
layout(location = 2) in float texID;   // unused
layout(location = 3) in vec3 normal;   // unused

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
    gl_Position = projection * view * model * vec4(position, 1.0);
}
