package fr.rhumun.game.worldcraftopengl.content.models;

import fr.rhumun.game.worldcraftopengl.content.AnimatedMesh;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import org.joml.Vector3f;

/**
 * Base class for models loaded from glTF with skeletal animation support.
 */
public class AnimatedModel extends AbstractModel {

    private final AnimatedMesh animatedMesh;

    public AnimatedModel(AnimatedMesh mesh) {
        super(mesh, false);
        this.animatedMesh = mesh;
    }

    public AnimatedMesh getAnimatedMesh() {
        return animatedMesh;
    }

    @Override
    public void setBlockDataOnPlace(Block block, Vector3f hitPosition, Vector3f direction) {
        block.setState(0);
    }
}
