package fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.ui;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.Shader;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderManager;

public class HUDShader extends Shader {

    public HUDShader() {
        super(ShaderManager.loadShader("ui\\hud_vertex_shader.glsl", "ui\\hud_fragment_shader.glsl"));
    }

    @Override
    public void init() {

    }
}
