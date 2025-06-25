package fr.rhumun.game.worldcraftopengl.content.materials.blocks;

import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.Material;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.PlaceableMaterial;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.audio.SoundPack;
import fr.rhumun.game.worldcraftopengl.content.GuiTypes;

public class SnowMaterial extends Material implements PlaceableMaterial {
    public SnowMaterial() {
        super(Texture.SNOW);
        this.addToType(GuiTypes.NATURAL);
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
