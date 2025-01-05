package fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders;

public class TextShader extends Shader {

    public TextShader() {
        super(ShaderUtils.loadShader("text.glsl", "text_frag.glsl"));
    }

    @Override
    public void init() {

    }
}
