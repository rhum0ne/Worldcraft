#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoord;
layout (location = 2) in float texID;
layout (location = 3) in vec3 normal;
layout (location = 4) in vec4 boneIndices;
layout (location = 5) in vec4 boneWeights;

out vec2 TexCoord;
flat out float TextureID;
flat out vec3 FragNormal;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

// Maximum number of bones supported by the shader
#define MAX_BONES 64
uniform mat4 boneMatrices[MAX_BONES];

void main(){
    vec4 skinnedPos = vec4(0.0);
    for(int i = 0; i < 4; i++) {
        int idx = int(boneIndices[i]);
        skinnedPos += boneMatrices[idx] * vec4(position, 1.0) * boneWeights[i];
    }

    gl_Position = projection * view * model * skinnedPos;

    FragNormal = normal;
    TexCoord = texCoord;
    TexCoord.y = 1.0 - TexCoord.y;
    TextureID = texID;
}
