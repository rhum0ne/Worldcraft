#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoord;
layout (location = 2) in float texID; // ID de texture

out vec2 TexCoord;
flat out float TextureID; // Doit être "flat" pour éviter l'interpolation

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main()
{
    gl_Position = projection * view * model * vec4(position, 1.0);
    TexCoord = texCoord;
    TextureID = texID; // On passe l'ID de texture au fragment shader
}
