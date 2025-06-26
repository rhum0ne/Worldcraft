package fr.rhumun.game.worldcraftopengl.content.models.entities;

import java.util.List;

public class Animation {
    public final String name;
    public final float duration;
    public final float ticksPerSecond;
    public final List<AnimationChannel<?>> channels;

    public Animation(String name, float duration, float ticksPerSecond, List<AnimationChannel<?>> channels) {
        this.name = name;
        this.duration = duration;
        this.ticksPerSecond = ticksPerSecond == 0 ? 1f : ticksPerSecond;
        this.channels = channels;
    }
}
