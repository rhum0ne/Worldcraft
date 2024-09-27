package fr.rhumun.game.worldcraftopengl.blocks;

import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import lombok.Getter;

@Getter
public abstract class AbstractMaterial {

    final int id;
    final String texturePath;

    public AbstractMaterial(String texturePath) {
        this.id = Material.createID();
        this.texturePath = texturePath;
    }

    public abstract Sound getSound();
    public abstract boolean isOpaque();
}
