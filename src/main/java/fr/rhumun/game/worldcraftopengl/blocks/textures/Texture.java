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
    public static Texture SELECTED_SLOT;
    public static Texture CREATIVE_INVENTORY;
    public static Texture INVENTORY;

    public static void init(){
        COBBLE = new Texture("cobble.png");
        DIRT = new Texture("dirt.png");
        GRASS_BLOCK = new Texture("grass.png");
        OAK_LOG = new Texture("oak_log.png");
        LEAVES = new Texture("leaves.png");
        STONE_BRICKS = new Texture("stone_brick.png");
        STONE = new Texture("stone.png");
        PLANKS = new Texture("planks.png");
        LAMP = new Texture("lamp.png");
        WATER = new Texture("water.png");
        GRASS = new Texture("grass-plant.png");
        RED_FLOWER = new Texture("red-flower.png");
        BLUE_FLOWER = new Texture("blue-flower.png");
        SAND = new Texture("sand.png");
        SAPLING = new Texture("sapling.png");
        LANTERN = new Texture("lantern.png");


        CROSSHAIR = new Texture("hud\\crosshair.png");
        HOTBAR = new Texture("hud\\hotbar.png");
        SELECTED_SLOT = new Texture("hud\\hotbar_selection.png");
        CREATIVE_INVENTORY = new Texture("hud\\creative-inventory.png");
        INVENTORY = new Texture("hud\\inventory.png");
    }

    public static ArrayList<Texture> textures = new ArrayList<>();

    private final String path;
    private final int id;

    public Texture(String path){
        textures.add(this);
        this.path = path;
        this.id = textures.size();
    }

    public boolean isAnimated(){return this instanceof AnimatedTexture;}

}
