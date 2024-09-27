package fr.rhumun.game.worldcraftopengl.blocks.materials;

import fr.rhumun.game.worldcraftopengl.blocks.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;

public class OakLogMaterial extends AbstractMaterial {
    public OakLogMaterial() {
        super("oak_log.png");
    }

    @Override
    public Sound getSound() {
        return Sound.WOOD;
    }

    @Override
    public boolean isOpaque() {
        return true;
    }
}
