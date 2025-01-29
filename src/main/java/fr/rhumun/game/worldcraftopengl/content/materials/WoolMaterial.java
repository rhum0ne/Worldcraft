package fr.rhumun.game.worldcraftopengl.content.materials;

import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.types.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.audio.SoundPack;

public class WoolMaterial extends AbstractMaterial {
    public WoolMaterial(String name) {
        super(Texture.getByName(name + "_wool"));
    }

    @Override
    public Sound getPlaceSound() {
        return SoundPack.CLOTH.getRandom();
    }
    @Override
    public Sound getBreakSound() {
        return SoundPack.CLOTH.getRandom();
    }

    @Override
    public OpacityType getOpacity() {
        return OpacityType.OPAQUE;
    }
}
