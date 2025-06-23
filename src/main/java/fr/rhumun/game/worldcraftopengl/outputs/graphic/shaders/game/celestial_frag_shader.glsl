#version 330 core
out vec4 FragColor;

in vec2 TexCoord;
flat in float TypeID;

uniform sampler2D sunTexture;
uniform sampler2D moonTexture;

void main() {
    if(TypeID < 0.5)
        FragColor = texture(sunTexture, TexCoord);
    else
        FragColor = texture(moonTexture, TexCoord);
}
