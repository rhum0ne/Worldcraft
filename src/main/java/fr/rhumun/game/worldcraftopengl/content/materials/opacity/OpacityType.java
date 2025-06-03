package fr.rhumun.game.worldcraftopengl.content.materials.opacity;

import fr.rhumun.game.worldcraftopengl.worlds.Block;

public enum OpacityType {
    OPAQUE(new Opaque()),
    TRANSPARENT(new Transparent()),
    LIQUID(new Liquid()),
    CLOSE_TRANSPARENT(new CloseTransparent());

    private final AbstractOpacity opacity;

    OpacityType(AbstractOpacity abstractOpacity){
        this.opacity = abstractOpacity;
    }

    public boolean isVisibleWith(Block block){
        return this.opacity.isVisibleWith(block);
    }

    public int getPriority(){ return this.ordinal(); }

    public int getMaxChunkDistance() {
        return this.opacity.getMaxViewDistance();
    }
}
