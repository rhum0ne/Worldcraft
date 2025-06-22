package fr.rhumun.game.worldcraftopengl.entities;

import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LivingEntity extends Entity{

    private static final float FALL_DAMAGE_THRESHOLD = 3f;

    protected int maxHealth = 20;
    protected int health = 20;
    private double fallStartY = 0;

    public LivingEntity(Model model, short textureID, int reach, float radius, float height, int walkSpeed, int sneakSpeed, int sprintSpeed, float accelerationByTick, int jumpForce, double x, double y, double z, float yaw, float pitch) {
        super(model, textureID, reach, radius, height, walkSpeed, sneakSpeed, sprintSpeed, accelerationByTick, jumpForce, x, y, z, yaw, pitch);
    }

    public LivingEntity(int reach, float radius, float height, int walkSpeed, int sneakSpeed, int sprintSpeed, float accelerationByTick, int jumpForce, double x, double y, double z, float yaw, float pitch) {
        super(reach, radius, height, walkSpeed, sneakSpeed, sprintSpeed, accelerationByTick, jumpForce, x, y, z, yaw, pitch);
    }


    public void damage(int amount) {
        if (amount <= 0) return;
        health -= amount;
        if (health < 0) health = 0;
    }

    public void heal(int amount) {
        if (amount <= 0) return;
        health = Math.min(maxHealth, health + amount);
    }

    protected void updateFallDamage() {
        if (isFlying() || isSwimming() || isNoClipping() || (this instanceof Player p && p.isInCreativeMode())) {
            setOnGround(true);
            return;
        }

        boolean ground = hasBlockDown();
        if (ground) {
            if (!isOnGround()) {
                double distance = fallStartY - this.getLocation().getY();
                if (distance > FALL_DAMAGE_THRESHOLD) {
                    int dmg = (int) Math.floor(distance - FALL_DAMAGE_THRESHOLD);
                    damage(dmg);
                }
            }
            setOnGround(true);
        } else {
            if (isOnGround()) {
                fallStartY = this.getLocation().getY();
            }
            setOnGround(false);
        }
    }

    @Override
    public void update() {
        super.update();
        this.updateFallDamage();
    }

    public void attack(LivingEntity target) {
        if (target == null) return;

        target.damage(1);

        float dx = (float) (target.getLocation().getX() - this.getLocation().getX());
        float dz = (float) (target.getLocation().getZ() - this.getLocation().getZ());
        var dir = new org.joml.Vector3f(dx, 0, dz);
        if (dir.lengthSquared() > 0) {
            dir.normalize().mul(0.5f);
            target.getVelocity().add(dir);
        }
    }
}
