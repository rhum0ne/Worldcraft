#version 330 core

layout (location = 0) in vec3 position;   // Position du vertex dans l'espace objet
layout (location = 1) in vec2 texCoord;    // Coordonnées de texture
layout (location = 2) in float texID;      // ID de texture
layout (location = 3) in vec3 normal;

out vec2 TexCoord;                          // Sortie des coordonnées de texture pour le fragment shader
flat out float TextureID;                   // Sortie de l'ID de texture
flat out vec3 FragNormal;
out vec3 FragPos;  // Position du fragment en espace monde
out vec4 FragPosLightSpace;

uniform mat4 model;                         // Matrice de modèle
uniform mat4 view;                          // Matrice de vue
uniform mat4 projection;                     // Matrice de projection

uniform vec3 lightPosition;                 // Position de la lumière dans l'espace monde
uniform mat4 lightSpaceMatrix;

void main()
{
    // Convertir la position du vertex de l'espace objet à l'espace monde
    vec4 worldPosition = model * vec4(position, 1.0);
    FragPos = worldPosition.xyz;  // On passe la position en espace monde au fragment shader

    // Convertir la position du vertex de l'espace monde à l'espace clip
    gl_Position = projection * view * worldPosition;

    FragNormal = normal;
    TexCoord = texCoord;                    // Passer les coordonnées de texture au fragment shader
    TexCoord.y = 1 - TexCoord.y;

    TextureID = texID;                      // Passer l'ID de texture au fragment shader
    //FragPosLightSpace = view * projection * vec4(FragPos, 1.0);
}
