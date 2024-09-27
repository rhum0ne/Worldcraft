#version 330 core

in vec2 TexCoord;
flat in float TextureID;  // L'identifiant de la texture provenant du vertex shader
//flat in float intensity;
//flat in vec3 rgb;

uniform sampler2D textures[10];  // Tableau de sampler pour 4 textures

out vec4 FragColor;

void main()
{
    int textID = int(TextureID);

    //vec4 color = texture(textures[textID], TexCoord) * vec4(rgb.x * intensity, rgb.y * intensity, rgb.z * intensity, 1);
    vec4 color = texture(textures[textID], TexCoord);

    FragColor = color;
}
