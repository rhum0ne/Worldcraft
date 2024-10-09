#version 330 core

out vec4 FragColor;
in vec2 TexCoord;
flat in float TextureID;  // Permet d'identifier quelle texture utiliser

uniform sampler2D textures[18];  // Tableau de textures

void main() {
    // Sélectionne la texture à utiliser avec l'identifiant TextureID
    int texIndex = int(TextureID);
    FragColor = texture(textures[texIndex], TexCoord);  // Utilise les coordonnées pour appliquer la texture
}