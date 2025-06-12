package fr.rhumun.game.worldcraftopengl.entities.physics.hitbox;

import org.joml.Vector3f;

/** Simple axis aligned bounding box */
public class AxisAlignedBB {
    public final float minX, minY, minZ;
    public final float maxX, maxY, maxZ;

    public AxisAlignedBB(float minX, float minY, float minZ,
                         float maxX, float maxY, float maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public boolean intersects(AxisAlignedBB other) {
        return this.maxX > other.minX && this.minX < other.maxX &&
               this.maxY > other.minY && this.minY < other.maxY &&
               this.maxZ > other.minZ && this.minZ < other.maxZ;
    }

    public AxisAlignedBB offset(Vector3f offset) {
        return new AxisAlignedBB(
                minX + offset.x, minY + offset.y, minZ + offset.z,
                maxX + offset.x, maxY + offset.y, maxZ + offset.z
        );
    }
}
