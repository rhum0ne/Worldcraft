package fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderUtils;

import java.io.IOException;

public class GlobalShader extends Shader {

    public GlobalShader() throws IOException {
        super(ShaderUtils.loadShader("vertex_shader.glsl", "fragment_shader.glsl"));
    }

    @Override
    public void init() {

    }
}
