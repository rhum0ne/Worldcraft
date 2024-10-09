package fr.rhumun.game.worldcraftopengl.blocks.materials;

import fr.rhumun.game.worldcraftopengl.blocks.materials.types.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.blocks.materials.types.ForcedModelMaterial;
import fr.rhumun.game.worldcraftopengl.blocks.Model;
import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;

public class RedFlowerMaterial extends AbstractMaterial implements ForcedModelMaterial {
    public RedFlowerMaterial() {
        super(Texture.RED_FLOWER);
    }

    @Override
    public Sound getSound() {
        return Sound.GRASS;
    }

    @Override
    public boolean isOpaque() {
        return false;
    }

    @Override
    public Model getModel() {
        return Model.CROSS;
    }
}
