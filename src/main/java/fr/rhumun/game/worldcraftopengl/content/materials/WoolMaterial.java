package fr.rhumun.game.worldcraftopengl.content.materials;

import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.types.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;

public class WoolMaterial extends AbstractMaterial {
    public WoolMaterial(String name) {
        super(Texture.getByName(name + "_wool"));
    }

    @Override
    public Sound getSound() {
        return null;
    }

    @Override
    public OpacityType getOpacity() {
        return OpacityType.OPAQUE;
    }
}
