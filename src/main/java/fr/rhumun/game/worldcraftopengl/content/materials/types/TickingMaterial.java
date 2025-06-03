package fr.rhumun.game.worldcraftopengl.content.materials.types;

import fr.rhumun.game.worldcraftopengl.worlds.Block;

public interface TickingMaterial {
    void tick(final Block block);
}
