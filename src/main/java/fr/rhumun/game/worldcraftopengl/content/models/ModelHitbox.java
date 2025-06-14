package fr.rhumun.game.worldcraftopengl.content.models;

import fr.rhumun.game.worldcraftopengl.entities.physics.hitbox.Hitbox;
import fr.rhumun.game.worldcraftopengl.worlds.Block;

public interface ModelHitbox {

    Hitbox getHitbox(Block block);

}
