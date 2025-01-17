package fr.rhumun.game.worldcraftopengl.content.materials.opacity;

import fr.rhumun.game.worldcraftopengl.content.Block;

public interface AbstractOpacity {

    boolean isVisibleWith(Block block);

    int getMaxViewDistance();


}
