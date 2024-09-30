package fr.rhumun.game.worldcraftopengl.blocks.materials;

import fr.rhumun.game.worldcraftopengl.blocks.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.blocks.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;

public class LeavesMaterial extends AbstractMaterial {
    public LeavesMaterial() {
        super(Texture.LEAVES);
    }

    @Override
    public Sound getSound() {
        return Sound.GRASS;
    }

    @Override
    public boolean isOpaque() {
        return false;
    }
}