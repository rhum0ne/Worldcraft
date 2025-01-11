#version 330
layout (location = 0) in vec3 position;   // Position du vertex dans l'espace objet

uniform mat4 model;                         // Matrice de modèle
uniform mat4 view;                          // Matrice de vue
uniform mat4 projection;                     // Matrice de projection

void main() {
    // Convertir la position du vertex de l'espace monde à l'espace clip
    gl_Position = projection * view * model * vec4(position, 1.0);
}