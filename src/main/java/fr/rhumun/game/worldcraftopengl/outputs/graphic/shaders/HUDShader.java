package fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderUtils;

public class HUDShader extends Shader {

    public HUDShader() {
        super(ShaderUtils.loadShader("hud_vertex_shader.glsl", "hud_fragment_shader.glsl"));
    }

    @Override
    public void init() {

    }
}
