package fr.rhumun.game.worldcraftopengl.blocks.materials;

import fr.rhumun.game.worldcraftopengl.blocks.materials.types.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;

public class StoneMaterial extends AbstractMaterial {
    public StoneMaterial() {
        super(Texture.STONE);
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
