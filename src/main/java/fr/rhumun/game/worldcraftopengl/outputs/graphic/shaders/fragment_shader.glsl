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

#define MAX_POINT_LIGHTS 100  // Le nombre maximum de lumières

uniform PointLight pointLights[MAX_POINT_LIGHTS];
uniform int numPointLights;  // Le nombre réel de lumières actives
uniform vec3 viewPos;

in vec2 TexCoord;
flat in float TextureID;
flat in vec3 FragNormal;
in vec3 FragPos;  // Position interpolée du fragment en espace monde
out vec4 FragPosLightSpace;

uniform sampler2D textures[32];  // Tableau de textures
uniform sampler2D shadowMap;  // Pour les ombres directionnelles
uniform samplerCube depthMap; // Pour les ombres des lumières ponctuelles
uniform float far_plane;      // Distance maximum de la lumière ponctuelle


out vec4 FragColor;

float ShadowCalculation(vec4 fragPosLightSpace)
{
    // perform perspective divide
    vec3 projCoords = fragPosLightSpace.xyz / fragPosLightSpace.w;
    // transform to [0,1] range
    projCoords = projCoords * 0.5 + 0.5;
    // get closest depth value from light's perspective (using [0,1] range fragPosLight as coords)
    float closestDepth = texture(shadowMap, projCoords.xy).r;
    // get depth of current fragment from light's perspective
    float currentDepth = projCoords.z;
    // check whether current frag pos is in shadow
    float shadow = currentDepth > closestDepth  ? 1.0 : 0.0;

    return shadow;
}

float PointShadowCalculation(PointLight light, vec3 fragPos)
{
    // get vector between fragment position and light position
    vec3 fragToLight = fragPos - light.position;
    // use the light to fragment vector to sample from the depth map
    float closestDepth = texture(depthMap, fragToLight).r;
    // it is currently in linear range between [0,1]. Re-transform back to original value
    closestDepth *= far_plane;
    // now get current linear depth as the length between the fragment and light position
    float currentDepth = length(fragToLight);
    // now test for shadows
    float bias = 0.05;
    float shadow = currentDepth -  bias > closestDepth ? 1.0 : 0.0;

    return shadow;
}

vec3 CalcDirLight(DirLight light, vec3 normal, vec3 viewDir)
{
    vec3 lightDir = normalize(-light.direction);
    // diffuse shading
    float diff = max(dot(normal, lightDir), 0.0);
    // specular shading
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32.0);
    // combine results
    vec3 ambient  = light.ambient  * vec3(texture(textures[int(TextureID)], TexCoord));
    vec3 diffuse  = light.diffuse  * diff * vec3(texture(textures[int(TextureID)], TexCoord));
    vec3 specular = light.specular * spec * vec3(texture(textures[int(TextureID)], TexCoord));

    //float shadow = ShadowCalculation(FragPosLightSpace);

    //return (ambient + (1.0 - shadow) * (diffuse + specular));
    return ambient + diffuse + specular;
}

vec3 CalcPointLight(PointLight light, vec3 normal, vec3 fragPos, vec3 viewDir)
{
    vec3 lightDir = normalize(light.position - fragPos);

    // Shading diffus
    float diff = max(dot(normal, lightDir), 0.0);

    // Shading spéculaire
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32.0);  // Brillance fixée à 32

    // Atténuation
    float distance = length(light.position - fragPos);
    float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));

    // Composantes ambiante, diffuse, spéculaire
    vec3 ambient = light.ambient * vec3(texture(textures[int(TextureID)], TexCoord));
    vec3 diffuse = light.diffuse * diff * vec3(texture(textures[int(TextureID)], TexCoord));
    vec3 specular = light.specular * spec * vec3(1.0);  // Si tu as une map spéculaire, tu pourrais la remplacer ici

    ambient *= attenuation;
    diffuse *= attenuation;
    specular *= attenuation;

    return (ambient + diffuse + specular);

    //float shadow = PointShadowCalculation(light, fragPos);

    //return (ambient + (1.0 - shadow) * (diffuse + specular));
}

void main()
{
    vec4 textureColor = texture(textures[int(TextureID)], TexCoord);
    vec3 norm = normalize(FragNormal);  // Normalisation des normales
    vec3 viewDir = normalize(viewPos - FragPos);  // Direction de la caméra en espace monde

    vec3 result = CalcDirLight(dirLight, norm, viewDir);

    for(int i = 0; i < numPointLights; i++)
    {
        result += CalcPointLight(pointLights[i], norm, FragPos, viewDir);  // On utilise FragPos ici
    }

    FragColor = vec4(result, textureColor.a);
}