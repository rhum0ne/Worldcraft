package fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.game;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.Shader;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderManager;

public class GlobalShader extends Shader {

    public GlobalShader(){
        super(ShaderManager.loadShader("game\\vertex_shader.glsl", "game\\fragment_shader.glsl"));
    }

    @Override
    public void init() {

    }
}
