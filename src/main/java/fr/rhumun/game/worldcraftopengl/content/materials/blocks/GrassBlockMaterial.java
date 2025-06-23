package fr.rhumun.game.worldcraftopengl.content.materials.blocks;

import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.ToolType;
import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.Material;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.PlaceableMaterial;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.audio.SoundPack;
import fr.rhumun.game.worldcraftopengl.content.GuiTypes;

public class GrassBlockMaterial extends Material implements PlaceableMaterial {
    public GrassBlockMaterial() {
        super(Texture.GRASS_BLOCK);
        this.addToType(GuiTypes.NATURAL);
        this.setTopTexture(Texture.GRASS_TOP);
        this.setBottomTexture(Texture.DIRT);
        this.setToolType(ToolType.DIRT);
    }

    @Override
    public Sound getPlaceSound() {
        return SoundPack.GRASS.getRandom();
    }
    @Override
    public Sound getBreakSound() {
        return SoundPack.GRASS.getRandom();
    }

    public Texture getTexture() {return this.getTopTexture(); }

    @Override
    public OpacityType getOpacity() {
        return OpacityType.OPAQUE;
    }
}
