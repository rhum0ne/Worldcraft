package fr.rhumun.game.worldcraftopengl.blocks.materials;

import fr.rhumun.game.worldcraftopengl.blocks.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.blocks.materials.types.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;

public class GrassBlockMaterial extends AbstractMaterial {
    public GrassBlockMaterial() {
        super(Texture.GRASS_BLOCK);
        this.setTopTexture(Texture.GRASS_TOP);
        this.setBottomTexture(Texture.DIRT);
    }

    @Override
    public Sound getSound() {
        return Sound.GRASS;
    }

    @Override
    public OpacityType getOpacity() {
        return OpacityType.OPAQUE;
    }
}
