package fr.rhumun.game.worldcraftopengl.blocks.materials.types;

import fr.rhumun.game.worldcraftopengl.Player;
import fr.rhumun.game.worldcraftopengl.blocks.Block;

public interface InteractableMaterial {
    void interact(Player player, Block block);
}
