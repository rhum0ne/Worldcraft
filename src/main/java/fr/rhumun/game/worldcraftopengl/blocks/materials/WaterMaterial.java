package fr.rhumun.game.worldcraftopengl.blocks.materials;

import fr.rhumun.game.worldcraftopengl.blocks.materials.types.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;
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
        return false;
    }
}
