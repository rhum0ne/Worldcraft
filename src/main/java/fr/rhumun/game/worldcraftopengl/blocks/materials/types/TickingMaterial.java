package fr.rhumun.game.worldcraftopengl.blocks.materials.types;

import fr.rhumun.game.worldcraftopengl.Player;
import fr.rhumun.game.worldcraftopengl.blocks.Block;

public interface TickingMaterial {
    void tick(final Block block);
}
