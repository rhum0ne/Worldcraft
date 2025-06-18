package fr.rhumun.game.worldcraftopengl.entities;

import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;

public class OtterEntity extends MobEntity {

    public OtterEntity(double x, double y, double z, float yaw, float pitch) {
        super(Model.OTTER, Texture.OTTER, 1, 0.3f, 0.6f, 1, 1, 2, 0.1f, 1, x, y, z, yaw, pitch);
    }
}
