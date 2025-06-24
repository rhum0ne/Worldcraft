package fr.rhumun.game.worldcraftopengl.content.models;

import fr.rhumun.game.worldcraftopengl.entities.physics.hitbox.BoxHitbox;
import fr.rhumun.game.worldcraftopengl.entities.physics.hitbox.Hitbox;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import org.joml.Vector3f;

/**
 * Simple thin door occupying one block with 2px thickness.
 */
public class DoorModel extends AbstractModel implements ModelHitbox {

    public DoorModel() {
        super(null, true);
    }

    @Override
    public void setBlockDataOnPlace(Block block, Vector3f hitPosition, Vector3f direction) {
        float absX = Math.abs(direction.x);
        float absZ = Math.abs(direction.z);
        int orientation;
        if(absX > absZ) {
            orientation = direction.x > 0 ? 1 : 3;
        } else {
            orientation = direction.z > 0 ? 0 : 2;
        }
        int open = block.getState() & 4;
        block.setState((orientation & 3) | open);
    }

    @Override
    public Hitbox getHitbox(Block block) {
        int state = block.getState();
        if((state & 4) != 0){
            // disable collisions when the door is opened
            return new BoxHitbox(2f, 2f, 2f, 2f, 2f, 2f);
        }

        int orientation = state & 3;
        float thick = 0.125f; // 2px
        return switch (orientation) {
            case 0 -> new BoxHitbox(0f, 0f, 0f, thick, 1f, 1f);
            case 1 -> new BoxHitbox(0f, 0f, 0f, 1f, 1f, thick);
            case 2 -> new BoxHitbox(1f - thick, 0f, 0f, 1f, 1f, 1f);
            case 3 -> new BoxHitbox(0f, 0f, 1f - thick, 1f, 1f, 1f);
            default -> BoxHitbox.fullBlock();
        };
    }
}
