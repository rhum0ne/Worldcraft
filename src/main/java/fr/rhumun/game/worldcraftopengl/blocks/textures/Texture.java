package fr.rhumun.game.worldcraftopengl.blocks.textures;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Texture {

    public static Texture COBBLE;
    public static Texture DIRT;
    public static Texture GRASS_BLOCK;
    public static Texture OAK_LOG;
    public static Texture LEAVES;
    public static Texture STONE_BRICKS;
    public static Texture STONE;
    public static Texture PLANKS;
    public static Texture LAMP;
    public static Texture WATER;
    public static Texture GRASS;
    public static Texture SAND;
    public static Texture RED_FLOWER;
    public static Texture BLUE_FLOWER;
    public static Texture SAPLING;
    public static Texture LANTERN;

    public static Texture CROSSHAIR;
    public static Texture HOTBAR;

    public static void init(){
        COBBLE = new Texture(1, "cobble.png");
        DIRT = new Texture(2, "dirt.png");
        GRASS_BLOCK = new Texture(3, "grass.png");
        OAK_LOG = new Texture(4, "oak_log.png");
        LEAVES = new Texture(5, "leaves.png");
        STONE_BRICKS = new Texture(6, "stone_brick.png");
        STONE = new Texture(7, "stone.png");
        PLANKS = new Texture(8, "planks.png");
        LAMP = new Texture(9, "lamp.png");
        WATER = new Texture(10, "water.png");
        GRASS = new Texture(11, "grass-plant.png");
        RED_FLOWER = new Texture(12, "red-flower.png");
        BLUE_FLOWER = new Texture(13, "blue-flower.png");
        SAND = new Texture(14, "sand.png");
        SAPLING = new Texture(15, "sapling.png");
        LANTERN = new Texture(16, "lantern.png");
        CROSSHAIR = new Texture(17, "hud\\crosshair.png");
        HOTBAR = new Texture(18, "hud\\hotbar.png");
    }

    public static ArrayList<Texture> textures = new ArrayList<>();

    private String path;
    private int id;

    public Texture(int id, String path){
        this.id = id;
        this.path = path;
        textures.add(this);
    }

    public boolean isAnimated(){return this instanceof AnimatedTexture;}

}
