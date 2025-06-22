package fr.rhumun.game.worldcraftopengl.content.textures;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ButtonTextureMaker;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

@Getter
@Setter
public class Texture {

    public static Texture COBBLE;
    public static Texture DIRT;
    public static Texture GRASS_BLOCK;
    public static Texture OAK_LOG;
    public static Texture OAK_LOG_TOP;
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
    public static Texture BIRCH_LOG_TOP;
    public static Texture BIRCH_PLANKS;
    public static Texture BIRCH_LEAVES;
    public static Texture BIRCH_SAPLING;
    public static Texture ACACIA_LOG;
    public static Texture ACACIA_LOG_TOP;
    public static Texture ACACIA_PLANKS;
    public static Texture ACACIA_LEAVES;
    public static Texture ACACIA_SAPLING;
    public static Texture ACACIA_DOOR;
    public static Texture ACACIA_DOOR_TOP;
    public static Texture ACACIA_TRAPDOOR;
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
    public static Texture TERRACOTTA;
    public static Texture BLACK_TERRACOTTA;
    public static Texture BLUE_TERRACOTTA;
    public static Texture BROWN_TERRACOTTA;
    public static Texture CYAN_TERRACOTTA;
    public static Texture GRAY_TERRACOTTA;
    public static Texture GREEN_TERRACOTTA;
    public static Texture LIGHT_BLUE_TERRACOTTA;
    public static Texture LIGHT_GRAY_TERRACOTTA;
    public static Texture LIME_TERRACOTTA;
    public static Texture MAGENTA_TERRACOTTA;
    public static Texture ORANGE_TERRACOTTA;
    public static Texture PINK_TERRACOTTA;
    public static Texture YELLOW_TERRACOTTA;
    public static Texture RED_TERRACOTTA;
    public static Texture WHITE_TERRACOTTA;
    public static Texture PURPLE_TERRACOTTA;
    public static Texture PUMPKIN;
    public static Texture PUMPKIN_TOP;
    public static Texture JACK_O_LANTERN;;
    public static Texture SNOWY_GRASS;
    public static Texture CALCITE_BRICK;
    public static Texture SAWMILL_TOP;
    public static Texture SAWMILL_SIDE;
    public static Texture SAWMILL_BOTTOM;
    public static Texture SAWMILL_SIDE_2;
    public static Texture SAWMILL_SIDE_3;
    public static Texture SAWMILL_FRONT;
    public static Texture STONE_CUTTER_TOP;
    public static Texture STONE_CUTTER_SIDE;
    public static Texture STONE_CUTTER_BOTTOM;
    public static Texture STONE_CUTTER_SIDE_2;
    public static Texture STONE_CUTTER_FRONT;

    public static Texture BLACKSTONE;
    public static Texture CRACKED_STONE_BRICK;
    public static Texture DARK_PLANKS;
    public static Texture GRASS_BLOCK_SIDE_OVERLAY;
    public static Texture MUD_BRICKS;
    public static Texture POLISHED_BLACKSTONE_BRICKS;
    public static Texture REDSTONE_LAMP_ON;
    public static Texture STONE_DIORITE1;
    public static Texture TORCH;
    public static Texture CROSSHAIR;
    public static Texture HOTBAR;
    public static Texture SELECTED_SLOT;
    public static Texture CREATIVE_INVENTORY;
    public static Texture INVENTORY;
    public static Texture WORKBENCH;
    public static Texture SQUARE_BUTTON;
    public static Texture DEFAULT_BUTTON;
    public static Texture DEFAULT_BUTTON_HOVERED;
    public static Texture DEFAULT_BUTTON_UNACTIVE;
    public static Texture DEFAULT_BUTTON_UNACTIVE_SELECTED;
    public static Texture WALLPAPER;

    public static Texture HEART_CONTAINER;
    public static Texture HEART_FULL;
    public static Texture HEART_HALF;

    public static Texture HUNGER_CONTAINER;
    public static Texture HUNGER_FULL;
    public static Texture HUNGER_HALF;


    // Item textures
    public static Texture APPLE;
    public static Texture BREAD;
    public static Texture DIAMOND;
    public static Texture IRON_INGOT;
    public static Texture IRON_AXE;
    public static Texture IRON_PICKAXE;
    public static Texture IRON_SHOVEL;
    public static Texture IRON_SWORD;
    public static Texture STICK;
    public static Texture WATER_BUCKET;
    public static Texture WOODEN_AXE;
    public static Texture WOODEN_HOE;
    public static Texture WOODEN_PICKAXE;
    public static Texture WOODEN_SHOVEL;
    public static Texture WOODEN_SWORD;

    public static Texture OTTER;
    public static Texture ROCKY;
    public static Texture NINJA_SKELETON;

