package fr.rhumun.game.worldcraftopengl.content.models.entities;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.Shader;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;

import static fr.rhumun.game.worldcraftopengl.content.models.entities.Keyframe.interpolateQuat;
import static fr.rhumun.game.worldcraftopengl.content.models.entities.Keyframe.interpolateVec3;

public class Animator {
    private final Map<String, Bone> bones;
    private final Bone[] boneArray;
    private final List<AnimationChannel<?>> channels;
    private float animationTime = 0f;
    private float animationSpeed = 1f;

    public Animator(Map<String, Bone> bones, List<AnimationChannel<?>> channels) {
        this.bones = bones;
        this.channels = channels;
        this.boneArray = new Bone[bones.size()];
        for (Bone b : bones.values()) {
            this.boneArray[b.index] = b;
        }
    }

    public Bone[] getBones() {
        return boneArray;
    }

    public void update(float deltaTime) {
        animationTime += deltaTime * animationSpeed;
        applyAnimation(animationTime);
    }

    public void applyAnimation(float time) {
        for (AnimationChannel<?> channel : channels) {
            Bone bone = bones.get(channel.targetNodeName);
            if (bone == null) continue;

            switch (channel.path) {
                case "translation" -> {
                    @SuppressWarnings("unchecked")
                    List<Keyframe<Vector3f>> keyframes = (List<Keyframe<Vector3f>>) (List<?>) channel.keyframes;
                    bone.setTranslation(interpolateVec3(keyframes, time));
                }
                case "rotation" -> {
                    @SuppressWarnings("unchecked")
                    List<Keyframe<Quaternionf>> keyframes = (List<Keyframe<Quaternionf>>) (List<?>) channel.keyframes;
                    bone.setRotation(interpolateQuat(keyframes, time));
                }
                case "scale" -> {
                    @SuppressWarnings("unchecked")
                    List<Keyframe<Vector3f>> keyframes = (List<Keyframe<Vector3f>>) (List<?>) channel.keyframes;
                    bone.setScale(interpolateVec3(keyframes, time));
                }
            }
        }

        for (Bone b : bones.values()) b.recomputeMatrix();
        for (Bone b : bones.values()) b.computeGlobalTransform();
    }

    public void sendToShader(Shader shader) {
        sendToShader(shader, new Matrix4f().identity());
    }

    public void sendToShader(Shader shader, Matrix4f transform) {
        for (Bone bone : boneArray) {
            Matrix4f mat = new Matrix4f(transform).mul(bone.globalTransform);
            shader.setUniform("boneMatrices[" + bone.index + "]", mat);
        }
    }
}
