package fr.rhumun.game.worldcraftopengl.content.materials;

import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.content.materials.types.PlaceableMaterial;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.audio.SoundPack;

public class GrassBlockMaterial extends Material implements PlaceableMaterial {
    public GrassBlockMaterial() {
        super(Texture.GRASS_BLOCK);
        this.setTopTexture(Texture.GRASS_TOP);
        this.setBottomTexture(Texture.DIRT);
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
