package fr.rhumun.game.worldcraftopengl.blocks.materials.opacity;

import fr.rhumun.game.worldcraftopengl.blocks.Block;

public class Liquid implements AbstractOpacity {
    @Override
    public boolean isVisibleWith(Block block) {
        if(block.getMaterial() == null) return true;
        return !block.isOpaque() && block.getMaterial().getOpacity() != OpacityType.LIQUID;
    }
}
