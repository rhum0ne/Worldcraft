package fr.rhumun.game.worldcraftopengl.blocks;

import lombok.Getter;

@Getter
public enum Texture {
    COBBLE(1, "cobble.png"),
    DIRT(2, "dirt.png"),
    GRASS_BLOCK(3, "grass.png"),
    OAK_LOG(4, "oak_log.png"),
    LEAVES(5, "leaves.png"),
    STONE_BRICKS(6, "stone_brick.png"),
    STONE(7, "stone.png"),
    PLANKS(8, "planks.png"),
    LAMP(9, "lamp.png"),
    WATER(10, "water.png"),
    GRASS(11, "grass-plant.png");

    String path;
    int id;
    Texture(int id, String path){
        this.id = id;
        this.path = path;
    }

}
