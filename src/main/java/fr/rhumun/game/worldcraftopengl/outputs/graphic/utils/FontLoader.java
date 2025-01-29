package fr.rhumun.game.worldcraftopengl.outputs.graphic.utils;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.GuiCharacter;
import lombok.Getter;
import org.lwjgl.opengl.GL30C;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;
import static org.lwjgl.opengl.ARBInternalformatQuery2.GL_TEXTURE_2D_ARRAY;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL12.glTexSubImage3D;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.opengl.GL42C.glTexStorage3D;
import static org.lwjgl.stb.STBTruetype.*;

public class FontLoader {

    private ByteBuffer fontBuffer;
    private STBTTFontinfo font;
    private int fontSize = 16;
    @Getter
    public static int TEXTURES_ARRAY;

    public FontLoader(String fontPath) throws Exception {
        // Charger la police TTF dans un ByteBuffer
        byte[] fontBytes = Files.readAllBytes(Paths.get(fontPath));
        fontBuffer = ByteBuffer.allocateDirect(fontBytes.length);
        fontBuffer.put(fontBytes).flip();

        font = new STBTTFontinfo(fontBuffer);
        if (!stbtt_InitFont(font, fontBuffer)) {
            throw new RuntimeException("Failed to initialize font.");
        }

    }

    public int loadFont() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Définir les caractères à charger (ASCII de 32 à 127)
            int charCount = 127 - 32; // Nombre de caractères
            int width = fontSize; // Largeur fixe pour chaque glyphe
            int height = fontSize; // Hauteur fixe pour chaque glyphe

            glUseProgram(ShaderUtils.TEXT_SHADER.id);
            // Création de la texture array
            int textureID = glGenTextures();
            glActiveTexture(GL_TEXTURE0 + textureID); // Utilisation de l'unité de texture 2 (ou une autre disponible)
            glBindTexture(GL_TEXTURE_2D_ARRAY, textureID);

            System.out.println("Character Texture Array ID: " + textureID);

            // Allouer de l'espace pour le tableau de textures
            glTexStorage3D(GL_TEXTURE_2D_ARRAY, 1, GL_RGBA8, width, height, charCount);

            // Charger chaque glyphe et l'ajouter comme une couche dans le tableau de textures
            for (char c = 32; c < 127; c++) {
                if(c==' ') continue;
                // Récupérer les métriques du glyphe
                IntBuffer widthBuf = stack.mallocInt(1);
                IntBuffer heightBuf = stack.mallocInt(1);
                IntBuffer xoff = stack.mallocInt(1);
                IntBuffer yoff = stack.mallocInt(1);
                IntBuffer leftSideBearing = stack.mallocInt(1);  // Buffer alloué ici
                IntBuffer advance = stack.mallocInt(1);

                stbtt_GetCodepointHMetrics(font, c, advance, leftSideBearing);

                float scale = stbtt_ScaleForPixelHeight(this.font, fontSize);
                ByteBuffer bitmap = stbtt_GetCodepointBitmap(font, scale, scale, c, widthBuf, heightBuf, xoff, yoff);

                if (bitmap != null) {
                    // Créer un buffer pour redimensionner le glyphe à la taille fixe (32x32)
                    ByteBuffer resizedBitmap = resizeGlyph(bitmap, widthBuf.get(0), heightBuf.get(0),width, height);

                    new GuiCharacter(c, c-32, widthBuf.get(0), heightBuf.get(0), xoff.get(0), yoff.get(0), (float) advance.get(0) );
                    // Copier le bitmap redimensionné dans la couche appropriée
                    glTexSubImage3D(GL_TEXTURE_2D_ARRAY, 0, 0, 0, c - 32, width, height, 1, GL_RED, GL_UNSIGNED_BYTE, resizedBitmap);
                    glGenerateMipmap(GL_TEXTURE_2D_ARRAY);
                    // Libérer la mémoire du bitmap original
                    stbtt_FreeBitmap(bitmap);
                } else {
                    System.err.println("Erreur lors du chargement du glyphe pour le caractère: " + c);
                }
            }

            // Configurer les paramètres de texture
            glTexParameteri(GL30C.GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
            glTexParameteri(GL30C.GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
//            glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
//            glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

            ShaderUtils.TEXT_SHADER.setUniform("charsTextures", textureID);
            TEXTURES_ARRAY = textureID;

            GAME.log("Character Texture Array creation complete! " + GuiCharacter.characters.size() + " entries.");
            return textureID;
        }
    }

    private ByteBuffer resizeGlyph(ByteBuffer input, int oldWidth, int oldHeight, int newWidth, int newHeight) {
        ByteBuffer resized = ByteBuffer.allocateDirect(newWidth * newHeight); // RGBA nécessite 4 canaux
        float xRatio = (float) oldWidth / newWidth;
        float yRatio = (float) oldHeight / newHeight;

        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                int oldX = (int) (x * xRatio);
                int oldY = (int) (y * yRatio);
                int oldIndex = (oldY * oldWidth + oldX); // Chaque pixel RGBA a 4 octets

                // Copier le pixel RGBA de l'ancienne image vers la nouvelle
                resized.put((y * newWidth + x), input.get(oldIndex));
            }
        }
        return resized;
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
