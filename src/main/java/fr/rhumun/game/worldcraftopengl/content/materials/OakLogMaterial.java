package fr.rhumun.game.worldcraftopengl.content.materials;

import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.types.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;

public class OakLogMaterial extends AbstractMaterial {
    public OakLogMaterial() {
        super(Texture.OAK_LOG);
        this.setTopAndBottomTexture(Texture.OAK_LOG_TOP);
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
