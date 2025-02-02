#version 330 core

out vec4 FragColor;
in vec2 TexCoord;
flat in float TextureID;  // Permet d'identifier quelle texture utiliser

uniform sampler2DArray textures;  // Tableau de textures
uniform sampler2D[30] guiTextures;
uniform int texturesNumber;

void main() {
    // Sélectionne la texture à utiliser avec l'identifiant TextureID
    int id = int(TextureID);
    if (id >= texturesNumber) {
        FragColor = texture(guiTextures[id-texturesNumber], TexCoord);
    } else {
        FragColor = texture(textures, vec3(TexCoord, id));  // Utilise les coordonnées pour appliquer la texture
    }
}