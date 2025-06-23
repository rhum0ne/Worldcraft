package fr.rhumun.game.worldcraftopengl.worlds.utils;

import fr.rhumun.game.worldcraftopengl.worlds.World;
import javafx.scene.paint.Color;

public final class DayNightCycle {
    private DayNightCycle() {}

    public static String ticksToTime(int ticks) {
        int normalized = normalize(ticks);
        int hours = (int) ((normalized / (float) World.DAY_NIGHT_TICKS) * 24);
        int minutes = (int) ((normalized / (float) World.DAY_NIGHT_TICKS) * 24 * 60) % 60;
        return String.format("%02d:%02d", hours, minutes);
    }

    public static int timeToTicks(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int totalMinutes = hours * 60 + minutes;
        return (int) ((totalMinutes / (24f * 60)) * World.DAY_NIGHT_TICKS);
    }

    public static Color getSkyColor(int ticks) {
        float angle = getAngle(ticks);
        double factor = (Math.sin(Math.toRadians(angle)) + 1) / 2.0;
        Color day = Color.rgb(77, 150, 230);
        Color night = Color.BLACK;
        return interpolate(night, day, (float) factor);
    }

    public static Color getLightColor(int ticks) {
        float angle = getAngle(ticks) % 360f;

        double brightness = Math.max(0.0, Math.sin(Math.toRadians(angle)));
        Color day = Color.rgb(255, 244, 232);
        Color night = Color.rgb(20, 30, 70);
        Color base = interpolate(night, day, (float) brightness);

        float d = Math.min(Math.min(Math.abs(angle), Math.abs(angle - 180f)), Math.abs(angle - 360f));
        float twilightFactor = Math.max(0f, 1f - d / 30f);
        Color twilight = Color.rgb(255, 128, 40);

        return interpolate(base, twilight, twilightFactor);
    }

    public static float getAngle(int ticks) {
        int normalized = normalize(ticks);
        return (normalized / (float) World.DAY_NIGHT_TICKS) * 360f;
    }

    private static int normalize(int ticks) {
        int mod = ticks % World.DAY_NIGHT_TICKS;
        return mod < 0 ? mod + World.DAY_NIGHT_TICKS : mod;
    }

    private static Color interpolate(Color start, Color end, float t) {
        t = Math.min(1f, Math.max(0f, t));
        double r = start.getRed() * (1 - t) + end.getRed() * t;
        double g = start.getGreen() * (1 - t) + end.getGreen() * t;
        double b = start.getBlue() * (1 - t) + end.getBlue() * t;
        return new Color(r, g, b, 1.0);
    }
}