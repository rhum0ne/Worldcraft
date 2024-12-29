package fr.rhumun.game.worldcraftopengl.blocks.textures;

import lombok.Getter;

@Getter
public abstract class AnimatedTexture extends Texture {

    private final int frames;
    private final int slowness;

    public AnimatedTexture(String path, int frames, int slowness) {
        super(path);
        this.frames = frames;
        this.slowness = slowness;
    }
}
