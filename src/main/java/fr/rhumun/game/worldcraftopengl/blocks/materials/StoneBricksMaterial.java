package fr.rhumun.game.worldcraftopengl.blocks.materials;

import fr.rhumun.game.worldcraftopengl.blocks.materials.types.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;

public class StoneBricksMaterial extends AbstractMaterial {
    public StoneBricksMaterial() {
        super(Texture.STONE_BRICKS);
    }

    @Override
    public Sound getSound() {
        return Sound.STONE;
    }

    @Override
    public boolean isOpaque() {
        return true;
    }
}
