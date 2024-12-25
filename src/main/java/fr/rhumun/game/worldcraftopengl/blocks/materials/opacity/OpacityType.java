package fr.rhumun.game.worldcraftopengl.blocks.materials.opacity;

import fr.rhumun.game.worldcraftopengl.blocks.Block;

public enum OpacityType {
    OPAQUE(new Opaque()),
    TRANSPARENT(new Transparent()),
    LIQUID(new Liquid());

    private final AbstractOpacity opacity;

    OpacityType(AbstractOpacity abstractOpacity){
        this.opacity = abstractOpacity;
    }

    public boolean isVisibleWith(Block block){
        return this.opacity.isVisibleWith(block);
    }

    public int getPriority(){ return this.ordinal(); }
}
