package fr.rhumun.game.worldcraftopengl.entities;

import fr.rhumun.game.worldcraftopengl.content.models.AnimatedModel;
import org.joml.Matrix4f;

/**
 * Simple animator that iterates over animations of an AnimatedModel and
 * produces bone transformation matrices.
 */
public class Animator {

    private final AnimatedModel model;
    private float time = 0f;

    public Animator(AnimatedModel model) {
        this.model = model;
    }

    public void update(float delta) {
        // TODO Update time and compute bone matrices by interpolating keyframes
        time += delta;
    }

    public Matrix4f[] getBoneMatrices() {
        int count = model.getAnimatedMesh().getBoneCount();
        Matrix4f[] mats = new Matrix4f[count];
        for (int i = 0; i < count; i++) {
            mats[i] = new Matrix4f();
        }
        // TODO Fill matrices with actual animation data
        return mats;
    }
}
