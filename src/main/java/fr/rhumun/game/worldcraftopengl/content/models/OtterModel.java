package fr.rhumun.game.worldcraftopengl.content.models;

import fr.rhumun.game.worldcraftopengl.worlds.Block;
import org.joml.Vector3f;

import static fr.rhumun.game.worldcraftopengl.content.Model.load;

public class OtterModel extends AbstractModel {

    public OtterModel() {
        super(load("nocsy_otter_v2.obj"), false);
    }

    @Override
    public void setBlockDataOnPlace(Block block, Vector3f hitPosition, Vector3f direction) {
        block.setState(0);
    }
}
