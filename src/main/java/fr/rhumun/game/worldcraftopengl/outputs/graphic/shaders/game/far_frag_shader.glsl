#version 330 core

struct DirLight {
    vec3 direction;
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

uniform DirLight dirLight;
uniform vec3 viewPos;
uniform sampler2DArray textures;

in vec2 TexCoord;
flat in float TextureID;
flat in vec3 FragNormal;
in vec3 FragPos;

out vec4 FragColor;

vec3 CalcDirLight(DirLight light, vec3 normal, vec3 viewDir)
{
    vec3 lightDir = normalize(-light.direction);
    float diff = max(dot(normal, lightDir), 0.0);

    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 16.0);

    vec3 texColor = vec3(texture(textures, vec3(TexCoord, TextureID)));

    vec3 ambient  = light.ambient  * texColor;
    vec3 diffuse  = light.diffuse  * diff * texColor;
    vec3 specular = light.specular * spec * texColor;

    return ambient + diffuse + specular;
}

void main() {
    vec4 tex = texture(textures, vec3(TexCoord, TextureID));
    if (tex.a < 0.01) discard;

    vec3 normal = normalize(FragNormal);
    vec3 viewDir = normalize(viewPos - FragPos);
    vec3 color = CalcDirLight(dirLight, normal, viewDir);

    FragColor = vec4(color, tex.a);
}
