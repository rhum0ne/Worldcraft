package fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.game;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.Shader;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderUtils;

public class FarShader extends Shader {
    public FarShader() {
        super(ShaderUtils.loadShader("game\\far_shader.glsl", "game\\far_frag_shader.glsl"));
    }

    @Override
    public void init() {

    }
}
