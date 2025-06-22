package fr.rhumun.game.worldcraftopengl.entities;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.models.entities.Animator;
import fr.rhumun.game.worldcraftopengl.content.models.entities.GltfAnimationLoader;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.entities.physics.Movements;

public class NinjaSkeletonEntity extends MobEntity {
    public NinjaSkeletonEntity(double x, double y, double z, float yaw, float pitch) {
        super(Model.NINJA_SKELETON, Texture.NINJA_SKELETON, 1, 0.4f, 1.8f, 1, 1, 2, 0.1f, 1, x, y, z, yaw, pitch);

        var result = GltfAnimationLoader.load("models\\ninja_skeleton.gltf");
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

        Player player = Game.GAME.getPlayer();
        if (player != null) {
            double dx = player.getLocation().getX() - this.getLocation().getX();
            double dz = player.getLocation().getZ() - this.getLocation().getZ();
            if (dx*dx + dz*dz < 1.5) {
                this.stopMove();
                return;
            }

            float yaw = (float) Math.toDegrees(Math.atan2(dz, dx));
            setYaw(yaw);
            getMovements()[0] = 1;
            getMovements()[2] = 0;
        }

        if(this.hasBlockInViewDirection()) { this. jump();}

        Movements.applyMovements(this);
    }
}
