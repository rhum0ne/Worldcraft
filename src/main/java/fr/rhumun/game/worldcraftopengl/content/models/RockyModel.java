package fr.rhumun.game.worldcraftopengl.content.models;

import fr.rhumun.game.worldcraftopengl.worlds.Block;
import org.joml.Vector3f;

import static fr.rhumun.game.worldcraftopengl.content.Model.loadGltf;

public class RockyModel extends AnimatedModel {
    public RockyModel() {
        super(loadGltf("Rocky.gltf"));
    }

    @Override
    public void setBlockDataOnPlace(Block block, Vector3f hitPosition, Vector3f direction) {
        block.setState(0);
    }
}
