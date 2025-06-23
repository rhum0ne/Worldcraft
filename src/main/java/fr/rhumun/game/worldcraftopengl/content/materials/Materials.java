package fr.rhumun.game.worldcraftopengl.content.materials;

import fr.rhumun.game.worldcraftopengl.content.materials.blocks.*;
import fr.rhumun.game.worldcraftopengl.content.materials.items.types.BlockItemMaterial;
import fr.rhumun.game.worldcraftopengl.content.materials.items.types.FoodMaterial;
import fr.rhumun.game.worldcraftopengl.content.materials.items.types.ItemMaterial;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class Materials {

    public static Material COBBLE = new CobbleMaterial();
    public static Material PLANKS = new PlanksMaterial();
    public static Material DIRT = new DirtMaterial();
    public static Material STONE = new StoneMaterial();
    public static Material GRASS_BLOCK = new GrassBlockMaterial();
    public static Material LEAVES = new LeavesMaterial();
    public static Material LOG = new OakLogMaterial();
    public static Material STONE_BRICK = new StoneBricksMaterial();
    public static Material LAMP = new LampMaterial();
    public static Material PURPLE_LAMP = new PurpleLampMaterial();
    public static Material CYAN_LAMP = new CyanLampMaterial();
    public static Material WATER = new WaterMaterial();
    public static Material GRASS = new GrassMaterial();
    public static Material RED_FLOWER = new RedFlowerMaterial();
    public static Material BLUE_FLOWER = new BlueFlowerMaterial();
    public static Material DANDELION = new DandelionMaterial();
    public static Material SAND = new SandMaterial();
    public static Material SAPLING = new SaplingMaterial();
    public static Material LANTERN = new LanternMaterial();
    public static Material WHITE_WOOL = new WoolMaterial("white");
    public static Material BLACK_WOOL = new WoolMaterial("black");
    public static Material BLUE_WOOL = new WoolMaterial("blue");
    public static Material BROWN_WOOL = new WoolMaterial("brown");
    public static Material CYAN_WOOL = new WoolMaterial("cyan");
    public static Material GRAY_WOOL = new WoolMaterial("gray");
    public static Material GREEN_WOOL = new WoolMaterial("green");
    public static Material LIGHT_BLUE_WOOL = new WoolMaterial("light_blue");
    public static Material LIGHT_GRAY_WOOL = new WoolMaterial("light_gray");
    public static Material LIME_WOOL = new WoolMaterial("lime");
    public static Material MAGENTA_WOOL = new WoolMaterial("magenta");
    public static Material ORANGE_WOOL = new WoolMaterial("orange");
    public static Material PINK_WOOL = new WoolMaterial("pink");
    public static Material PURPLE_WOOL = new WoolMaterial("purple");
    public static Material RED_WOOL = new WoolMaterial("red");
    public static Material YELLOW_WOOL = new WoolMaterial("yellow");
    public static Material WHITE_TERRACOTTA = new TerracottaMaterial("white");
    public static Material BLACK_TERRACOTTA = new TerracottaMaterial("black");
    public static Material BLUE_TERRACOTTA = new TerracottaMaterial("blue");
    public static Material BROWN_TERRACOTTA = new TerracottaMaterial("brown");
    public static Material CYAN_TERRACOTTA = new TerracottaMaterial("cyan");
    public static Material GRAY_TERRACOTTA = new TerracottaMaterial("gray");
    public static Material GREEN_TERRACOTTA = new TerracottaMaterial("green");
    public static Material LIGHT_BLUE_TERRACOTTA = new TerracottaMaterial("light_blue");
    public static Material LIGHT_GRAY_TERRACOTTA = new TerracottaMaterial("light_gray");
    public static Material LIME_TERRACOTTA = new TerracottaMaterial("lime");
    public static Material MAGENTA_TERRACOTTA = new TerracottaMaterial("magenta");
    public static Material ORANGE_TERRACOTTA = new TerracottaMaterial("orange");
    public static Material PINK_TERRACOTTA = new TerracottaMaterial("pink");
    public static Material PURPLE_TERRACOTTA = new TerracottaMaterial("purple");
    public static Material RED_TERRACOTTA = new TerracottaMaterial("red");
    public static Material YELLOW_TERRACOTTA = new TerracottaMaterial("yellow");
    public static Material TERRACOTTA = new TerracottaMaterial();
    public static Material BOOKSHELF = new BookshelfMaterial();
    public static Material BIRCH_LOG = new BirchLogMaterial();
    public static Material BIRCH_PLANKS = new BirchPlanksMaterial();
    public static Material BIRCH_LEAVES = new BirchLeavesMaterial();
    public static Material BIRCH_SAPLING = new BirchSaplingMaterial();
    public static Material ACACIA_LOG = new AcaciaLogMaterial();
    public static Material ACACIA_PLANKS = new AcaciaPlanksMaterial();
    public static Material ACACIA_LEAVES = new AcaciaLeavesMaterial();
    public static Material ACACIA_SAPLING = new AcaciaSaplingMaterial();
    public static Material ACACIA_DOOR = new AcaciaDoorMaterial();
    public static Material ACACIA_DOOR_TOP = new AcaciaDoorTopMaterial();
    public static Material ACACIA_TRAPDOOR = new AcaciaTrapdoorMaterial();
    public static Material BRICKS = new BricksMaterial();
    public static Material GLASS = new GlassMaterial();
    public static Material CALCITE = new CalciteMaterial();
    public static Material CALCITE_BRICK = new CalciteBrickMaterial();
    public static Material POLISHED_DARK_BRICK = new PolishedBarkBricksMaterial();
    public static Material SNOW = new SnowMaterial();
    public static Material SNOWY_GRASS = new SnowyGrassMaterial();
    public static Material PUMPKIN = new PumpkinMaterial();
    public static Material JACKOLANTERN = new JackOLanternMaterial();
    public static Material DARK_STONE = new DarkStoneMaterial();
    public static Material DARK_COBBLE = new DarkCobbleMaterial();
    public static Material DARK_STONE_BRICK = new DarkStoneBrickMaterial();
    public static Material SAWMILL = new SawmillMaterial();
    public static Material BLACKSTONE = new BlackstoneMaterial();
    public static Material CRACKED_STONE_BRICK = new CrackedStoneBrickMaterial();
    public static Material DARK_PLANKS = new DarkPlanksMaterial();
    public static Material MUD_BRICKS = new MudBricksMaterial();
    public static Material POLISHED_BLACKSTONE_BRICKS = new PolishedBlackstoneBricksMaterial();
    public static Material REDSTONE_LAMP_ON = new RedstoneLampMaterial();
    public static Material STONE_DIORITE = new StoneDioriteMaterial();
    public static Material TORCH = new TorchMaterial();
    public static Material STONE_CUTTER = new StoneCutterMaterial();

    // Non-placeable items
    public static Material APPLE = new FoodMaterial(Texture.APPLE, 3);
    public static Material BREAD = new FoodMaterial(Texture.BREAD, 4);
    public static Material DIAMOND = new ItemMaterial(Texture.DIAMOND);
    public static Material IRON_INGOT = new ItemMaterial(Texture.IRON_INGOT);
    public static Material IRON_AXE = new ItemMaterial(Texture.IRON_AXE);
    public static Material IRON_PICKAXE = new ItemMaterial(Texture.IRON_PICKAXE);
    public static Material IRON_SHOVEL = new ItemMaterial(Texture.IRON_SHOVEL);
    public static Material IRON_SWORD = new ItemMaterial(Texture.IRON_SWORD);
    public static Material STICK = new ItemMaterial(Texture.STICK);
    public static Material WATER_BUCKET = new BlockItemMaterial(Texture.WATER_BUCKET, WATER);
    public static Material ACADIA_DOOR_ITEM = new BlockItemMaterial(Texture.ACACIA_DOOR_ITEM, ACACIA_DOOR);
    public static Material WOODEN_AXE = new ItemMaterial(Texture.WOODEN_AXE);
    public static Material WOODEN_HOE = new ItemMaterial(Texture.WOODEN_HOE);
    public static Material WOODEN_PICKAXE = new ItemMaterial(Texture.WOODEN_PICKAXE);
    public static Material WOODEN_SHOVEL = new ItemMaterial(Texture.WOODEN_SHOVEL);
    public static Material WOODEN_SWORD = new ItemMaterial(Texture.WOODEN_SWORD);


    public static Material[] Registry;

    public static void init() {
        List<Material> materials = new ArrayList<>();
        for (Field field : Materials.class.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers()) && Material.class.isAssignableFrom(field.getType())) {
                try {
                    materials.add((Material) field.get(null));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        Registry = materials.toArray(new Material[0]);
    }

    public static Material getById(short id){
        return Registry[id];
    }

}
