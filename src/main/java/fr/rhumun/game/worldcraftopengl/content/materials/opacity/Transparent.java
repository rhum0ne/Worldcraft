package fr.rhumun.game.worldcraftopengl.content.materials.opacity;

import fr.rhumun.game.worldcraftopengl.content.Block;

public class Transparent implements AbstractOpacity {
    @Override
    public boolean isVisibleWith(Block block) {
        return true;
    }
}
