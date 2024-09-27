package fr.rhumun.game.worldcraftopengl.blocks.materials;

import fr.rhumun.game.worldcraftopengl.blocks.AbstractMaterial;
import fr.rhumun.game.worldcraftopengl.blocks.LightSource;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;

public class LampMaterial extends AbstractMaterial implements LightSource {
    public LampMaterial() {
        super("lamp.png");
    }

    @Override
    public Sound getSound() {
        return Sound.STONE;
    }

    @Override
    public boolean isOpaque() {
        return true;
    }

    @Override
    public short getRed() {
        return 500;
    }

    @Override
    public short getGreen() {
        return 0;
    }

    @Override
    public short getBlue() {
        return 500;
    }
}
