package fr.rhumun.game.worldcraftopengl.blocks.materials.opacity;

import fr.rhumun.game.worldcraftopengl.blocks.Block;

public class Transparent implements AbstractOpacity {
    @Override
    public boolean isVisibleWith(Block block) {
        return true;
    }
}