    public static void init(){
        COBBLE = new Texture("cobble.png");
        DIRT = new Texture("dirt.png");
        GRASS_BLOCK = new Texture("grass.png");
        OAK_LOG = new Texture("oak_log.png");
        OAK_LOG_TOP = new Texture("oak_log_top.png");
        LEAVES = new Texture("oak_leaves.png");
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
        SAPLING = new Texture("oak_sapling.png");
        LANTERN = new Texture("lantern.png");
        BIRCH_LOG = new Texture("birch_log.png");
        BIRCH_LOG_TOP = new Texture("birch_log_top.png");
        BIRCH_PLANKS = new Texture("birch_planks.png");
        BIRCH_LEAVES = new Texture("birch_leaves.png");
        BIRCH_SAPLING = new Texture("birch_sapling.png");
        BLACK_WOOL = new Texture("black_wool.png");
        BLUE_WOOL = new Texture("blue_wool.png");
        BOOKSHELF = new Texture("bookshelf.png");
        BOOKSHELF_TOP = new Texture("bookshelf_top.png");
        BRICKS = new Texture("bricks.png");
        BROWN_WOOL = new Texture("brown_wool.png");
        CALCITE = new Texture("calcite.png");
        CALCITE_BRICK = new Texture("calcite_brick.png");
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
        TERRACOTTA = new Texture("terracotta.png");
        BLACK_TERRACOTTA = new Texture("black_terracotta.png");
        BLUE_TERRACOTTA = new Texture("blue_terracotta.png");
        BROWN_TERRACOTTA = new Texture("brown_terracotta.png");
        CYAN_TERRACOTTA = new Texture("cyan_terracotta.png");
        GRAY_TERRACOTTA = new Texture("gray_terracotta.png");
        GREEN_TERRACOTTA = new Texture("green_terracotta.png");
        LIGHT_BLUE_TERRACOTTA = new Texture("light_blue_terracotta.png");
        LIGHT_GRAY_TERRACOTTA = new Texture("light_gray_terracotta.png");
        LIME_TERRACOTTA = new Texture("lime_terracotta.png");
        MAGENTA_TERRACOTTA = new Texture("magenta_terracotta.png");
        ORANGE_TERRACOTTA = new Texture("orange_terracotta.png");
        PINK_TERRACOTTA = new Texture("pink_terracotta.png");
        PURPLE_TERRACOTTA = new Texture("purple_terracotta.png");
        RED_TERRACOTTA = new Texture("red_terracotta.png");
        WHITE_TERRACOTTA = new Texture("white_terracotta.png");
        YELLOW_TERRACOTTA = new Texture("yellow_terracotta.png");
        PUMPKIN = new Texture("pumpkin.png");
        PUMPKIN_TOP = new Texture("pumpkin_top.png");
        JACK_O_LANTERN = new Texture("jack_o_lantern.png");
        SNOWY_GRASS = new Texture("snowy_grass.png");
        BLACKSTONE = new Texture("blackstone.png");
        CRACKED_STONE_BRICK = new Texture("cracked_stone_brick.png");
        DARK_PLANKS = new Texture("dark_planks.png");
        GRASS_BLOCK_SIDE_OVERLAY = new Texture("grass_block_side_overlay.png");
        MUD_BRICKS = new Texture("mud_bricks.png");
        POLISHED_BLACKSTONE_BRICKS = new Texture("polished_blackstone_bricks.png");
        REDSTONE_LAMP_ON = new Texture("redstone_lamp_on.png");
        STONE_DIORITE1 = new Texture("stone_diorite1.png");
        TORCH = new Texture("torch.png");
        SAWMILL_TOP = new Texture("sawmill_top.png");
        SAWMILL_SIDE = new Texture("sawmill_side.png");
        SAWMILL_BOTTOM = new Texture("sawmill_bottom.png");
        SAWMILL_SIDE_2 = new Texture("sawmill_side_1.png");
        SAWMILL_SIDE_3 = new Texture("sawmill_side_2.png");
        SAWMILL_FRONT = new Texture("sawmill_front.png");
        STONE_CUTTER_TOP = new Texture("stone_cutter_top.png");
        STONE_CUTTER_SIDE = new Texture("stone_cutter_side.png");
        STONE_CUTTER_BOTTOM = new Texture("stone_cutter_bottom.png");
        STONE_CUTTER_SIDE_2 = new Texture("stone_cutter_side_2.png");
        STONE_CUTTER_FRONT = new Texture("stone_cutter_front.png");
        ACACIA_LOG = new Texture("acacia_log.png");
        ACACIA_LOG_TOP = new Texture("acacia_log_top.png");
        ACACIA_PLANKS = new Texture("acacia_planks.png");
        ACACIA_LEAVES = new Texture("acacia_leaves.png");
        ACACIA_SAPLING = new Texture("acacia_sapling.png");
        ACACIA_DOOR_TOP = new Texture("acacia_door_top.png");
        ACACIA_DOOR = new Texture("acacia_door_bottom.png");
        ACACIA_TRAPDOOR = new Texture("acacia_trapdoor.png");

        CROSSHAIR = new Texture(TextureTypes.GUIS,"hud\\crosshair.png");
        HOTBAR = new Texture(TextureTypes.GUIS,"hud\\hotbar.png");
        SELECTED_SLOT = new Texture(TextureTypes.GUIS,"hud\\hotbar_selection.png");
        CREATIVE_INVENTORY = new Texture(TextureTypes.GUIS,"hud\\creative-inventory.png");
        INVENTORY = new Texture(TextureTypes.GUIS,"hud\\inventory.png");
        WORKBENCH = new Texture(TextureTypes.GUIS,"hud\\workbench.png");
        SQUARE_BUTTON = new Texture(TextureTypes.GUIS,"hud\\widgets\\checkbox.png");
        DEFAULT_BUTTON = new Texture(TextureTypes.GUIS, "hud\\widgets\\button.png");
        DEFAULT_BUTTON_HOVERED = new Texture(TextureTypes.GUIS, "hud\\widgets\\hovered.png");
        DEFAULT_BUTTON_UNACTIVE = new Texture(TextureTypes.GUIS, "hud\\widgets\\unactive.png");
        DEFAULT_BUTTON_UNACTIVE_SELECTED = new Texture(TextureTypes.GUIS, "hud\\widgets\\unactive_selected.png");
        WALLPAPER = new Texture(TextureTypes.GUIS, "hud\\wallpaper.png");

        HEART_CONTAINER = new Texture(TextureTypes.GUIS, "hud\\heart\\container.png");
        HEART_FULL = new Texture(TextureTypes.GUIS, "hud\\heart\\full.png");
        HEART_HALF = new Texture(TextureTypes.GUIS, "hud\\heart\\half.png");

        HUNGER_CONTAINER = new Texture(TextureTypes.GUIS, "hud\\food_empty.png");
        HUNGER_FULL = new Texture(TextureTypes.GUIS, "hud\\food_full.png");
        HUNGER_HALF = new Texture(TextureTypes.GUIS, "hud\\food_half.png");

        // Item textures initialization
        APPLE = new Texture("items\\apple.png");
        BREAD = new Texture("items\\bread.png");
        DIAMOND = new Texture("items\\diamond.png");
        IRON_INGOT = new Texture("items\\iron_ingot.png");
        IRON_AXE = new Texture("items\\iron_axe.png");
        IRON_PICKAXE = new Texture("items\\iron_pickaxe.png");
        IRON_SHOVEL = new Texture("items\\iron_shovel.png");
        IRON_SWORD = new Texture("items\\iron_sword.png");
        STICK = new Texture("items\\stick.png");
        WATER_BUCKET = new Texture("items\\water_bucket.png");
        WOODEN_AXE = new Texture("items\\wooden_axe.png");
        WOODEN_HOE = new Texture("items\\wooden_hoe.png");
        WOODEN_PICKAXE = new Texture("items\\wooden_pickaxe.png");
        WOODEN_SHOVEL = new Texture("items\\wooden_shovel.png");
        WOODEN_SWORD = new Texture("items\\wooden_sword.png");

        OTTER = new Texture(TextureTypes.ENTITIES,"entities\\nocsy_otter_v2.png");
        ROCKY = new Texture(TextureTypes.ENTITIES,"entities\\Rocky.png");
        NINJA_SKELETON = new Texture(TextureTypes.ENTITIES,"entities\\ninja_skeleton.png");
    }

    public static ArrayList<Texture> textures = new ArrayList<>();
    public static HashMap<String, Texture> textureByName = new HashMap<>();
    public static Texture getByName(String s) {
        return textureByName.get(s);
    }


    private String path;
    private final String name;
    private final int id;

    private ByteBuffer buffer;
    private int width;
    private int height;

    public Texture(TextureTypes type, ByteBuffer buffer, String name, int width, int height) {
        this.buffer = buffer;
        this.name = name;
        this.width = width;
        this.height = height;
        this.id = textures.size();
        textureByName.put(this.name, this);
        type.add(this);
    }
    public Texture(String path){ this(TextureTypes.BLOCKS, path); }
    public Texture(TextureTypes type, String path){
        this.path = path;
        this.id = textures.size();

        type.add(this);
        textures.add(this);

        String name=path;
        while(name.indexOf('\\') != -1){
            name = name.substring(1);
        }
        this.name = name.substring(0, name.length()-4);
        textureByName.put(this.name, this);

    }

    public boolean isBuffered() { return buffer != null; }

    public String getPath(){ return Game.TEXTURES_PATH + this.path; }

    public boolean isAnimated(){return this instanceof AnimatedTexture;}

}
