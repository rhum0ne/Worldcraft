package fr.rhumun.game.worldcraftopengl.blocks.materials.types;

import fr.rhumun.game.worldcraftopengl.blocks.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.blocks.materials.*;
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
    SAND(new SandMaterial()),
    SAPLING(new SaplingMaterial());

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
}
