package fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderUtils;

public class GlobalShader extends Shader {

    public GlobalShader(){
        super(ShaderUtils.loadShader("vertex_shader.glsl", "fragment_shader.glsl"));
    }

    @Override
    public void init() {

    }
}
