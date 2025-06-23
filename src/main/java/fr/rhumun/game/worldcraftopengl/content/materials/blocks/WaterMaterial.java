package fr.rhumun.game.worldcraftopengl.content.materials.blocks;

import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.Material;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.PlaceableMaterial;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.content.GuiTypes;

public class WaterMaterial extends Material implements PlaceableMaterial {
    public WaterMaterial() {
        super(Texture.WATER, 0.1f, 1f);
        this.addToType(GuiTypes.NATURAL);
    }

    @Override
    public Sound getPlaceSound() {
        return null;
    }
    @Override
    public Sound getBreakSound() {
        return null;
    }

    @Override
    public OpacityType getOpacity() {
        return OpacityType.LIQUID;
    }

    @Override
    public boolean showInCreativeInventory() {
        return false;
    }
}
