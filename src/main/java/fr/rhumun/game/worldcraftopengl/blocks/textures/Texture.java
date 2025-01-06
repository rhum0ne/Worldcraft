package fr.rhumun.game.worldcraftopengl.blocks.textures;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    public static Texture GRASS_TOP;
    public static Texture SAND;
    public static Texture RED_FLOWER;
    public static Texture BLUE_FLOWER;
    public static Texture DANDELION;
    public static Texture SAPLING;
    public static Texture LANTERN;
    public static Texture BIRCH_LOG;
    public static Texture BIRCH_PLANKS;
    public static Texture BLACK_WOOL;
    public static Texture BLUE_WOOL;
    public static Texture BOOKSHELF;
    public static Texture BOOKSHELF_TOP;
    public static Texture BRICKS;
    public static Texture BROWN_WOOL;
    public static Texture CALCITE;
    public static Texture CYAN_WOOL;
    public static Texture GLASS;
    public static Texture GRAY_WOOL;
    public static Texture GREEN_WOOL;
    public static Texture LIGHT_BLUE_WOOL;
    public static Texture LIGHT_GRAY_WOOL;
    public static Texture LIME_WOOL;
    public static Texture MAGENTA_WOOL;
    public static Texture ORANGE_WOOL;
    public static Texture PINK_WOOL;
    public static Texture POLISHED_DARK_BRICKS;
    public static Texture SNOW;
    public static Texture PURPLE_WOOL;
    public static Texture RED_WOOL;
    public static Texture WHITE_WOOL;
    public static Texture YELLOW_WOOL;
    public static Texture DARK_STONE;
    public static Texture DARK_COBBLE;
    public static Texture DARK_STONE_BRICK;

    public static Texture CROSSHAIR;
    public static Texture HOTBAR;
    public static Texture SELECTED_SLOT;
    public static Texture CREATIVE_INVENTORY;
    public static Texture INVENTORY;

    public static Map<Character, Integer> chars = new HashMap<>();

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
        GRASS_TOP = new Texture("grass_top.png");
        RED_FLOWER = new Texture("red-flower.png");
        BLUE_FLOWER = new Texture("blue-flower.png");
        DANDELION = new Texture("dandelion.png");
        SAND = new Texture("sand.png");
        SAPLING = new Texture("sapling.png");
        LANTERN = new Texture("lantern.png");
        BIRCH_LOG = new Texture("birch_log.png");
        BIRCH_PLANKS = new Texture("birch_planks.png");
        BLACK_WOOL = new Texture("black_wool.png");
        BLUE_WOOL = new Texture("blue_wool.png");
        BOOKSHELF = new Texture("bookshelf.png");
        BOOKSHELF_TOP = new Texture("bookshelf_top.png");
        BRICKS = new Texture("bricks.png");
        BROWN_WOOL = new Texture("brown_wool.png");
        CALCITE = new Texture("calcite.png");
        CYAN_WOOL = new Texture("cyan_wool.png");
        GLASS = new Texture("glass.png");
        GRAY_WOOL = new Texture("gray_wool.png");
        GREEN_WOOL = new Texture("green_wool.png");
        LIGHT_BLUE_WOOL = new Texture("light_blue_wool.png");
        LIGHT_GRAY_WOOL = new Texture("light_gray_wool.png");
        LIME_WOOL = new Texture("lime_wool.png");
        MAGENTA_WOOL = new Texture("magenta_wool.png");
        ORANGE_WOOL = new Texture("orange_wool.png");
        PINK_WOOL = new Texture("pink_wool.png");
        POLISHED_DARK_BRICKS = new Texture("polished_dark_bricks.png");
        SNOW = new Texture("snow.png");
        PURPLE_WOOL = new Texture("purple_wool.png");
        RED_WOOL = new Texture("red_wool.png");
        WHITE_WOOL = new Texture("white_wool.png");
        YELLOW_WOOL = new Texture("yellow_wool.png");
        DARK_STONE = new Texture("dark_stone.png");
        DARK_COBBLE = new Texture("dark_cobble.png");
        DARK_STONE_BRICK = new Texture("dark_stone_brick.png");

        CROSSHAIR = new Texture("hud\\crosshair.png");
        HOTBAR = new Texture("hud\\hotbar.png");
        SELECTED_SLOT = new Texture("hud\\hotbar_selection.png");
        CREATIVE_INVENTORY = new Texture("hud\\creative-inventory.png");
        INVENTORY = new Texture("hud\\inventory.png");
    }

    public static ArrayList<Texture> textures = new ArrayList<>();
    public static HashMap<String, Texture> textureByName = new HashMap<>();

    private final String path;
    @Setter
    private int id;

    public Texture(String path){
        textures.add(this);

        String name=path;
        while(name.indexOf('\\') != -1){
            name = name.substring(1);
        }
        name = name.substring(0, name.length()-4);
        textureByName.put(name, this);

        this.path = path;
        this.id = textures.size();
    }

    public static Texture getByName(String s) {
        return textureByName.get(s);
    }

    public boolean isAnimated(){return this instanceof AnimatedTexture;}

}
