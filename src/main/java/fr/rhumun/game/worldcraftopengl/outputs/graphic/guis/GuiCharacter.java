package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis;

import lombok.Getter;

import java.util.HashMap;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

@Getter
public class GuiCharacter {

    private final int textureID;  // ID de la texture de l'atlas
    private final int width;
    private final int height;
    private final float xOffset;
    private final float yOffset;
    private final float advance;

    private static HashMap<Character, GuiCharacter> characters = new HashMap<>();

    public static GuiCharacter get(char c) {
        return characters.get(c);
    }

    public GuiCharacter(char c, int textureID, int width, int height, float xOffset, float yOffset, float advance) {
        this.textureID = textureID;
        this.width = width;
        this.height = height;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.advance = advance;

        GAME.debug("Adding font char " + c + " -> Array texture ID: " + textureID);

        characters.put(c, this);
    }
}
