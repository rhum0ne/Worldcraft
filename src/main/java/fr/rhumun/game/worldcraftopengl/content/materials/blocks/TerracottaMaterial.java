package fr.rhumun.game.worldcraftopengl.content.materials.blocks;

import fr.rhumun.game.worldcraftopengl.content.GuiTypes;
import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.Material;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.PlaceableMaterial;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.audio.SoundPack;

public class TerracottaMaterial extends Material implements PlaceableMaterial {

    public TerracottaMaterial(String name) {
        super(Texture.getByName(name + "_terracotta"));
        this.addToType(GuiTypes.COLOR);
        this.setDurability(10f);
    }

    public TerracottaMaterial() {
        super(Texture.TERRACOTTA);
        this.setDurability(10f);
    }

    @Override
    public Sound getPlaceSound() {
        return SoundPack.STONE.getRandom();
    }
    @Override
    public Sound getBreakSound() {
        return SoundPack.STONE_DIG.getRandom();
    }

    @Override
    public OpacityType getOpacity() {
        return OpacityType.OPAQUE;
    }
}
