package fr.rhumun.game.worldcraftopengl.content.materials.blocks.types;

import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.worlds.Block;

public interface BreakEvent {
    void onBreak(Block block, Player player);
}
