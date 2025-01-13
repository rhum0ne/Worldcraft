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
    BOOKSHELF(new BookshelfMaterial()),
    BIRCH_LOG(new BirchLogMaterial()),
    BIRCH_PLANKS(new BirchPlanksMaterial()),
    BRICKS(new BricksMaterial()),
    GLASS(new GlassMaterial()),
    CALCITE(new CalciteMaterial()),
    POLISHED_DARK_BRICK(new PolishedBarkBricksMaterial()),
    SNOW(new SnowMaterial()),
    DARK_STONE(new DarkStoneMaterial()),
    DARK_COBBLE(new DarkCobbleMaterial()),
    DARK_STONE_BRICK(new DarkStoneBrickMaterial());

    static int maxID = 0;

    final AbstractMaterial material;
    Material(final AbstractMaterial material) {
        this.material = material;
    }

    public Sound getSound(){ return this.material.getSound(); }
    public int getId(){ return this.material.getId(); }
    public int getTextureID(){ return this.material.getTexture().getId(); }
    public OpacityType getOpacity(){ return this.material.getOpacity(); }

    public static int createID(){
        return ++maxID;
    }

    public boolean isLiquid() {
        return this.material.getOpacity()==OpacityType.LIQUID;
    }
}
