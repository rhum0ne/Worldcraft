package fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.game;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.Shader;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderManager;

/**
 * Simple shader dedicated to rendering clouds as plain white quads.
 */
public class CloudShader extends Shader {
    public CloudShader() {
        super(ShaderManager.loadShader("game\\cloud_vertex.glsl", "game\\cloud_fragment.glsl"));
    }

    @Override
    public void init() {
        // no specific initialization
    }
}
