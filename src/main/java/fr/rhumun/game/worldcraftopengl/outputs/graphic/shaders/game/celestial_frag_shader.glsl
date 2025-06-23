#version 330 core
out vec4 FragColor;

in vec2 TexCoord;
flat in float TypeID;

uniform sampler2D sunTexture;
uniform sampler2D moonTexture;

void main() {
    if (TypeID < 0.5) {
        FragColor = texture(sunTexture, TexCoord);
        //FragColor = vec4(1.0, 0.0, 0.0, 1.0); // rouge pour debug
    } else {
        FragColor = texture(moonTexture, TexCoord);
        //FragColor = vec4(0.0, 1.0, 0.0, 1.0); // vert pour debug
    }
}
