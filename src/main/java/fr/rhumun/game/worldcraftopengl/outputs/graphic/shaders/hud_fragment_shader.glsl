#version 330 core

out vec4 FragColor;
in vec2 TexCoord;
flat in float TextureID;  // Permet d'identifier quelle texture utiliser

uniform sampler2D textures[27];  // Tableau de textures
uniform sampler2D guiTextures[5];
uniform int texturesNumber;

void main() {
    // Sélectionne la texture à utiliser avec l'identifiant TextureID
    int id = int(TextureID);
    if (id > texturesNumber) {
        FragColor = texture(guiTextures[id-texturesNumber], TexCoord);  // Utilise les coordonnées pour appliquer la texture
    } else {
        //FragColor = vec4(0.0, 1.0, 0.0, 1.0);  // Vert si faux
        FragColor = texture(textures[id], TexCoord);  // Utilise les coordonnées pour appliquer la texture
    }
}