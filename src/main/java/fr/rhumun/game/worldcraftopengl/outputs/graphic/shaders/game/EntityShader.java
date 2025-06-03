package fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.game;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.Shader;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderManager;

public class EntityShader extends Shader {

    public EntityShader() {
        super(ShaderManager.loadShader("game\\entity_shader.glsl", "game\\entity_frag_shader.glsl"));
    }

    @Override
    public void init() {

    }
}
