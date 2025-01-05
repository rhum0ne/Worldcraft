package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis;

import lombok.Getter;

import java.util.HashMap;

@Getter
public class GuiCharacter {

    private final int atlasTextureID;  // ID de la texture de l'atlas
    private final int width;
    private final int height;
    private final float xOffset;
    private final float yOffset;
    private final float advance;
    private final float xStart, yStart, xEnd, yEnd;  // Coordonnées dans l'atlas

    private static HashMap<Character, GuiCharacter> characters = new HashMap<>();

    public static GuiCharacter get(char c) {
        return characters.get(c);
    }

    public GuiCharacter(char c, int atlasTextureID, float xStart, float yStart, float xEnd, float yEnd,
                        int width, int height, float xOffset, float yOffset, float advance) {
        this.atlasTextureID = atlasTextureID;
        this.width = width;
        this.height = height;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.advance = advance;

        this.xStart = xStart;
        this.yStart = yStart;
        this.xEnd = xEnd;
        this.yEnd = yEnd;

        System.out.println("Adding font char " + c + " -> Atlas texture ID: " + atlasTextureID);

        characters.put(c, this);
    }
}
