package fr.rhumun.game.worldcraftopengl.content.models.entities;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

public record Keyframe<T>(float time, T value) {

    public static Vector3f interpolateVec3(List<Keyframe<Vector3f>> keyframes, float time) {
        if (keyframes.isEmpty()) return new Vector3f();
        if (keyframes.size() == 1) return new Vector3f(keyframes.get(0).value);

        for (int i = 0; i < keyframes.size() - 1; i++) {
            Keyframe<Vector3f> a = keyframes.get(i);
            Keyframe<Vector3f> b = keyframes.get(i + 1);
            if (time >= a.time && time <= b.time) {
                float t = (time - a.time) / (b.time - a.time);
                return new Vector3f(a.value).lerp(b.value, t);
            }
        }

        return new Vector3f(keyframes.get(keyframes.size() - 1).value);
    }

    public static Quaternionf interpolateQuat(List<Keyframe<Quaternionf>> keyframes, float time) {
        if (keyframes.isEmpty()) return new Quaternionf();
        if (keyframes.size() == 1) return new Quaternionf(keyframes.get(0).value);

        for (int i = 0; i < keyframes.size() - 1; i++) {
            Keyframe<Quaternionf> a = keyframes.get(i);
            Keyframe<Quaternionf> b = keyframes.get(i + 1);
            if (time >= a.time && time <= b.time) {
                float t = (time - a.time) / (b.time - a.time);
                return new Quaternionf(a.value).slerp(b.value, t);
            }
        }

        return new Quaternionf(keyframes.get(keyframes.size() - 1).value);
    }

}
