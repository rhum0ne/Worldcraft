package fr.rhumun.game.worldcraftopengl.blocks.materials;

import fr.rhumun.game.worldcraftopengl.blocks.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.blocks.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;

public class WaterMaterial extends AbstractMaterial {
    public WaterMaterial() {
        super(Texture.WATER);
    }

    @Override
    public Sound getSound() {
        return null;
    }

    @Override
    public boolean isOpaque() {
        return true;
    }
}
