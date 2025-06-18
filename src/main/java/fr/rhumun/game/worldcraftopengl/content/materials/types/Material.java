package fr.rhumun.game.worldcraftopengl.content.materials.types;

import fr.rhumun.game.worldcraftopengl.content.materials.*;
import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import lombok.Getter;

@Getter
public enum Material {
    COBBLE(new CobbleMaterial()),
    PLANKS(new PlanksMaterial()),
    DIRT(new DirtMaterial()),
    STONE(new StoneMaterial()),
    GRASS_BLOCK(new GrassBlockMaterial()),
    LEAVES(new LeavesMaterial()),
    LOG(new OakLogMaterial()),
    STONE_BRICK(new StoneBricksMaterial()),
    LAMP(new LampMaterial()),
    PURPLE_LAMP(new PurpleLampMaterial()),
    CYAN_LAMP(new CyanLampMaterial()),
    WATER(new WaterMaterial()),
    GRASS(new GrassMaterial()),
    RED_FLOWER(new RedFlowerMaterial()),
    BLUE_FLOWER(new BlueFlowerMaterial()),
    DANDELION(new DandelionMaterial()),
    SAND(new SandMaterial()),
    SAPLING(new SaplingMaterial()),
    LANTERN(new LanternMaterial()),
    WHITE_WOOL(new WoolMaterial("white")),
    BLACK_WOOL(new WoolMaterial("black")),
    BLUE_WOOL(new WoolMaterial("blue")),
    BROWN_WOOL(new WoolMaterial("brown")),
    CYAN_WOOL(new WoolMaterial("cyan")),
    GRAY_WOOL(new WoolMaterial("gray")),
    GREEN_WOOL(new WoolMaterial("green")),
    LIGHT_BLUE_WOOL(new WoolMaterial("light_blue")),
    LIGHT_GRAY_WOOL(new WoolMaterial("light_gray")),
    LIME_WOOL(new WoolMaterial("lime")),
    MAGENTA_WOOL(new WoolMaterial("magenta")),
    ORANGE_WOOL(new WoolMaterial("orange")),
    PINK_WOOL(new WoolMaterial("pink")),
    PURPLE_WOOL(new WoolMaterial("purple")),
    RED_WOOL(new WoolMaterial("red")),
    YELLOW_WOOL(new WoolMaterial("yellow")),
    WHITE_TERRACOTTA(new TerracottaMaterial("white")),
    BLACK_TERRACOTTA(new TerracottaMaterial("black")),
    BLUE_TERRACOTTA(new TerracottaMaterial("blue")),
    BROWN_TERRACOTTA(new TerracottaMaterial("brown")),
    CYAN_TERRACOTTA(new TerracottaMaterial("cyan")),
    GRAY_TERRACOTTA(new TerracottaMaterial("gray")),
    GREEN_TERRACOTTA(new TerracottaMaterial("green")),
    LIGHT_BLUE_TERRACOTTA(new TerracottaMaterial("light_blue")),
    LIGHT_GRAY_TERRACOTTA(new TerracottaMaterial("light_gray")),
    LIME_TERRACOTTA(new TerracottaMaterial("lime")),
    MAGENTA_TERRACOTTA(new TerracottaMaterial("magenta")),
    ORANGE_TERRACOTTA(new TerracottaMaterial("orange")),
    PINK_TERRACOTTA(new TerracottaMaterial("pink")),
    PURPLE_TERRACOTTA(new TerracottaMaterial("purple")),
    RED_TERRACOTTA(new TerracottaMaterial("red")),
    YELLOW_TERRACOTTA(new TerracottaMaterial("yellow")),
    TERRACOTTA(new TerracottaMaterial()),
    BOOKSHELF(new BookshelfMaterial()),
    BIRCH_LOG(new BirchLogMaterial()),
    BIRCH_PLANKS(new BirchPlanksMaterial()),
    BIRCH_LEAVES(new BirchLeavesMaterial()),
    BIRCH_SAPLING(new BirchSaplingMaterial()),
    ACACIA_LOG(new AcaciaLogMaterial()),
    ACACIA_PLANKS(new AcaciaPlanksMaterial()),
    ACACIA_LEAVES(new AcaciaLeavesMaterial()),
    ACACIA_SAPLING(new AcaciaSaplingMaterial()),
    ACACIA_DOOR(new AcaciaDoorMaterial()),
    ACACIA_TRAPDOOR(new AcaciaTrapdoorMaterial()),
    BRICKS(new BricksMaterial()),
    GLASS(new GlassMaterial()),
    CALCITE(new CalciteMaterial()),
    CALCITE_BRICK(new CalciteBrickMaterial()),
    POLISHED_DARK_BRICK(new PolishedBarkBricksMaterial()),
    SNOW(new SnowMaterial()),
    SNOWY_GRASS(new SnowyGrassMaterial()),
    PUMPKIN(new PumpkinMaterial()),
    JACKOLANTERN(new JackOLanternMaterial()),
    DARK_STONE(new DarkStoneMaterial()),
    DARK_COBBLE(new DarkCobbleMaterial()),
    DARK_STONE_BRICK(new DarkStoneBrickMaterial()),
    SAWMILL(new SawmillMaterial()),
    BLACKSTONE(new BlackstoneMaterial()),
    CRACKED_STONE_BRICK(new CrackedStoneBrickMaterial()),
    DARK_PLANKS(new DarkPlanksMaterial()),
    GRASS_BLOCK_SIDE_OVERLAY(new GrassBlockSideOverlayMaterial()),
    MUD_BRICKS(new MudBricksMaterial()),
    POLISHED_BLACKSTONE_BRICKS(new PolishedBlackstoneBricksMaterial()),
    REDSTONE_LAMP_ON(new RedstoneLampMaterial()),
    STONE_DIORITE(new StoneDioriteMaterial()),
    TORCH(new TorchMaterial());

    static int maxID = 0;

    final AbstractMaterial material;
    static Material[] MATERIALS;
    Material(final AbstractMaterial material) {
        this.material = material;
    }

    public static void init(){
        MATERIALS = Material.values().clone();
    }

    public static Material getById(short id){
        return MATERIALS[id];
    }

    public Sound getPlaceSound(){ return this.material.getPlaceSound(); }
    public Sound getBreakSound(){ return this.material.getBreakSound(); }
    public int getId(){ return this.material.getId(); }
    public int getTextureID(){ return this.material.getTexture().getId(); }
    public OpacityType getOpacity(){ return this.material.getOpacity(); }
    public float getDensity(){ return this.material.getDensity(); }

    public static int createID(){
        return maxID++;
    }

    public boolean isLiquid() {
        return this.material.getOpacity()==OpacityType.LIQUID;
    }
}
