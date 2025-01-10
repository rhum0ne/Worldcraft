package fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderUtils;

public class LiquidShader extends Shader{
    public LiquidShader() {
        super(ShaderUtils.loadShader("liquid.glsl", "liquid_frag.glsl"));
    }

    @Override
    public void init() {

    }
}
