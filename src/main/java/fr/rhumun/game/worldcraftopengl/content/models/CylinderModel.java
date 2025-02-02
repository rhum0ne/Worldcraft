package fr.rhumun.game.worldcraftopengl.content.models;

import fr.rhumun.game.worldcraftopengl.content.Block;
import org.joml.Vector3f;

import static fr.rhumun.game.worldcraftopengl.content.Model.load;

public class CylinderModel extends AbstractModel {

    public CylinderModel() {
        super(load("cylinder.obj"), false);
    }

    @Override
    public void setBlockDataOnPlace(Block block, Vector3f hitPosition, Vector3f direction) {
        block.setState(0);
    }
}
