package fr.rhumun.game.worldcraftopengl.content.materials;

import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.types.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.audio.SoundPack;

public class SnowyGrassMaterial extends AbstractMaterial {
    public SnowyGrassMaterial() {
        super(Texture.SNOWY_GRASS);
        this.setBottomTexture(Texture.DIRT);
        this.setTopTexture(Texture.SNOW);
    }

    @Override
    public Sound getPlaceSound() {
        return SoundPack.SNOW.getRandom();
    }
    @Override
    public Sound getBreakSound() {
        return SoundPack.SNOW.getRandom();
    }

    @Override
    public OpacityType getOpacity() {
        return OpacityType.OPAQUE;
    }
}
