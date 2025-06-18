package fr.rhumun.game.worldcraftopengl.content.models;

import fr.rhumun.game.worldcraftopengl.entities.physics.hitbox.BoxHitbox;
import fr.rhumun.game.worldcraftopengl.entities.physics.hitbox.Hitbox;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import org.joml.Vector3f;

import static fr.rhumun.game.worldcraftopengl.content.Model.load;

/**
 * Simple trapdoor model with ~2 pixel height.
 */
public class TrapdoorModel extends AbstractModel implements ModelHitbox {

    public TrapdoorModel() {
        super(load("trapdoor.obj"), false);
    }

    @Override
    public void setBlockDataOnPlace(Block block, Vector3f hitPosition, Vector3f direction) {
        block.setState(0);
    }

    @Override
    public Hitbox getHitbox(Block block) {
        if(block.getState() == 0) {
            return new BoxHitbox(0f, 0f, 0f, 1f, 0.125f, 1f);
        }
        return new BoxHitbox(0f, 0.875f, 0f, 1f, 1f, 1f);
    }
}
