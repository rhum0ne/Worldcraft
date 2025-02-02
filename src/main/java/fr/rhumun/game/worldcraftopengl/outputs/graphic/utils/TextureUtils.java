package fr.rhumun.game.worldcraftopengl.outputs.graphic.utils;

import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.content.textures.TextureTypes;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.Shader;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;
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
        BLOCKS_TEXTURES = initBlocksTextures();
        GUIS_TEXTURES = initGuiTextures();
        initEntitiesTextures();
    }

    private static int initGuiTextures() {
        int[] guiTexturesUnits = new int[TextureTypes.GUIS.get().size()];
        int i=0;
        for (Texture texture : TextureTypes.GUIS.get()) {
            int textureID = loadTexture(texture.getPath());
            glActiveTexture(textureID); // Active l'unité de texture correspondante
            glBindTexture(GL_TEXTURE_2D, textureID);
            glGenerateMipmap(GL_TEXTURE_2D);
            //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);  // Répétition sur l'axe S (horizontal)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);  // Répétition sur l'axe T (vertical)

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            guiTexturesUnits[i] = textureID; // Stocke l'unité de texture
            i++;
        }

        ShaderUtils.PLAN_SHADERS.setUniform("guiTextures", guiTexturesUnits);
        ShaderUtils.ENTITY_SHADER.setUniform("texturesNumber", BLOCKS_TEXTURES + guiTexturesUnits.length);

        GAME.debug("Done!");
        return guiTexturesUnits.length;
    }

    private static void initEntitiesTextures() {
        int entitesTextureCount = TextureTypes.ENTITIES.get().size();
        int[] entitiesTexturesUnits = new int[entitesTextureCount + 1]; // Unité de texture pour chaque texture

        // Création de la texture 2D array
        int textureID = glGenTextures();
        glActiveTexture(GL_TEXTURE0 + textureID); // Utilisation de l'unité de texture 0
        glBindTexture(GL_TEXTURE_2D_ARRAY, textureID);

        GAME.debug("Texture Array ID: " + textureID);

        // Dimensions des textures
        int width = 64;  // Par exemple, chaque texture fait 32x32 pixels
        int height = 64;

        // Allouer de l'espace pour la texture 2D array
        glTexStorage3D(GL_TEXTURE_2D_ARRAY, 1, GL_RGBA8, width, height, entitesTextureCount);

        // Charger toutes les textures dans la texture array
        for (int i = 0; i < entitesTextureCount; i++) {
            Texture texture = TextureTypes.ENTITIES.get().get(i);
            String texturePath = texture.getPath();


            // Charger l'image
            IntBuffer widthBuf = BufferUtils.createIntBuffer(1);
            IntBuffer heightBuf = BufferUtils.createIntBuffer(1);
            IntBuffer compBuf = BufferUtils.createIntBuffer(1);
            ByteBuffer image = STBImage.stbi_load(texturePath, widthBuf, heightBuf, compBuf, 4);

            if (image != null) {
                GAME.debug("Loading " + texture.getName());
                // Copier l'image dans la couche appropriée de la texture array
                glTexSubImage3D(GL_TEXTURE_2D_ARRAY, 0, 0, 0, i, widthBuf.get(0), heightBuf.get(0), 1, GL_RGBA, GL_UNSIGNED_BYTE, image);
                glGenerateMipmap(GL_TEXTURE_2D_ARRAY); // Générer les mipmaps
                STBImage.stbi_image_free(image);
            } else {
                GAME.errorLog("Erreur lors du chargement de la texture " + texturePath);
            }
        }

        // Paramètres de texture
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_T, GL_REPEAT);

        // Associe la texture array à l'unité de texture
        entitiesTexturesUnits[0] = textureID;

        // Remplir le shader avec l'unité de texture et le sampler
        ShaderUtils.ENTITY_SHADER.setUniform("entitiesTextures", entitiesTexturesUnits);

        GAME.debug("Done!");
    }

    private static int loadTexture(String path) {
        int textureID = glGenTextures();
        glActiveTexture(GL_TEXTURE0 + textureID);
        glBindTexture(GL_TEXTURE_2D, textureID);

        GAME.debug(path + " -> " + textureID );

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);
        ByteBuffer image = STBImage.stbi_load(path, width, height, comp, 4);

        if (image != null) {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(), height.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            glGenerateMipmap(GL_TEXTURE_2D);
            STBImage.stbi_image_free(image);
        } else {
            GAME.errorLog("Erreur lors du chargement de la texture " + path);
        }

        return textureID;
    }

    private static int initBlocksTextures() {
        // Nombre de textures pour les blocs
        int blockTextureCount = TextureTypes.BLOCKS.get().size();
        int[] blockTexturesUnits = new int[blockTextureCount]; // Unité de texture pour chaque texture

        // Création de la texture 2D array
        int textureID = glGenTextures();
        glActiveTexture(GL_TEXTURE0 + textureID); // Utilisation de l'unité de texture 0
        glBindTexture(GL_TEXTURE_2D_ARRAY, textureID);

        GAME.debug("Texture Array ID: " + textureID);

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
                GAME.debug("Loading " + texture.getName());
                // Copier l'image dans la couche appropriée de la texture array
                glTexSubImage3D(GL_TEXTURE_2D_ARRAY, 0, 0, 0, i, widthBuf.get(0), heightBuf.get(0), 1, GL_RGBA, GL_UNSIGNED_BYTE, image);
                glGenerateMipmap(GL_TEXTURE_2D_ARRAY); // Générer les mipmaps
                STBImage.stbi_image_free(image);
            } else {
                GAME.errorLog("Erreur lors du chargement de la texture " + texturePath);
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

        GAME.debug("Done!");
        return textureID;
    }
}
