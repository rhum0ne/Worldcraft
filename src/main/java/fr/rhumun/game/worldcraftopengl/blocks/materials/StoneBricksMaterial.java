package fr.rhumun.game.worldcraftopengl.blocks.materials;

import fr.rhumun.game.worldcraftopengl.blocks.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;

public class StoneBricksMaterial extends AbstractMaterial {
    public StoneBricksMaterial() {
        super("stone_brick.png");
    }

    @Override
    public Sound getSound() {
        return Sound.STONE;
    }

    @Override
    public boolean isOpaque() {
        return true;
    }
}
