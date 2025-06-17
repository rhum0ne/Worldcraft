package fr.rhumun.game.worldcraftopengl.entities;

import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;

public class RockyEntity extends MobEntity {
    public RockyEntity(double x, double y, double z, float yaw, float pitch) {
        super(Model.ROCKY, Texture.ROCKY, 1, 0.6f, 2.0f, 1, 1, 2, 0.1f, 1, x, y, z, yaw, pitch);
    }
}
