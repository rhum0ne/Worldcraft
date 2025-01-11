#version 330 core

out vec4 FragColor;
in vec2 TexCoord;
flat in float TextureID;  // Identifiant de la texture pour le glyphe

uniform sampler2DArray charsTextures;  // Texture unique de l'atlas
uniform vec4 textColor; // Couleur personnalisée pour le texte (passée en uniforme)

void main() {
    // Récupérer l'intensité du glyphe (monochrome, basé sur le canal rouge)
    float intensity = texture(charsTextures, vec3(TexCoord, TextureID)).r;
    //float intensity = (texture(charsTextures, vec3(TexCoord, TextureID)).r >= 0.8) ? 1.0 : 0;

    // Appliquer la couleur du texte avec une transparence basée sur l'intensité
    FragColor = vec4(textColor.rgb, intensity * textColor.a);
}