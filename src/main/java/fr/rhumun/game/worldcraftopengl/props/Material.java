package fr.rhumun.game.worldcraftopengl.props;

import lombok.Getter;

@Getter
public enum Material {
    COBBLE(1, "cobble.png"),
    PLANKS(2, "planks.png"),
    DIRT(3, "dirt.png"),
    STONE(4, "stone.png"),
    LOG(5, "wood.png"),
    BRICKS(6, "dark-bricks.png");

    final int id;
    final String texturePath;
    Material(int id, String texturePath){
        this.id = id;
        this.texturePath = texturePath;
    }
}
