package fr.rhumun.game.worldcraftopengl.entities;

import fr.rhumun.game.worldcraftopengl.Game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AnimationLoader {

    public record WalkAnimation(float[] times, float[] yValues, float length) {}

    public static WalkAnimation loadWalk(String fileName, String... boneNames) {
        Path path = Path.of(Game.TEXTURES_PATH + "entities/" + fileName);
        try {
            String json = Files.readString(path);
            int walkIdx = json.indexOf("\"name\":\"walk\"");
            if (walkIdx < 0) return null;
            float length = parseFloatAfter(json, "\"length\":", walkIdx);

            int animIdx = json.indexOf("\"animators\":", walkIdx);
            int animStart = json.indexOf('{', animIdx);
            int animEnd = findClosing(json, animStart);
            String animators = json.substring(animStart + 1, animEnd);

            String bone = null;
            for (String bn : boneNames) {
                int nameIdx = animators.indexOf("\"name\":\"" + bn + "\"");
                if (nameIdx >= 0) {
                    int bStart = animators.lastIndexOf('{', nameIdx);
                    int bEnd = findClosing(animators, bStart);
                    bone = animators.substring(bStart, bEnd + 1);
                    break;
                }
            }
            if (bone == null) return null;

            List<Float> times = new ArrayList<>();
            List<Float> ys = new ArrayList<>();
            int pos = 0;
            while (true) {
                int keyIdx = bone.indexOf("\"channel\":\"position\"", pos);
                if (keyIdx < 0) break;
                float y = parseFloatAfter(bone, "\"y\":", keyIdx);
                float t = parseFloatAfter(bone, "\"time\":", keyIdx);
                ys.add(y);
                times.add(t);
                pos = bone.indexOf('}', keyIdx);
                if (pos < 0) break;
            }
            float[] tArr = new float[times.size()];
            float[] yArr = new float[ys.size()];
            for (int i = 0; i < tArr.length; i++) {
                tArr[i] = times.get(i);
                yArr[i] = ys.get(i);
            }
            return new WalkAnimation(tArr, yArr, length);
        } catch (IOException e) {
            return null;
        }
    }

    private static float parseFloatAfter(String text, String key, int start) {
        int idx = text.indexOf(key, start);
        if (idx < 0) return 0f;
        idx += key.length();
        int end = idx;
        while (end < text.length()) {
            char c = text.charAt(end);
            if ((c >= '0' && c <= '9') || c == '-' || c == '.') {
                end++;
            } else {
                break;
            }
        }
        String num = text.substring(idx, end);
        if (num.startsWith("\"") && num.endsWith("\"")) {
            num = num.substring(1, num.length() - 1);
        }
        if (num.isEmpty()) return 0f;
        return Float.parseFloat(num);
    }

    private static int findClosing(String text, int open) {
        int depth = 0;
        for (int i = open; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '{') depth++;
            else if (c == '}') {
                depth--;
                if (depth == 0) return i;
            }
        }
        return text.length() - 1;
    }
}
