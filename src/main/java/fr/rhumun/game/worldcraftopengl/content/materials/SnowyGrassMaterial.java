package fr.rhumun.game.worldcraftopengl.content.materials;

import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.types.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;

public class SnowyGrassMaterial extends AbstractMaterial {
    public SnowyGrassMaterial() {
        super(Texture.SNOWY_GRASS);
        this.setBottomTexture(Texture.DIRT);
        this.setTopTexture(Texture.SNOW);
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
