package fr.rhumun.game.worldcraftopengl.outputs.graphic.utils;

import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;
import fr.rhumun.game.worldcraftopengl.blocks.textures.TextureTypes;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.Shader;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.ShaderUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;
import static fr.rhumun.game.worldcraftopengl.Game.TEXTURES_PATH;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.opengl.GL30C.GL_TEXTURE_2D_ARRAY;
import static org.lwjgl.opengl.GL42C.glTexStorage3D;

public class TextureUtils {

    public static int BLOCKS_TEXTURES;
    public static int GUIS_TEXTURES;

    public static void initTextures(){
        GUIS_TEXTURES = initGuiTextures();
        BLOCKS_TEXTURES = initBlocksTextures();
    }

    private static int initGuiTextures() {
        // Nombre de textures pour les blocs
        int textureCount = TextureTypes.GUIS.get().size();
        int[] blockTexturesUnits = new int[textureCount + 1]; // Unité de texture pour chaque texture

        // Création de la texture 2D array
        int textureID = glGenTextures();
        glActiveTexture(GL_TEXTURE1); // Utilisation de l'unité de texture 0
        glBindTexture(GL_TEXTURE_2D_ARRAY, textureID);

        System.out.println("Texture Array ID: " + textureID);

        // Dimensions des textures
        int width = 528;  // Par exemple, chaque texture fait 32x32 pixels
        int height = 528;

        // Allouer de l'espace pour la texture 2D array
        glTexStorage3D(GL_TEXTURE_2D_ARRAY, 1, GL_RGBA8, width, height, textureCount);

        // Charger toutes les textures dans la texture array
        for (int i = 0; i < textureCount; i++) {
            Texture texture = TextureTypes.GUIS.get().get(i);
            String texturePath = texture.getPath();


            // Charger l'image
            IntBuffer widthBuf = BufferUtils.createIntBuffer(1);
            IntBuffer heightBuf = BufferUtils.createIntBuffer(1);
            IntBuffer compBuf = BufferUtils.createIntBuffer(1);
            ByteBuffer image = STBImage.stbi_load(texturePath, widthBuf, heightBuf, compBuf, 4);

            if (image != null) {
                GAME.log("Loading " + texture.getName());
                // Copier l'image dans la couche appropriée de la texture array
                glTexSubImage3D(GL_TEXTURE_2D_ARRAY, 0, 0, 0, i, width, height, 1, GL_RGBA, GL_UNSIGNED_BYTE, image);
                glGenerateMipmap(GL_TEXTURE_2D_ARRAY); // Générer les mipmaps
                STBImage.stbi_image_free(image);
            } else {
                System.err.println("Erreur lors du chargement de la texture " + texturePath);
            }
        }

        // Paramètres de texture
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        // Associe la texture array à l'unité de texture
        blockTexturesUnits[0] = textureID;

        // Remplir le shader avec l'unité de texture et le sampler
        ShaderUtils.PLAN_SHADERS.setUniform("guiTextures", textureID);

        System.out.println("Done!");
        return textureID;
    }

    public static int initBlocksTextures() {
        // Nombre de textures pour les blocs
        int blockTextureCount = TextureTypes.BLOCKS.get().size();
        int[] blockTexturesUnits = new int[blockTextureCount + 1]; // Unité de texture pour chaque texture

        // Création de la texture 2D array
        int textureID = glGenTextures();
        glActiveTexture(GL_TEXTURE2); // Utilisation de l'unité de texture 0
        glBindTexture(GL_TEXTURE_2D_ARRAY, textureID);

        System.out.println("Texture Array ID: " + textureID);

        // Dimensions des textures
        int width = 32;  // Par exemple, chaque texture fait 32x32 pixels
        int height = 32;

        // Allouer de l'espace pour la texture 2D array
        glTexStorage3D(GL_TEXTURE_2D_ARRAY, 1, GL_RGBA8, width, height, blockTextureCount);

        // Charger toutes les textures dans la texture array
        for (int i = 0; i < blockTextureCount; i++) {
            Texture texture = TextureTypes.BLOCKS.get().get(i);
            String texturePath = texture.getPath();


            // Charger l'image
            IntBuffer widthBuf = BufferUtils.createIntBuffer(1);
            IntBuffer heightBuf = BufferUtils.createIntBuffer(1);
            IntBuffer compBuf = BufferUtils.createIntBuffer(1);
            ByteBuffer image = STBImage.stbi_load(texturePath, widthBuf, heightBuf, compBuf, 4);

            if (image != null) {
                GAME.log("Loading " + texture.getName());
                // Copier l'image dans la couche appropriée de la texture array
                glTexSubImage3D(GL_TEXTURE_2D_ARRAY, 0, 0, 0, i, width, height, 1, GL_RGBA, GL_UNSIGNED_BYTE, image);
                glGenerateMipmap(GL_TEXTURE_2D_ARRAY); // Générer les mipmaps
                STBImage.stbi_image_free(image);
            } else {
                System.err.println("Erreur lors du chargement de la texture " + texturePath);
            }
        }

        // Paramètres de texture
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_T, GL_REPEAT);

        // Associe la texture array à l'unité de texture
        blockTexturesUnits[0] = textureID;

        // Remplir le shader avec l'unité de texture et le sampler
        for (Shader shader : GAME.getGraphicModule().getShaders()) {
            shader.setUniform("textures", textureID);
        }

        ShaderUtils.PLAN_SHADERS.setUniform("texturesNumber", blockTextureCount);

        System.out.println("Done!");
        return textureID;
    }
}
