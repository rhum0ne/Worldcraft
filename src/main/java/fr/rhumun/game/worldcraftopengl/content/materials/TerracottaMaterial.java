package fr.rhumun.game.worldcraftopengl.content.materials;

import fr.rhumun.game.worldcraftopengl.content.GuiTypes;
import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.content.materials.types.PlaceableMaterial;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.audio.SoundPack;

public class TerracottaMaterial extends Material implements PlaceableMaterial {

    public TerracottaMaterial(String name) {
        super(Texture.getByName(name + "_terracotta"));
        this.addToType(GuiTypes.COLOR);
    }

    public TerracottaMaterial() {
        super(Texture.TERRACOTTA);
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
