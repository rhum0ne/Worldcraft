package fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.game;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.Shader;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderManager;

public class AnimatedEntity extends Shader {

    public AnimatedEntity() {
        super(ShaderManager.loadShader("game\\animated_entity_shader.glsl", "game\\animated_entity_frag_shader.glsl"));
    }

    @Override
    public void init() {

    }
}
