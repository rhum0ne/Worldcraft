package fr.rhumun.game.worldcraftopengl.content.materials;

import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.types.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;

public class WaterMaterial extends AbstractMaterial {
    public WaterMaterial() {
        super(Texture.WATER);
    }

    @Override
    public Sound getSound() {
        return null;
    }

    @Override
    public OpacityType getOpacity() {
        return OpacityType.LIQUID;
    }
}
