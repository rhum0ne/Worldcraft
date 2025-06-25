package fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.game;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.Shader;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderManager;

public class CelestialShader extends Shader {

    public CelestialShader() {
        super(ShaderManager.loadShader("game\\celestial_shader.glsl", "game\\celestial_frag_shader.glsl"));
    }

    @Override
    public void init() {

    }
}
