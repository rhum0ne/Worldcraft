package fr.rhumun.game.worldcraftopengl.content.materials.opacity;

import fr.rhumun.game.worldcraftopengl.content.Block;

public class Liquid implements AbstractOpacity {
    @Override
    public boolean isVisibleWith(Block block) {
        if(block.getMaterial() == null) return true;
        return !block.isOpaque() && block.getMaterial().getOpacity() != OpacityType.LIQUID;
    }

    @Override
    public int getMaxViewDistance() {
        return -1;
    }


}
