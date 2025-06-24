package fr.rhumun.game.worldcraftopengl.content.models;

import fr.rhumun.game.worldcraftopengl.entities.physics.hitbox.BoxHitbox;
import fr.rhumun.game.worldcraftopengl.entities.physics.hitbox.Hitbox;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import org.joml.Vector3f;

public class WallModel extends AbstractModel implements ModelHitbox {

    public WallModel() {
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

        block.setState(orientation & 3);
    }

    @Override
    public Hitbox getHitbox(Block block) {
        int orientation = block.getState() & 3;
        if(orientation % 2 == 0) {
            return new BoxHitbox(0f, 0f, 0.25f, 1f, 1f, 0.75f);
        }
        return new BoxHitbox(0.25f, 0f, 0f, 0.75f, 1f, 1f);
    }
}
