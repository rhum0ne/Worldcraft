package fr.rhumun.game.worldcraftopengl.content.models;

import fr.rhumun.game.worldcraftopengl.content.Mesh;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import org.joml.Vector3f;

import static fr.rhumun.game.worldcraftopengl.content.Model.load;

public class NinjaSkeletonModel extends AbstractModel {
    public NinjaSkeletonModel() {
        super(Mesh.loadSkinnedMesh("models\\ninja_skeleton.gltf"), false);
    }

    @Override
    public void setBlockDataOnPlace(Block block, Vector3f hitPosition, Vector3f direction) {
        block.setState(0);
    }
}
