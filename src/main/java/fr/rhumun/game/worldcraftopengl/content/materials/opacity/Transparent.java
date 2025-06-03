package fr.rhumun.game.worldcraftopengl.content.materials.opacity;

import fr.rhumun.game.worldcraftopengl.worlds.Block;

public class Transparent implements AbstractOpacity {
    @Override
    public boolean isVisibleWith(Block block) {
        return true;
    }
    @Override
    public int getMaxViewDistance() {
        return 12;
    }
}
