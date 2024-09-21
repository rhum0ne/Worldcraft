#version 330 core

in vec2 TexCoord;
flat in float TextureID;  // L'identifiant de la texture provenant du vertex shader

flat int textID;
uniform sampler2D textures[9];  // Tableau de sampler pour 4 textures

out vec4 FragColor;

void main()
{
    vec4 sampledColor;
    textID = int(TextureID);

    sampledColor = texture(textures[textID], TexCoord);

    //if(textID==textures.length) sampledColor = texture(textures[textures.length-1], TexCoord);
    FragColor = sampledColor;
}
