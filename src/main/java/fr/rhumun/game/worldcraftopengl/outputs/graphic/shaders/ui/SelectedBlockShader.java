package fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.ui;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.Shader;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderManager;

public class SelectedBlockShader extends Shader {

    public SelectedBlockShader() {
        super(ShaderManager.loadShader("ui\\selectedBlock.glsl", "ui\\selectedBlock_frag.glsl"));
    }

    @Override
    public void init() {

    }
}
