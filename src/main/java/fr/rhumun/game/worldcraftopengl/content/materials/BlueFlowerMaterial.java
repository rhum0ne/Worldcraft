package fr.rhumun.game.worldcraftopengl.content.materials;

import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.types.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.content.materials.types.ForcedModelMaterial;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.outputs.audio.SoundPack;

public class BlueFlowerMaterial extends AbstractMaterial implements ForcedModelMaterial {
    public BlueFlowerMaterial() {
        super(Texture.BLUE_FLOWER);
    }

    @Override
    public Sound getPlaceSound() {
        return SoundPack.GRASS.getRandom();
    }
    @Override
    public Sound getBreakSound() {
        return SoundPack.WET_GRASS.getRandom();
    }

    @Override
    public OpacityType getOpacity() {
        return OpacityType.CLOSE_TRANSPARENT;
    }

    @Override
    public Model getModel() {
        return Model.CROSS;
    }
}
