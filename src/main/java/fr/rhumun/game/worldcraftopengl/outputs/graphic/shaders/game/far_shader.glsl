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

const vec2 uvMap[6] = vec2[](
vec2(0.0, 0.0), vec2(1.0, 1.0), vec2(1.0, 0.0),
vec2(0.0, 0.0), vec2(0.0, 1.0), vec2(1.0, 1.0)
);

void main() {
    int face = gl_VertexID / 6;
    vec2 baseUV = uvMap[gl_VertexID % 6];

    vec3 localVertex = cubeVertices[gl_VertexID] * scale;
    vec3 vertexPos = instancePosition + localVertex;
    FragPos = vertexPos;

    // UV scaling by face
    if (face == 0 || face == 1)       TexCoord = baseUV * vec2(scale.x, scale.y); // Front/Back
    else if (face == 2 || face == 3)  TexCoord = baseUV * vec2(scale.z, scale.y); // Left/Right
    else                              TexCoord = baseUV * vec2(scale.x, scale.z); // Top/Bottom

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
