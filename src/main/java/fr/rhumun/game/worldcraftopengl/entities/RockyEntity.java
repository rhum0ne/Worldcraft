package fr.rhumun.game.worldcraftopengl.entities;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.models.entities.Animator;
import fr.rhumun.game.worldcraftopengl.content.models.entities.GltfAnimationLoader;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.entities.NinjaSkeletonEntity;
import fr.rhumun.game.worldcraftopengl.entities.physics.Movements;

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
    public void move() {
        if (getAnimator() != null) getAnimator().update(1f / 60f);

        var world = Game.GAME.getWorld();
        NinjaSkeletonEntity target = null;
        double best = Double.MAX_VALUE;
        for (var e : world.getEntities()) {
            if (e instanceof NinjaSkeletonEntity skel) {
                double dx = skel.getLocation().getX() - this.getLocation().getX();
                double dz = skel.getLocation().getZ() - this.getLocation().getZ();
                double dist2 = dx * dx + dz * dz;
                if (dist2 < best) {
                    best = dist2;
                    target = skel;
                }
            }
        }

        if (target != null) {
            double dx = target.getLocation().getX() - this.getLocation().getX();
            double dz = target.getLocation().getZ() - this.getLocation().getZ();
            float yaw = (float) Math.toDegrees(Math.atan2(dz, dx));
            setYaw(yaw);
            getMovements()[0] = 1;
            getMovements()[2] = 0;
        }

        Movements.applyMovements(this);
    }
}
