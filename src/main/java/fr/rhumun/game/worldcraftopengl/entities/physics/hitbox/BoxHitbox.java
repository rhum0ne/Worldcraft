package fr.rhumun.game.worldcraftopengl.entities.physics.hitbox;

import fr.rhumun.game.worldcraftopengl.worlds.Block;

/** Simple box hitbox defined in block coordinates (0-1 range). */
public class BoxHitbox implements Hitbox {
    private final AxisAlignedBB box;

    public BoxHitbox(float minX, float minY, float minZ,
                     float maxX, float maxY, float maxZ) {
        this.box = new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public static BoxHitbox fullBlock() {
        return new BoxHitbox(0f, 0f, 0f, 1f, 1f, 1f);
    }

    @Override
    public AxisAlignedBB getBoundingBox(Block block) {
        return box;
    }
}
