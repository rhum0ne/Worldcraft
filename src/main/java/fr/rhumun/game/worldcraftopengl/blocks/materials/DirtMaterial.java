package fr.rhumun.game.worldcraftopengl.blocks.materials;

import fr.rhumun.game.worldcraftopengl.blocks.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;

public class DirtMaterial extends AbstractMaterial {
    public DirtMaterial() {
        super("dirt.png");
    }

    @Override
    public Sound getSound() {
        return Sound.GRASS;
    }

    @Override
    public boolean isOpaque() {
        return true;
    }
}
