#version 330 core

layout (location = 0) in vec3 position;   // Position du vertex dans l'espace objet
layout (location = 1) in vec2 texCoord;  // Coordonnées de texture
layout (location = 2) in float texID;    // ID de texture
layout (location = 3) in vec3 normal;

out vec2 TexCoord;                        // Sortie des coordonnées de texture pour le fragment shader
flat out float TextureID;                 // Sortie de l'ID de texture
flat out vec3 FragNormal;
out vec3 FragPos;                         // Position du fragment en espace monde

uniform mat4 model;                       // Matrice de modèle
uniform mat4 view;                        // Matrice de vue
uniform mat4 projection;                  // Matrice de projection

uniform float time;                       // Uniform pour le temps (dynamique)
uniform vec3 globalOffset;                // Décalage global (optionnel)

// Fonction Simplex-like pour simuler du bruit procédural
float hash(float n) { return fract(sin(n) * 43758.5453123); }

float noise(vec2 st) {
    vec2 i = floor(st);
    vec2 f = fract(st);

    // Interpolation de base
    float a = hash(i.x + i.y * 57.0);
    float b = hash(i.x + 1.0 + i.y * 57.0);
    float c = hash(i.x + (i.y + 1.0) * 57.0);
    float d = hash(i.x + 1.0 + (i.y + 1.0) * 57.0);

    vec2 u = f * f * (3.0 - 2.0 * f);

    return mix(a, b, u.x) + (c - a) * u.y * (1.0 - u.x) + (d - b) * u.x * u.y;
}

void main()
{
    // Réduction de taille autour du centre
    vec3 scaledPosition = position * vec3(1, 0.90, 1); // Réduit la taille de 5%
    vec3 offset = position * vec3(0, 0.097, 0);            // Recentrage pour compenser l'échelle (5% de 0.5)

    vec4 worldPosition = model * vec4(scaledPosition + offset, 1.0);

    FragPos = worldPosition.xyz;          // Position en espace monde pour le fragment shader
    gl_Position = projection * view * worldPosition;

    FragNormal = normal;                  // Normale du fragment
    TexCoord = texCoord;                  // Coordonnées de texture
    TexCoord.y = 1 - TexCoord.y;          // Correction de l'inversion Y pour les UV

    TextureID = texID;                    // ID de texture
}
