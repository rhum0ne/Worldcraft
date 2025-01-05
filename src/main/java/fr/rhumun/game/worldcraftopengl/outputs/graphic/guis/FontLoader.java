package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.ShaderUtils;
import lombok.Getter;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBTruetype.*;

public class FontLoader {

    private ByteBuffer fontBuffer;
    private STBTTFontinfo font;
    private int fontSize = 32;
    @Getter
    private int atlasNum;

    public FontLoader(String fontPath) throws Exception {
        // Charger la police TTF dans un ByteBuffer
        byte[] fontBytes = Files.readAllBytes(Paths.get(fontPath));
        fontBuffer = ByteBuffer.allocateDirect(fontBytes.length);
        fontBuffer.put(fontBytes).flip();

        font = new STBTTFontinfo(fontBuffer);
        STBTruetype.stbtt_InitFont(font, fontBuffer);
    }

    public void loadFont() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Exemple de création de l'atlas
            int atlasWidth = 1024;
            int atlasHeight = 1024;

            int atlasTextureID = glGenTextures();
            this.atlasNum = atlasTextureID;
            glBindTexture(GL_TEXTURE_2D, atlasTextureID);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, atlasWidth, atlasHeight, 0, GL_RED, GL_UNSIGNED_BYTE, (ByteBuffer) null);

// Maintenant, insérez les glyphes dans l'atlas
            for (char c = 32; c < 128; c++) {
                if(c==' ') continue;
                // Récupérer les métriques du glyphe
                IntBuffer width = stack.mallocInt(1);
                IntBuffer height = stack.mallocInt(1);
                IntBuffer xoff = stack.mallocInt(1);
                IntBuffer yoff = stack.mallocInt(1);
                IntBuffer advance = stack.mallocInt(1);
                IntBuffer leftSideBearing = stack.mallocInt(1);  // Buffer alloué ici

                stbtt_GetCodepointHMetrics(font, c, advance, leftSideBearing);
                float scale = stbtt_ScaleForPixelHeight(font, fontSize);

                // Bitmap du glyphe
                ByteBuffer bitmap = stbtt_GetCodepointBitmap(font, scale, scale, c, width, height, xoff, yoff);

                // Positionner le glyphe dans l'atlas
                int x = (c - 32) % 16 * 32;  // Exemple de calcul des positions (simple grille)
                int y = (c - 32) / 16 * 32;
                glTexSubImage2D(GL_TEXTURE_2D, 0, x, y, width.get(0), height.get(0), GL_RED, GL_UNSIGNED_BYTE, bitmap);

                // Stocker les coordonnées de texture et autres informations pour chaque glyphe
                float xStart = (float) x / atlasWidth;
                float yStart = (float) y / atlasHeight;
                float xEnd = (float) (x + width.get(0)) / atlasWidth;
                float yEnd = (float) (y + height.get(0)) / atlasHeight;

                // Ajouter le glyphe à votre liste
                new GuiCharacter(c, atlasTextureID, xStart, yStart, xEnd, yEnd, width.get(0), height.get(0), xoff.get(0), yoff.get(0), advance.get(0));

                // Libérer la mémoire du bitmap
                stbtt_FreeBitmap(bitmap);
            }

        }
    }

    private int createGlyphTexture(ByteBuffer bitmap, int width, int height) {
        int textureID = glGenTextures();  // Générer un ID de texture OpenGL
        glActiveTexture(textureID); // Active l'unité de texture correspondante
        glBindTexture(GL_TEXTURE_2D, textureID);

        // Remplir la texture avec les données du bitmap
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, width, height, 0, GL_RED, GL_UNSIGNED_BYTE, bitmap);
        glGenerateMipmap(GL_TEXTURE_2D);
        // Paramètres de la texture OpenGL
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        // Vous pouvez maintenant utiliser textureID pour dessiner ce glyphe dans la scène.
        return textureID;
    }

}
