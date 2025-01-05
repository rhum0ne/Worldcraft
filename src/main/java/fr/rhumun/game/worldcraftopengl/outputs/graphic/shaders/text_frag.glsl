#version 330 core

out vec4 FragColor;
in vec2 TexCoord;
flat in float TextureID;  // Identifiant de la texture pour le glyphe

uniform sampler2D atlasTexture;  // Texture unique de l'atlas

void main() {
    // Utiliser les coordonnées de texture du glyphe dans l'atlas
    FragColor = texture(atlasTexture, TexCoord);
}