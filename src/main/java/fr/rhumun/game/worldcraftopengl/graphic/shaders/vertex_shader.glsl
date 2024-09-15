#version 330 core

layout(location = 0) in vec3 position;  // Attribut pour les coordonnées x, y, z
out vec2 TexCoord;  // Transmettre les coordonnées de texture au fragment shader
layout (location = 1) in vec2 aTexCoord;  // Coordonnées de texture
uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
    // Appliquer les transformations de l'espace modèle à l'espace clip
    gl_Position = projection * view * model * vec4(position, 1.0);
    TexCoord = aTexCoord;           // Transférer les coordonnées de texture au fragment shader
}
