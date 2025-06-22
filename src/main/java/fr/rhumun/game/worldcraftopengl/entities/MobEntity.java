package fr.rhumun.game.worldcraftopengl.entities;

import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.models.entities.Animator;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public abstract class MobEntity extends Entity implements MovingEntity {

    private final int[] movements = new int[3];
    private int moveCooldown = 0;
    private int attackCooldown = 0;
    private Animator animator;

    public MobEntity(Model model, Texture texture, int reach, float radius, float height,
                     int walkSpeed, int sneakSpeed, int sprintSpeed, float accelerationByTick,
                     int jumpForce, double x, double y, double z, float yaw, float pitch) {
        super(model, (short) texture.getId(), reach, radius, height, walkSpeed, sneakSpeed,
                sprintSpeed, accelerationByTick, jumpForce, x, y, z, yaw, pitch);

        // L'Animator devra être initialisé par la classe concrète
    }

    @Override
    public int[] getMovements() {
        return movements;
    }

    @Override
    public boolean isAnimated() {
        return true;
    }

    @Override
    public void update() {
        if (attackCooldown > 0) attackCooldown--;
        this.move();
        super.update();
    }

    public void move(){
        this.randomMove();
    }

    protected void randomMove() {
        movements[0] = movements[2] = 0;
        int dir = (int) (Math.random() * 4);
        switch (dir) {
            case 0 -> movements[0] = 1;
            case 1 -> movements[0] = -1;
            case 2 -> movements[2] = 1;
            case 3 -> movements[2] = -1;
        }
    }

    protected void stopMove() {
        movements[0] = movements[2] = 0;
    }
}
