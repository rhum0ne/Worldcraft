#version 330 core

out vec4 FragColor;
in vec2 TexCoord;
flat in float TextureID;  // Permet d'identifier quelle texture utiliser

uniform sampler2DArray guiTextures;
uniform sampler2DArray textures;  // Tableau de textures
uniform int texturesNumber;

void main() {
    // Sélectionne la texture à utiliser avec l'identifiant TextureID
    int id = int(TextureID);
    if (id >= texturesNumber) {
        FragColor = texture(guiTextures, vec3(TexCoord, id-texturesNumber));
    } else {
        //FragColor = vec4(0.0, 1.0, 0.0, 1.0);  // Vert si faux
        FragColor = texture(textures, vec3(TexCoord, id));  // Utilise les coordonnées pour appliquer la texture
    }
}