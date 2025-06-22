package fr.rhumun.game.worldcraftopengl.content.materials;

import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.types.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.content.materials.types.PlaceableMaterial;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.audio.SoundPack;

public class SandMaterial extends AbstractMaterial  implements PlaceableMaterial {
    public SandMaterial() {
        super(Texture.SAND);
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
