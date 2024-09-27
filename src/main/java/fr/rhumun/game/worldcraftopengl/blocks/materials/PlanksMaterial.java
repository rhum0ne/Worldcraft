package fr.rhumun.game.worldcraftopengl.blocks.materials;

import fr.rhumun.game.worldcraftopengl.blocks.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;

public class PlanksMaterial extends AbstractMaterial {
    public PlanksMaterial() {
        super("planks.png");
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
