package fr.rhumun.game.worldcraftopengl.blocks.materials;

import fr.rhumun.game.worldcraftopengl.blocks.materials.types.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;

public class PlanksMaterial extends AbstractMaterial {
    public PlanksMaterial() {
        super(Texture.PLANKS);
    }

    @Override
    public Sound getSound() {
        return Sound.WOOD;
    }

    @Override
    public boolean isOpaque() {
        return true;
    }
}
