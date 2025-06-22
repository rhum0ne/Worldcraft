package fr.rhumun.game.worldcraftopengl.content.materials;

import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.types.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.content.materials.types.ForcedModelMaterial;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.audio.SoundPack;

public class GlassMaterial extends AbstractMaterial implements PlaceableMaterial, ForcedModelMaterial {
    public GlassMaterial() {
        super(Texture.GLASS);
    }

    @Override
    public Sound getPlaceSound() {
        return SoundPack.GLASS.getRandom();
    }
    @Override
    public Sound getBreakSound() {
        return SoundPack.GLASS_BREAK.getRandom();
    }

    @Override
    public OpacityType getOpacity() {
        return OpacityType.TRANSPARENT;
    }

    @Override
    public Model getModel() {
        return Model.BLOCK;
    }
}
