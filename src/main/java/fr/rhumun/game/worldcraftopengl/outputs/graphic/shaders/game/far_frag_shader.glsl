#version 330 core

in vec2 TexCoord;
flat in float TextureID;

uniform sampler2DArray textures;

out vec4 FragColor;

void main() {
    FragColor = texture(textures, vec3(TexCoord, TextureID));
}
