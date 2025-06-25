package fr.rhumun.game.worldcraftopengl.content.materials.blocks;

import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.Material;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.PlaceableMaterial;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.audio.SoundPack;
import fr.rhumun.game.worldcraftopengl.content.GuiTypes;

public class SnowyGrassMaterial extends Material implements PlaceableMaterial {
    public SnowyGrassMaterial() {
        super(Texture.SNOWY_GRASS);
        this.addToType(GuiTypes.NATURAL);
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
