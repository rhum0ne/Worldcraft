package fr.rhumun.game.worldcraftopengl.content.materials;

import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.types.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.audio.SoundPack;

public class LeavesMaterial extends AbstractMaterial {
    public LeavesMaterial() {
        super(Texture.LEAVES);
    }

    @Override
    public Sound getPlaceSound() {
        return SoundPack.WET_GRASS.getRandom();
    }
    @Override
    public Sound getBreakSound() {
        return SoundPack.WET_GRASS.getRandom();
    }

    @Override
    public OpacityType getOpacity() {
        return OpacityType.TRANSPARENT;
    }
}
