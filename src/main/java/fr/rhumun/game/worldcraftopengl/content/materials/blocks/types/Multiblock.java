package fr.rhumun.game.worldcraftopengl.content.materials.blocks.types;

import fr.rhumun.game.worldcraftopengl.worlds.Block;

/**
 * Interface for materials that occupy multiple blocks when placed.
 */
public interface Multiblock {
    /**
     * Called after the base block is placed to create additional blocks.
     * @param block base block that was placed
     */
    void onPlace(Block block);
}
