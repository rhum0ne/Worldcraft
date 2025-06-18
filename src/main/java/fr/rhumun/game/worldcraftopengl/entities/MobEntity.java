package fr.rhumun.game.worldcraftopengl.entities;

import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.Game;
import java.util.HashMap;
import java.util.Map;

import static fr.rhumun.game.worldcraftopengl.entities.AnimationLoader.*;

public abstract class MobEntity extends Entity implements MovingEntity {

    private static final Map<Model, WalkAnimation> WALK_ANIMATIONS = new HashMap<>();

    static {
        WALK_ANIMATIONS.put(Model.NINJA_SKELETON,
                AnimationLoader.loadWalk("ninja_skeleton.bbmodel", "all"));
        WALK_ANIMATIONS.put(Model.ROCKY,
                AnimationLoader.loadWalk("Rocky.bbmodel", "Body"));
    }

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
        WalkAnimation anim = WALK_ANIMATIONS.get(this.getModel());
        if (anim == null) return 0f;

        float[] times = anim.times();
        float[] values = anim.yValues();
        float length = anim.length();

        float t = animationStep % length;
        for (int i = 1; i < times.length; i++) {
            if (t < times[i]) {
                float dt = times[i] - times[i - 1];
                float prog = (t - times[i - 1]) / (dt <= 0 ? 1 : dt);
                return values[i - 1] + prog * (values[i] - values[i - 1]);
            }
        }
        return values[values.length - 1];
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
