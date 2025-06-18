package fr.rhumun.game.worldcraftopengl.entities;

import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.entities.MobAnimations;

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

    public float getAnimationOffset(){
        float[] times;
        float[] values;
        float length;
        switch (this.getModel()) {
            case NINJA_SKELETON -> {
                times = MobAnimations.NINJA_WALK_TIMES;
                values = MobAnimations.NINJA_WALK_Y;
                length = MobAnimations.NINJA_WALK_LENGTH;
            }
            case ROCKY -> {
                times = MobAnimations.ROCKY_WALK_TIMES;
                values = MobAnimations.ROCKY_WALK_Y;
                length = MobAnimations.ROCKY_WALK_LENGTH;
            }
            default -> { return 0f; }
        }

        float t = animationStep % length;
        for(int i=1;i<times.length;i++){
            if(t < times[i]){
                float dt = times[i] - times[i-1];
                float prog = (t - times[i-1]) / (dt<=0?1:dt);
                return values[i-1] + prog * (values[i]-values[i-1]);
            }
        }
        return values[values.length-1];
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
