package fr.rhumun.game.worldcraftopengl.content.materials;

import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.types.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;

public class DirtMaterial extends AbstractMaterial {
    public DirtMaterial() {
        super(Texture.DIRT);
    }

    @Override
    public Sound getSound() {
        return Sound.GRASS;
    }

    @Override
    public OpacityType getOpacity() {
        return OpacityType.OPAQUE;
    }
}
