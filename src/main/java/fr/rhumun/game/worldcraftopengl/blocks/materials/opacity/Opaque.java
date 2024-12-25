package fr.rhumun.game.worldcraftopengl.blocks.materials.opacity;

import fr.rhumun.game.worldcraftopengl.blocks.Block;

public class Opaque implements AbstractOpacity {
    @Override
    public boolean isVisibleWith(Block block) {
        return !block.isOpaque();
    }
}
