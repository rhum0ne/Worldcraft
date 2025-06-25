package fr.rhumun.game.worldcraftopengl.content.materials.opacity;

import fr.rhumun.game.worldcraftopengl.worlds.Block;

public class Liquid implements AbstractOpacity {
    @Override
    public boolean isVisibleWith(Block block) {
        if(block.isAir()) return true;
        return !block.isOpaque() && block.getMaterial().getOpacity() != OpacityType.LIQUID;
    }

    @Override
    public int getMaxViewDistance() {
        return -1;
    }


}
