package fr.rhumun.game.worldcraftopengl.worlds.utils;

import fr.rhumun.game.worldcraftopengl.worlds.World;
import javafx.scene.paint.Color;

/**
 * Utility handling world time and sky color based on a day/night cycle.
 */
public class DayNightCycle {

    private final World world;
    private int tick;

    public DayNightCycle(World world) {
        this.world = world;
    }

    /** Update the cycle and world sky color. */
    public void update() {
        tick = (tick + 1) % World.DAY_NIGHT_TICKS;
        world.setSkyColor(computeSkyColor());
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = ((tick % World.DAY_NIGHT_TICKS) + World.DAY_NIGHT_TICKS) % World.DAY_NIGHT_TICKS;
        world.setSkyColor(computeSkyColor());
    }

    /**
     * Convert tick count to an HH:mm formatted time.
     */
    public static String tickToTime(int tick) {
        int minutes = (int) ((long) tick * 24 * 60 / World.DAY_NIGHT_TICKS);
        int hour = (minutes / 60) % 24;
        int minute = minutes % 60;
        return String.format("%02d:%02d", hour, minute);
    }

    /**
     * Convert an HH:mm formatted time to ticks.
     */
    public static int timeToTick(String time) {
        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        int total = hour * 60 + minute;
        return (int) ((long) total * World.DAY_NIGHT_TICKS / (24 * 60));
    }

    private Color computeSkyColor() {
        Color day = Color.rgb(77, 150, 230);
        double progress = (double) tick / World.DAY_NIGHT_TICKS;
        double t = 0.5 * (1 - Math.cos(progress * 2 * Math.PI));
        return Color.color(day.getRed() * t, day.getGreen() * t, day.getBlue() * t);
    }
}
