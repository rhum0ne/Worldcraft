package fr.rhumun.game.worldcraftopengl.blocks.materials;

import fr.rhumun.game.worldcraftopengl.blocks.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.blocks.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;

public class GrassBlockMaterial extends AbstractMaterial {
    public GrassBlockMaterial() {
        super(Texture.GRASS_BLOCK);
    }

    @Override
    public Sound getSound() {
        return Sound.GRASS;
    }

    @Override
    public boolean isOpaque() {
        return true;
    }
}
