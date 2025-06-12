package fr.rhumun.game.worldcraftopengl.entities.physics.hitbox;

import fr.rhumun.game.worldcraftopengl.entities.Entity;
import fr.rhumun.game.worldcraftopengl.worlds.Block;

public interface Hitbox {
    AxisAlignedBB getBoundingBox(Block block);

    /**
     * Check intersection between the hitbox of a block and an arbitrary
     * bounding box in world coordinates.
     */
    default boolean intersects(AxisAlignedBB box, Block block) {
        AxisAlignedBB blockBox = getBoundingBox(block).offset(
                new org.joml.Vector3f(block.getX(), block.getY(), block.getZ()));
        return blockBox.intersects(box);
    }

    default boolean intersects(Entity entity, Block block) {
        return intersects(entity.getBoundingBox(), block);
    }
}
