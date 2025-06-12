package fr.rhumun.game.worldcraftopengl.entities.physics.hitbox;

import fr.rhumun.game.worldcraftopengl.entities.Entity;
import fr.rhumun.game.worldcraftopengl.worlds.Block;

public interface Hitbox {
    /**
     * Bounding box relative to the block's lower corner (0-1 range).
     */
    AxisAlignedBB getBoundingBox(Block block);

    /**
     * Bounding box in world coordinates centred on the block position.
     */
    default AxisAlignedBB getWorldBoundingBox(Block block) {
        return getBoundingBox(block).offset(new org.joml.Vector3f(
                block.getX() - 0.5f,
                block.getY(),
                block.getZ() - 0.5f
        ));
    }

    /**
     * Check intersection between the hitbox of a block and an arbitrary
     * bounding box in world coordinates.
     */
    default boolean intersects(AxisAlignedBB box, Block block) {
        return getWorldBoundingBox(block).intersects(box);
    }

    default boolean intersects(Entity entity, Block block) {
        return intersects(entity.getBoundingBox(), block);
    }
}
