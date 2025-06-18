package fr.rhumun.game.worldcraftopengl.content.models.entities;

import java.util.List;

public class AnimationChannel<T> {
    public final String targetNodeName;
    public final String path; // "translation", "rotation", "scale"
    public final List<Keyframe<T>> keyframes;

    public AnimationChannel(String targetNodeName, String path, List<Keyframe<T>> keyframes) {
        this.targetNodeName = targetNodeName;
        this.path = path;
        this.keyframes = keyframes;
    }
}
