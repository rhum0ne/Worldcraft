#version 330 core

layout (location = 0) in vec3 instancePosition; // Coordonnées du bloc
layout (location = 1) in float texID;           // ID de la texture

out vec2 TexCoord;
flat out float TextureID;

uniform mat4 view;
uniform mat4 projection;

// Tableau des positions relatives d’un cube
const vec3 cubeVertices[36] = vec3[](
// Face avant
vec3(0, 0, 0), vec3(1, 1, 0), vec3(1, 0, 0),
vec3(0, 0, 0), vec3(0, 1, 0), vec3(1, 1, 0),

// Face arrière
vec3(1, 0, 1), vec3(1, 1, 1), vec3(0, 1, 1),
vec3(0, 0, 1), vec3(1, 0, 1), vec3(0, 1, 1),

// Face gauche
vec3(0, 0, 1), vec3(0, 1, 0), vec3(0, 0, 0),
vec3(0, 0, 1), vec3(0, 1, 1), vec3(0, 1, 0),

// Face droite
vec3(1, 0, 0), vec3(1, 1, 0), vec3(1, 0, 1),
vec3(1, 0, 1), vec3(1, 1, 0), vec3(1, 1, 1),

// Face dessus
vec3(0, 1, 0), vec3(0, 1, 1), vec3(1, 1, 0),
vec3(1, 1, 0), vec3(0, 1, 1), vec3(1, 1, 1),

// Face dessous
vec3(0, 0, 0), vec3(1, 0, 0), vec3(0, 0, 1),
vec3(0, 0, 1), vec3(1, 0, 0), vec3(1, 0, 1)
);

// Simple UVs
const vec2 uvMap[6] = vec2[](
vec2(0.0, 0.0), vec2(1.0, 1.0), vec2(1.0, 0.0),
vec2(0.0, 0.0), vec2(0.0, 1.0), vec2(1.0, 1.0)
);

void main() {
    vec3 vertexPos = cubeVertices[gl_VertexID] + instancePosition;
    gl_Position = projection * view * vec4(vertexPos, 1.0);

    TexCoord = uvMap[gl_VertexID % 6];
    TextureID = texID;
}
