package fr.rhumun.game.worldcraftopengl.content.materials.blocks;

import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.ToolType;
import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.Material;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.PlaceableMaterial;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.audio.SoundPack;
import fr.rhumun.game.worldcraftopengl.content.GuiTypes;

public class DirtMaterial extends Material implements PlaceableMaterial {
    public DirtMaterial() {
        super(Texture.DIRT);
        this.addToType(GuiTypes.NATURAL);
        this.setToolType(ToolType.DIRT);
    }

    @Override
    public Sound getPlaceSound() {
        return SoundPack.GRAVEL.getRandom();
    }
    @Override
    public Sound getBreakSound() {
        return SoundPack.GRAVEL.getRandom();
    }

    @Override
    public OpacityType getOpacity() {
        return OpacityType.OPAQUE;
    }
}
