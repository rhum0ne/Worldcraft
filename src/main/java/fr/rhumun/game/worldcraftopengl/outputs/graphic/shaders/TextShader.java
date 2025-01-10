package fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderUtils;

public class TextShader extends Shader {

    public TextShader() {
        super(ShaderUtils.loadShader("text.glsl", "text_frag.glsl"));
    }

    @Override
    public void init() {

    }
}
