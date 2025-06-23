package fr.rhumun.game.worldcraftopengl.content.materials.blocks;

import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.Material;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.PlaceableMaterial;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.audio.SoundPack;
import fr.rhumun.game.worldcraftopengl.content.GuiTypes;

public class SandMaterial extends Material implements PlaceableMaterial {
    public SandMaterial() {
        super(Texture.SAND);
        this.addToType(GuiTypes.NATURAL);
    }

    @Override
    public Sound getPlaceSound() {
        return SoundPack.SAND.getRandom();
    }
    @Override
    public Sound getBreakSound() {
        return SoundPack.SAND.getRandom();
    }

    @Override
    public OpacityType getOpacity() {
        return OpacityType.OPAQUE;
    }
}
