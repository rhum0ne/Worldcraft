package fr.rhumun.game.worldcraftopengl.entities;

import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.models.entities.Animator;
import fr.rhumun.game.worldcraftopengl.content.models.entities.GltfAnimationLoader;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;

public class RockyEntity extends MobEntity {
    public RockyEntity(double x, double y, double z, float yaw, float pitch) {
        super(Model.ROCKY, Texture.ROCKY, 1, 0.6f, 2.0f, 1, 1, 2, 0.1f, 1, x, y, z, yaw, pitch);

        var result = GltfAnimationLoader.load("models\\Rocky.gltf");
        if (result != null) {
            this.setAnimator(new Animator(result.bones(), result.channels()));
        }
    }

    @Override
    public boolean isAnimated() {
        return true;
    }

    @Override
    public void update() {
        if (getAnimator() != null) getAnimator().update(1f / 60f);
        super.update();
    }
}
