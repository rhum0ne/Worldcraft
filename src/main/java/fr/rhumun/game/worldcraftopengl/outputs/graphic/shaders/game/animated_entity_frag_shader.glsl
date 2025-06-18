// Fragment Shader
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


in vec2 fragTexCoord;
in float fragTextureIndex;
in vec3 fragNormal;

out vec4 fragColor;

uniform sampler2DArray entitiesTextures;

void main() {
    fragColor = vec4(1.0, 0.5, 0.2, 1.0); // Placeholder
}