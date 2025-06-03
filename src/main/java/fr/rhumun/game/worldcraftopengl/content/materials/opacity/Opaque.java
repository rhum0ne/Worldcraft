package fr.rhumun.game.worldcraftopengl.content.materials.opacity;

import fr.rhumun.game.worldcraftopengl.worlds.Block;

public class Opaque implements AbstractOpacity {
    @Override
    public boolean isVisibleWith(Block block) {
        return !block.isOpaque();
    }
    @Override
    public int getMaxViewDistance() {
        return -1;
    }
}
