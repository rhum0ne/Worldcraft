#version 330 core
layout(location = 0) in vec3 aPos;   // Position du sommet
layout(location = 1) in vec2 aTexCoord;  // Coordonnées de texture
layout(location = 2) in float aTexId;  // Coordonnées de texture

out vec2 TexCoord;
flat out float TextureID;
uniform float aspectRatio;

uniform mat4 projection; // Matrice orthographique (pixels -> clip space)

void main() {
    //vec3 scaledPos = aPos;
    //scaledPos.x /= aspectRatio;  // Ajuste la position en X par le ratio
    gl_Position = projection * vec4(aPos, 1.0);  // Transforme les coordonnées ajustées
    TextureID = aTexId;
    TexCoord = aTexCoord;           // Transmission des coordonnées de texture au fragment shader
}
