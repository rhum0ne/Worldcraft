package fr.rhumun.game.worldcraftopengl.blocks.materials;

import fr.rhumun.game.worldcraftopengl.blocks.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.blocks.materials.types.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.blocks.materials.types.ForcedModelMaterial;
import fr.rhumun.game.worldcraftopengl.blocks.Model;
import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;

public class GrassMaterial extends AbstractMaterial implements ForcedModelMaterial {
    public GrassMaterial() {
        super(Texture.GRASS);
    }

    @Override
    public Sound getSound() {
        return Sound.GRASS;
    }

    @Override
    public OpacityType getOpacity() {
        return OpacityType.TRANSPARENT;
    }

    @Override
    public Model getModel() {
        return Model.CROSS;
    }
}
