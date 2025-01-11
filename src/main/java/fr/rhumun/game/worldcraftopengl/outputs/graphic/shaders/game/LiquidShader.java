package fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.game;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.Shader;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderUtils;

public class LiquidShader extends Shader {
    public LiquidShader() {
        super(ShaderUtils.loadShader("game\\liquid.glsl", "game\\liquid_frag.glsl"));
    }

    @Override
    public void init() {

    }
}
