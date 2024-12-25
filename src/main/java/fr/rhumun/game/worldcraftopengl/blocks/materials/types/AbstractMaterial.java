package fr.rhumun.game.worldcraftopengl.blocks.materials.types;

import fr.rhumun.game.worldcraftopengl.blocks.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import lombok.Getter;

@Getter
public abstract class AbstractMaterial {

    final int id;
    final Texture texture;
    private final float friction;

    public AbstractMaterial(Texture texture) {this(texture, 0.1f);}

    public AbstractMaterial(Texture texture, float friction) {
        this.id = Material.createID();
        this.texture = texture;
        this.friction = friction;
    }

    public abstract Sound getSound();
    public abstract OpacityType getOpacity();
}
