package fr.rhumun.game.worldcraftopengl.content.materials;

import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.content.materials.types.PlaceableMaterial;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.audio.SoundPack;
import fr.rhumun.game.worldcraftopengl.content.GuiTypes;

public class AcaciaLeavesMaterial extends Material implements PlaceableMaterial {
    public AcaciaLeavesMaterial() {
        super(Texture.ACACIA_LEAVES);
        this.addToType(GuiTypes.NATURAL);
    }

    @Override
    public Sound getPlaceSound() {
        return SoundPack.WET_GRASS.getRandom();
    }
    @Override
    public Sound getBreakSound() {
        return SoundPack.WET_GRASS.getRandom();
    }

    @Override
    public OpacityType getOpacity() {
        return OpacityType.TRANSPARENT;
    }
}
