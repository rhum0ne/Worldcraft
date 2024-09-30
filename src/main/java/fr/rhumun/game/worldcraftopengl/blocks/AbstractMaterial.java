package fr.rhumun.game.worldcraftopengl.blocks;

import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import lombok.Getter;

@Getter
public abstract class AbstractMaterial {

    final int id;
    final Texture texture;

    public AbstractMaterial(Texture texture) {
        this.id = Material.createID();
        this.texture = texture;
    }

    public abstract Sound getSound();
    public abstract boolean isOpaque();
}
