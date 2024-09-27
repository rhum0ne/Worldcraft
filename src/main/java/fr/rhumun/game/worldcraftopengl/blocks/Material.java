package fr.rhumun.game.worldcraftopengl.blocks;

import fr.rhumun.game.worldcraftopengl.blocks.materials.*;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import lombok.Getter;

@Getter
public enum Material {
    COBBLE(new CobbleMaterial()),
    PLANKS(new PlanksMaterial()),
    DIRT(new DirtMaterial()),
    STONE(new StoneMaterial()),
    GRASS(new GrassMaterial()),
    LEAVES(new LeavesMaterial()),
    LOG(new OakLogMaterial()),
    BRICKS(new StoneBricksMaterial()),
    LAMP(new LampMaterial());

    static int maxID = 0;

    final AbstractMaterial material;
    Material(final AbstractMaterial material) {
        this.material = material;
    }

    public Sound getSound(){ return this.material.getSound(); }
    public int getId(){ return this.material.getId(); }
    public String getTexturePath(){ return this.material.getTexturePath(); }
    public boolean isOpaque(){ return this.material.isOpaque(); }

    public static int createID(){
        return ++maxID;
    }
}
