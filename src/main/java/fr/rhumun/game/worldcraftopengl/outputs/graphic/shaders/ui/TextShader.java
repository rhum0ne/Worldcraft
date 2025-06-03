package fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.ui;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.Shader;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderManager;

public class TextShader extends Shader {

    public TextShader() {
        super(ShaderManager.loadShader("ui\\text.glsl", "ui\\text_frag.glsl"));
    }

    @Override
    public void init() {

    }
}
