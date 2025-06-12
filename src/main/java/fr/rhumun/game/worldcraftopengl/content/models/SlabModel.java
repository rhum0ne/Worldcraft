package fr.rhumun.game.worldcraftopengl.content.models;

import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.entities.physics.hitbox.BoxHitbox;
import fr.rhumun.game.worldcraftopengl.entities.physics.hitbox.Hitbox;
import org.joml.Vector3f;

import static fr.rhumun.game.worldcraftopengl.content.Model.BLOCK;
import static fr.rhumun.game.worldcraftopengl.content.Model.load;

public class SlabModel extends AbstractModel {

    public SlabModel() {
        super(load("slab.obj"), false);
    }

    @Override
    public void setBlockDataOnPlace(Block block, Vector3f hitPosition, Vector3f direction) {
        float y = (float) (hitPosition.y() - Math.floor(hitPosition.y()));
        if(y>0.5f) block.setState(1);
        else if(y<0.5f) block.setState(0);
        else {
            block.setModel(BLOCK);
            BLOCK.setBlockDataOnPlace(block, hitPosition, direction);
        }
    }

    @Override
    public Hitbox getHitbox(Block block) {
        if(block.isOnTheFloor()) {
            return new BoxHitbox(-0.5f, 0f, -0.5f, 0.5f, 0.5f, 0.5f);
        }
        return new BoxHitbox(-0.5f, 0.5f, -0.5f, 0.5f, 1f, 0.5f);
    }
}
