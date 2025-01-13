package fr.rhumun.game.worldcraftopengl.content.materials;

import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.types.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;

public class BirchPlanksMaterial extends AbstractMaterial {
    public BirchPlanksMaterial() {
        super(Texture.BIRCH_PLANKS);
    }

    @Override
    public Sound getSound() {
        return Sound.WOOD;
    }

    @Override
    public OpacityType getOpacity() {
        return OpacityType.OPAQUE;
    }
}
