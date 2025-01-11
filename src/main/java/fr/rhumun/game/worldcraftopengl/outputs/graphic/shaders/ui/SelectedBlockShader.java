package fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.ui;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.Shader;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderUtils;

public class SelectedBlockShader extends Shader {

    public SelectedBlockShader() {
        super(ShaderUtils.loadShader("ui\\selectedBlock.glsl", "ui\\selectedBlock_frag.glsl"));
    }

    @Override
    public void init() {

    }
}
