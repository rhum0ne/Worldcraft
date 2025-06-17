package fr.rhumun.game.worldcraftopengl.entities;

import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;

public class NinjaSkeletonEntity extends MobEntity {
    public NinjaSkeletonEntity(double x, double y, double z, float yaw, float pitch) {
        super(Model.NINJA_SKELETON, Texture.NINJA_SKELETON, 1, 0.4f, 1.8f, 1, 1, 2, 0.1f, 1, x, y, z, yaw, pitch);
    }
}
