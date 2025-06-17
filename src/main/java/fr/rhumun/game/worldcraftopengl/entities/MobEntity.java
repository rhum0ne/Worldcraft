package fr.rhumun.game.worldcraftopengl.entities;

import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;

public abstract class MobEntity extends Entity implements MovingEntity {

    private final int[] movements = new int[3];
    private int moveCooldown = 0;
    private float animationStep = 0f;

    public MobEntity(Model model, Texture texture, int reach, float radius, float height,
                     int walkSpeed, int sneakSpeed, int sprintSpeed, float accelerationByTick,
                     int jumpForce, double x, double y, double z, float yaw, float pitch) {
        super(model, (short) texture.getId(), reach, radius, height, walkSpeed, sneakSpeed,
                sprintSpeed, accelerationByTick, jumpForce, x, y, z, yaw, pitch);
    }

    @Override
    public int[] getMovements() {
        return movements;
    }

    public float getAnimationStep() {
        return animationStep;
    }

    @Override
    public void update() {
        moveCooldown--;
        if (moveCooldown <= 0) {
            moveCooldown = 60;
            movements[0] = movements[2] = 0;
            int dir = (int) (Math.random() * 4);
            switch (dir) {
                case 0 -> movements[0] = 1;
                case 1 -> movements[0] = -1;
                case 2 -> movements[2] = 1;
                case 3 -> movements[2] = -1;
            }
        }

        if (movements[0] != 0 || movements[2] != 0) {
            animationStep += 0.3f;
        }

        super.update();
    }
}
