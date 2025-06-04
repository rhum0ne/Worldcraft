#version 330 core

layout (location = 0) in vec3 instancePosition; // Position du cube
layout (location = 1) in float texID;           // ID de la texture
layout (location = 2) in vec3 scale;            // Dimensions du cube

out vec2 TexCoord;
flat out float TextureID;
flat out vec3 FragNormal;
out vec3 FragPos;

uniform mat4 view;
uniform mat4 projection;

// Sommets unitaires du cube
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


void main() {
    int face = gl_VertexID / 6;
    vec3 localVertex = cubeVertices[gl_VertexID] * scale;
    vec3 vertexPos = instancePosition + localVertex;
    FragPos = vertexPos;

    // Compute tiling UVs from the vertex position so textures repeat on
    // faces merged by greedy meshing.
    if (face == 0 || face == 1) {
        TexCoord = (cubeVertices[gl_VertexID].xy * vec2(scale.x, scale.y));
    } else if (face == 2 || face == 3) {
        TexCoord = (vec2(cubeVertices[gl_VertexID].z, cubeVertices[gl_VertexID].y)
                    * vec2(scale.z, scale.y));
    } else {
        TexCoord = (vec2(cubeVertices[gl_VertexID].x, cubeVertices[gl_VertexID].z)
                    * vec2(scale.x, scale.z));
    }

    TextureID = texID;

    // Face normal
    if      (face == 0) FragNormal = vec3( 0,  0, -1); // Front
    else if (face == 1) FragNormal = vec3( 0,  0,  1); // Back
    else if (face == 2) FragNormal = vec3(-1,  0,  0); // Left
    else if (face == 3) FragNormal = vec3( 1,  0,  0); // Right
    else if (face == 4) FragNormal = vec3( 0,  1,  0); // Top
    else                FragNormal = vec3( 0, -1,  0); // Bottom

    gl_Position = projection * view * vec4(vertexPos, 1.0);
}
