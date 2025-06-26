// Vertex Shader
#version 330 core

layout(location = 0) in vec3 inPosition;
layout(location = 1) in vec2 inTexCoord;
layout(location = 2) in float inTextureIndex;
layout(location = 3) in vec3 inNormal;
layout(location = 4) in vec4 inBoneIDs;
layout(location = 5) in vec4 inWeights;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;
uniform mat4 boneMatrices[64];

out vec2 fragTexCoord;
out float fragTextureIndex;
out vec3 fragNormal;

void main() {
    mat4 skinMatrix =
        inWeights.x * boneMatrices[int(inBoneIDs.x)] +
        inWeights.y * boneMatrices[int(inBoneIDs.y)] +
        inWeights.z * boneMatrices[int(inBoneIDs.z)] +
        inWeights.w * boneMatrices[int(inBoneIDs.w)];
    vec4 worldPos = model * skinMatrix * vec4(inPosition, 1.0);
    fragTexCoord = inTexCoord;
    fragTextureIndex = inTextureIndex;
    fragNormal = mat3(transpose(inverse(model * skinMatrix))) * inNormal;
    gl_Position = projection * view * worldPos;
}
