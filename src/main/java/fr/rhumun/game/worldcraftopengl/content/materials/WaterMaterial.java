package fr.rhumun.game.worldcraftopengl.content.materials;

import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.types.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.audio.SoundPack;

public class WaterMaterial extends AbstractMaterial {
    public WaterMaterial() {
        super(Texture.WATER, 0.1f, 1f);
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
}
