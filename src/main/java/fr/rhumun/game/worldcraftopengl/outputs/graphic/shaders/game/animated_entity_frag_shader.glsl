#version 330 core

struct DirLight {
    vec3 direction;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};
uniform DirLight dirLight;

struct PointLight {
    vec3 position;
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;

    float constant;
    float linear;
    float quadratic;
};

#define MAX_POINT_LIGHTS 100

uniform PointLight pointLights[MAX_POINT_LIGHTS];
uniform int numPointLights;
uniform vec3 viewPos;

in vec2 TexCoord;
flat in float TextureID;
flat in vec3 FragNormal;
in vec3 FragPos;
out vec4 FragPosLightSpace;

uniform sampler2DArray entitiesTextures;
uniform sampler2DArray textures;
uniform int texturesNumber;
uniform sampler2D shadowMap;
uniform samplerCube depthMap;
uniform float far_plane;

out vec4 FragColor;

float ShadowCalculation(vec4 fragPosLightSpace)
{
    vec3 projCoords = fragPosLightSpace.xyz / fragPosLightSpace.w;
    projCoords = projCoords * 0.5 + 0.5;
    float closestDepth = texture(shadowMap, projCoords.xy).r;
    float currentDepth = projCoords.z;
    float shadow = currentDepth > closestDepth  ? 1.0 : 0.0;

    return shadow;
}

float PointShadowCalculation(PointLight light, vec3 fragPos)
{
    vec3 fragToLight = fragPos - light.position;
    float closestDepth = texture(depthMap, fragToLight).r;
    closestDepth *= far_plane;
    float currentDepth = length(fragToLight);
    float bias = 0.05;
    float shadow = currentDepth -  bias > closestDepth ? 1.0 : 0.0;

    return shadow;
}

vec3 CalcDirLight(DirLight light, vec3 normal, vec3 viewDir)
{
    vec3 lightDir = normalize(-light.direction);
    float diff = max(dot(normal, lightDir), 0.0);
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32.0);
    vec3 ambient  = light.ambient  * vec3(texture(textures, vec3(TexCoord, TextureID)));
    vec3 diffuse  = light.diffuse  * diff * vec3(texture(textures, vec3(TexCoord, TextureID)));
    vec3 specular = light.specular * spec * vec3(texture(textures, vec3(TexCoord, TextureID)));

    return ambient + diffuse + specular;
}

vec3 CalcPointLight(PointLight light, vec3 normal, vec3 fragPos, vec3 viewDir)
{
    vec3 lightDir = normalize(light.position - fragPos);
    float diff = max(dot(normal, lightDir), 0.0);
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32.0);
    float distance = length(light.position - fragPos);
    float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));
    vec3 ambient = light.ambient * vec3(texture(textures, vec3(TexCoord, TextureID)));
    vec3 diffuse = light.diffuse * diff * vec3(texture(textures, vec3(TexCoord, TextureID)));
    vec3 specular = light.specular * spec * vec3(1.0);

    ambient *= attenuation;
    diffuse *= attenuation;
    specular *= attenuation;

    float shadow = PointShadowCalculation(light, fragPos);

    return (ambient + (1.0 - shadow) * (diffuse + specular));
}

void main(){
    vec4 textureColor;
    if(TextureID >= texturesNumber){
        textureColor = texture(entitiesTextures, vec3(TexCoord, TextureID-texturesNumber));
    }else{
        textureColor = texture(textures, vec3(TexCoord, TextureID));
    }
    vec3 norm = normalize(FragNormal);
    vec3 viewDir = normalize(viewPos - FragPos);

    vec3 result = CalcDirLight(dirLight, norm, viewDir);

    for(int i = 0; i < numPointLights; i++)
    {
        result += CalcPointLight(pointLights[i], norm, FragPos, viewDir);
    }
    FragColor = vec4(textureColor.rgb * result, textureColor.a);
}

