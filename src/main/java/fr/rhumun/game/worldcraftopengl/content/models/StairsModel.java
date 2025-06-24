package fr.rhumun.game.worldcraftopengl.content.models;

import fr.rhumun.game.worldcraftopengl.entities.physics.hitbox.BoxHitbox;
import fr.rhumun.game.worldcraftopengl.entities.physics.hitbox.Hitbox;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import org.joml.Vector3f;

public class StairsModel extends AbstractModel implements ModelMultiHitbox {

    public StairsModel() {
        super(null, true);
    }

    @Override
    public void setBlockDataOnPlace(Block block, Vector3f hitPosition, Vector3f direction) {
        float absX = Math.abs(direction.x);
        float absZ = Math.abs(direction.z);

        int orientation;
        if (absX > absZ) {
            orientation = direction.x > 0 ? 1 : 3;
        } else {
            orientation = direction.z > 0 ? 0 : 2;
        }

        boolean top = (hitPosition.y - Math.floor(hitPosition.y)) > 0.5f;
        block.setState((orientation & 3) | (top ? 4 : 0));
    }

    @Override
    public Hitbox[] getHitboxes(Block block) {
        int state = block.getState();
        boolean top = (state & 4) != 0;
        int orientation = state & 3;

        Hitbox bottom = new BoxHitbox(0f, top ? 0.5f : 0f, 0f, 1f, top ? 1f : 0.5f, 1f);
        Hitbox step;
        switch (orientation) {
            case 0 -> step = new BoxHitbox(0f, top ? 0f : 0.5f, 0f, 1f, top ? 0.5f : 1f, 0.5f);
            case 1 -> step = new BoxHitbox(0f, top ? 0f : 0.5f, 0f, 0.5f, top ? 0.5f : 1f, 1f);
            case 2 -> step = new BoxHitbox(0f, top ? 0f : 0.5f, 0.5f, 1f, top ? 0.5f : 1f, 1f);
            case 3 -> step = new BoxHitbox(0.5f, top ? 0f : 0.5f, 0f, 1f, top ? 0.5f : 1f, 1f);
            default -> step = BoxHitbox.fullBlock();
        }
        return new Hitbox[]{bottom, step};
    }
}
