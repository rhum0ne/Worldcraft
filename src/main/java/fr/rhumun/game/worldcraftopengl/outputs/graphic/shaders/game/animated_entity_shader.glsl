// Vertex Shader
#version 330 core

layout(location = 0) in vec3 inPosition;
layout(location = 1) in vec2 inTexCoord;
layout(location = 2) in float inTextureIndex;
layout(location = 3) in vec3 inNormal;
layout(location = 4) in int inBoneID;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 boneMatrices[64];

out vec2 fragTexCoord;
out float fragTextureIndex;
out vec3 fragNormal;

void main() {
    mat4 boneTransform = boneMatrices[inBoneID];
    vec4 worldPos = boneTransform * vec4(inPosition, 1.0);
    fragTexCoord = inTexCoord;
    fragTextureIndex = inTextureIndex;
    fragNormal = mat3(transpose(inverse(boneTransform))) * inNormal;
    vec4 skinnedPos = boneMatrices[inBoneID] * vec4(inPosition, 1.0);
    skinnedPos.xyz *= 0.05; // Réduit l’échelle
    gl_Position = projection * view * worldPos * skinnedPos;
}