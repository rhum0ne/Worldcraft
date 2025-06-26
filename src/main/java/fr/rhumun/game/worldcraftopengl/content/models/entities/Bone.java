package fr.rhumun.game.worldcraftopengl.content.models.entities;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Bone {
    public final String name;
    public int index;
    public Bone parent;
    public final List<Bone> children = new ArrayList<>();

    private final Vector3f translation = new Vector3f();
    private final Quaternionf rotation = new Quaternionf();
    private final Vector3f scale = new Vector3f(1, 1, 1);

    private final Vector3f bindTranslation = new Vector3f();
    private final Quaternionf bindRotation = new Quaternionf();
    private final Vector3f bindScale = new Vector3f(1, 1, 1);

    public final Matrix4f localTransform = new Matrix4f();
    public final Matrix4f bindTransform = new Matrix4f();
    public final Matrix4f globalTransform = new Matrix4f();
    public final Matrix4f offsetMatrix = new Matrix4f();

    public Bone(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public void setTranslation(Vector3f t) {
        this.translation.set(t);
        recomputeMatrix();
    }

    public void setRotation(Quaternionf r) {
        this.rotation.set(r);
        recomputeMatrix();
    }

    public void setScale(Vector3f s) {
        this.scale.set(s);
        recomputeMatrix();
    }

    public void setBindMatrix(Matrix4f mat) {
        this.bindTransform.set(mat);
        mat.getTranslation(bindTranslation);
        mat.getUnnormalizedRotation(bindRotation);
        mat.getScale(bindScale);
        translation.set(bindTranslation);
        rotation.set(bindRotation);
        scale.set(bindScale);
        recomputeMatrix();
    }

    public void resetToBindPose() {
        translation.set(bindTranslation);
        rotation.set(bindRotation);
        scale.set(bindScale);
        recomputeMatrix();
    }

    public void recomputeMatrix() {
        localTransform.identity()
                .translate(translation)
                .rotate(rotation)
                .scale(scale);
    }

    public void computeGlobalTransformRecursive() {
        if (parent != null)
            globalTransform.set(parent.globalTransform).mul(localTransform);
        else
            globalTransform.set(localTransform);
        for (Bone child : children) {
            child.computeGlobalTransformRecursive();
        }
    }
}
