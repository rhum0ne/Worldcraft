// Vertex Shader
#version 330 core

layout(location = 0) in vec3 inPosition;
layout(location = 1) in vec2 inTexCoord;
layout(location = 2) in float inTextureIndex;
layout(location = 3) in vec3 inNormal;
layout(location = 4) in float inBoneID;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 boneMatrices[64];

out vec2 TexCoord;
flat out float TextureID;
flat out vec3 FragNormal;
out vec3 FragPos;

void main() {
    int boneIndex = int(inBoneID);
    mat4 boneTransform = boneMatrices[boneIndex];
    vec4 localPos = vec4(inPosition, 1.0);
    vec4 worldPos = boneTransform * localPos;
    FragPos = worldPos.xyz;
    TexCoord = inTexCoord;
    TexCoord.y = 1 - TexCoord.y;
    TextureID = inTextureIndex;
    FragNormal = mat3(transpose(inverse(boneTransform))) * inNormal;
    gl_Position = projection * view * worldPos;
}
