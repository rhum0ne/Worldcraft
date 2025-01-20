package fr.rhumun.game.worldcraftopengl.content.materials;

import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.types.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;

public class BirchLeavesMaterial extends AbstractMaterial {
    public BirchLeavesMaterial() {
        super(Texture.BIRCH_LEAVES);
    }

    @Override
    public Sound getSound() {
        return Sound.GRASS;
    }

    @Override
    public OpacityType getOpacity() {
        return OpacityType.TRANSPARENT;
    }
}
