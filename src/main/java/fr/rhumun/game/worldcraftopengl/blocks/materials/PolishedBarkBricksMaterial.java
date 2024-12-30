package fr.rhumun.game.worldcraftopengl.blocks.materials;

import fr.rhumun.game.worldcraftopengl.blocks.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.blocks.materials.types.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;

public class PolishedBarkBricksMaterial extends AbstractMaterial {
    public PolishedBarkBricksMaterial() {
        super(Texture.POLISHED_DARK_BRICKS);
    }

    @Override
    public Sound getSound() {
        return Sound.STONE;
    }

    @Override
    public OpacityType getOpacity() {
        return OpacityType.OPAQUE;
    }
}
