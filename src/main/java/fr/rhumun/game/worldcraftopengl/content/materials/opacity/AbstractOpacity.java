package fr.rhumun.game.worldcraftopengl.content.materials.opacity;

import fr.rhumun.game.worldcraftopengl.worlds.Block;

public interface AbstractOpacity {

    boolean isVisibleWith(Block block);

    int getMaxViewDistance();


}
