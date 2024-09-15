#version 330 core
out vec4 FragColor;

in vec2 TexCoord;  // Coordonnées de texture venant du vertex shader

uniform sampler2D ourTexture;  // L'uniforme pour la texture

void main() {
    // Appliquer la texture sur le fragment
    FragColor = texture(ourTexture, TexCoord);
}