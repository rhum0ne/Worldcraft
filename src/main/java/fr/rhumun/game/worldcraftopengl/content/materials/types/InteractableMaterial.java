package fr.rhumun.game.worldcraftopengl.content.materials.types;

import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.worlds.Block;

public interface InteractableMaterial {
    void interact(Player player, Block block);
}
