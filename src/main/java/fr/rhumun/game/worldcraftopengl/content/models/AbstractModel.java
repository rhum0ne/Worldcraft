package fr.rhumun.game.worldcraftopengl.content.models;

import fr.rhumun.game.worldcraftopengl.content.Block;
import fr.rhumun.game.worldcraftopengl.content.Mesh;
import lombok.Getter;
import org.joml.Vector3f;

@Getter
public abstract class AbstractModel {

    private final Mesh model;
    private final boolean isOpaque;
    private final int maxChunkDistance;

    public AbstractModel(Mesh model, boolean isOpaque) {
        this(model, isOpaque, -1);
    }

    public AbstractModel(Mesh model, boolean isOpaque, int maxChunkDistance) {
        this.model = model;
        this.isOpaque = isOpaque;
        this.maxChunkDistance = maxChunkDistance;
    }

    public abstract void setBlockDataOnPlace(Block block, Vector3f hitPosition, Vector3f direction);
}
