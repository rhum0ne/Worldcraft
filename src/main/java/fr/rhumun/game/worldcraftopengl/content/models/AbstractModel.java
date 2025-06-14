package fr.rhumun.game.worldcraftopengl.content.models;

import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.content.Mesh;
import fr.rhumun.game.worldcraftopengl.entities.physics.hitbox.BoxHitbox;
import fr.rhumun.game.worldcraftopengl.entities.physics.hitbox.Hitbox;
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
