package fr.rhumun.game.worldcraftopengl.content.models;

import fr.rhumun.game.worldcraftopengl.worlds.Block;
import org.joml.Vector3f;

import static fr.rhumun.game.worldcraftopengl.content.Model.BLOCK;
import static fr.rhumun.game.worldcraftopengl.content.Model.load;

public class SlabModel extends AbstractModel {

    public SlabModel() {
        super(load("slab.obj"), false, true);
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
}
