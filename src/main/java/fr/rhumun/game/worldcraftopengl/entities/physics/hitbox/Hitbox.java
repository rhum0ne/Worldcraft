package fr.rhumun.game.worldcraftopengl.entities.physics.hitbox;

import fr.rhumun.game.worldcraftopengl.entities.Entity;
import fr.rhumun.game.worldcraftopengl.worlds.Block;

public interface Hitbox {
    AxisAlignedBB getBoundingBox(Block block);

    default boolean intersects(Entity entity, Block block) {
        AxisAlignedBB blockBox = getBoundingBox(block).offset(
                new org.joml.Vector3f(block.getX(), block.getY(), block.getZ()));
        AxisAlignedBB entityBox = entity.getBoundingBox();
        return blockBox.intersects(entityBox);
    }
}
