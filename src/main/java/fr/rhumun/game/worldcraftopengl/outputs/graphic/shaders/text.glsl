#version 330 core
layout(location = 0) in vec3 aPos;   // Position du sommet
layout(location = 1) in vec2 aTexCoord;  // Coordonnées de texture
layout(location = 2) in float aTexId;  // Identifiant de la texture (utile si plusieurs atlas ou types de texture)

out vec2 TexCoord;  // Coordonnées à passer au fragment shader
flat out float TextureID;

uniform mat4 projection;  // Matrice orthographique

void main() {
    gl_Position = projection * vec4(aPos, 1.0);
    TextureID = aTexId;
    TexCoord = aTexCoord;  // Ici, aTexCoord est déjà calculé pour la position dans l'atlas
}
