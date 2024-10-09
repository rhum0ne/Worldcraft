package fr.rhumun.game.worldcraftopengl.blocks.materials;

import fr.rhumun.game.worldcraftopengl.blocks.materials.types.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;

public class SaplingMaterial extends AbstractMaterial {
    public SaplingMaterial() {
        super(Texture.SAPLING);
    }

    @Override
    public Sound getSound() {
        return Sound.GRASS;
    }

    @Override
    public boolean isOpaque() {
        return false;
    }
}
