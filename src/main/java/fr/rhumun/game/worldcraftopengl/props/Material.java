package fr.rhumun.game.worldcraftopengl.props;

import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import lombok.Getter;

@Getter
public enum Material {
    COBBLE(1, "cobble.png", Sound.STONE, true),
    PLANKS(2, "planks.png", Sound.WOOD, true),
    DIRT(3, "dirt.png", Sound.GRASS, true),
    STONE(4, "stone.png", Sound.STONE, true),
    GRASS(5, "grass.png", Sound.GRASS, true),
    LEAVES(6, "leaves.png", Sound.GRASS, true),
    LOG(7, "oak_log.png", Sound.WOOD, true),
    BRICKS(8, "stone_brick.png", Sound.STONE, true);

    final int id;
    final String texturePath;
    final Sound sound;
    final boolean isOpaque;
    Material(int id, String texturePath, Sound sound, boolean isOpaque){
        this.id = id;
        this.texturePath = texturePath;
        this.sound = sound;
        this.isOpaque = isOpaque;
    }
}
