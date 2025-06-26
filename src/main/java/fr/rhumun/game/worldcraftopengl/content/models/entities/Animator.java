package fr.rhumun.game.worldcraftopengl.content.models.entities;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.Shader;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static fr.rhumun.game.worldcraftopengl.content.models.entities.Keyframe.interpolateQuat;
import static fr.rhumun.game.worldcraftopengl.content.models.entities.Keyframe.interpolateVec3;

public class Animator {
    private final Map<String, Bone> bones;
    private final Map<String, Animation> animations;
    private final List<Bone> rootBones;
    private Animation currentAnimation;
    private float animationTime = 0f;
    private float animationSpeed = 1f;

    public Animator(Map<String, Bone> bones, Map<String, Animation> animations) {
        this.bones = bones;
        this.animations = new LinkedHashMap<>(animations);
        if (!animations.isEmpty()) {
            this.currentAnimation = animations.values().iterator().next();
        }
        List<Bone> roots = new java.util.ArrayList<>();
        for (Bone b : bones.values()) {
            if (b.parent == null) roots.add(b);
        }
        this.rootBones = roots;
    }

    public void play(String name) {
        Animation anim = animations.get(name);
        if (anim != null && anim != currentAnimation) {
            currentAnimation = anim;
            animationTime = 0f;
        }
    }

    public void update(float deltaTime) {
        if (currentAnimation == null) return;
        animationTime += deltaTime * animationSpeed;
        float durationSec = currentAnimation.duration / currentAnimation.ticksPerSecond;
        float localTime = animationTime % durationSec;
        applyAnimation(currentAnimation, localTime * currentAnimation.ticksPerSecond);
    }

    private void applyAnimation(Animation animation, float time) {
        for (Bone b : bones.values()) {
            b.resetToBindPose();
        }

        for (AnimationChannel<?> channel : animation.channels) {
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

        for (Bone root : rootBones) root.computeGlobalTransformRecursive();
    }

    public void sendToShader(Shader shader) {
        for (Bone bone : bones.values()) {
            if (bone.index < 0) continue;
            Matrix4f mat = new Matrix4f(bone.globalTransform).mul(bone.offsetMatrix);
            shader.setUniform("boneMatrices[" + bone.index + "]", mat);
        }
    }
}
