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
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class TextureUtils {

    public static int ATLAS;

        public static void initTextures() {
            List<Texture> textures = Texture.textures;
            int atlasSize = 1024; // Taille de l'atlas (exemple : 1024x1024)
            int textureSize = 64; // Taille des textures individuelles (exemple : 64x64)

            // Génère l'atlas
            ByteBuffer atlasData = generateAtlas(textures, atlasSize, textureSize);

            // Charge l'atlas en OpenGL
            int atlasTextureID = glGenTextures();
            ATLAS = atlasTextureID;
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, atlasTextureID);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, atlasSize, atlasSize, 0, GL_RGBA, GL_UNSIGNED_BYTE, atlasData);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glGenerateMipmap(GL_TEXTURE_2D);

            // Libère les données de l'atlas
            atlasData.clear();

            // Associe l'atlas aux shaders
            for (Shader shader : GAME.getGraphicModule().getShaders()) {
                shader.setUniform("atlas", 0); // L'atlas est associé à l'unité de texture 0
            }

            System.out.println("Atlas généré avec succès !");
        }

        private static ByteBuffer generateAtlas(List<Texture> textures, int atlasSize, int textureSize) {
            int columns = atlasSize / textureSize;
            ByteBuffer atlasData = BufferUtils.createByteBuffer(atlasSize * atlasSize * 4); // RGBA

            int x = 0, y = 0;

            for (Texture texture : textures) {
                String path = texture.getPath();

                IntBuffer width = BufferUtils.createIntBuffer(1);
                IntBuffer height = BufferUtils.createIntBuffer(1);
                IntBuffer comp = BufferUtils.createIntBuffer(1);
                ByteBuffer image = STBImage.stbi_load(path, width, height, comp, 4);

                if (image != null) {
                    // Copie les pixels dans l'atlas
                    for (int ty = 0; ty < textureSize; ty++) {
                        for (int tx = 0; tx < textureSize; tx++) {
                            int srcIndex = (ty * textureSize + tx) * 4;
                            int dstIndex = ((y + ty) * atlasSize + (x + tx)) * 4;
                            atlasData.put(dstIndex, image.get(srcIndex));      // R
                            atlasData.put(dstIndex + 1, image.get(srcIndex + 1)); // G
                            atlasData.put(dstIndex + 2, image.get(srcIndex + 2)); // B
                            atlasData.put(dstIndex + 3, image.get(srcIndex + 3)); // A
                        }
                    }

                    // Définir les coordonnées UV
                    float u1 = (float) x / atlasSize;
                    float v1 = (float) y / atlasSize;
                    float u2 = (float) (x + textureSize) / atlasSize;
                    float v2 = (float) (y + textureSize) / atlasSize;

                    texture.setUV(u1, v1, u2, v2);

                    STBImage.stbi_image_free(image);
                } else {
                    System.err.println("Erreur lors du chargement de la texture " + path);
                }

                // Passer à la colonne suivante, ou à la ligne suivante si la colonne est pleine
                x += textureSize;
                if (x >= atlasSize) {
                    x = 0;
                    y += textureSize;
                }
            }

            return atlasData;
        }


//    public static void initTextures(){
//        int[] blockTexturesUnits = new int[TextureTypes.BLOCKS.get().size()+1]; // Supposons que tu as 4 textures
//        int[] guiTexturesUnits = new int[TextureTypes.GUIS.get().size()+1]; // Supposons que tu as 4 textures
//        int i = 1;
//
//        for (Texture texture : TextureTypes.BLOCKS.get()) {
//            int textureID = loadTexture(TEXTURES_PATH + texture.getPath());
//            glActiveTexture(textureID); // Active l'unité de texture correspondante
//            glBindTexture(GL_TEXTURE_2D, textureID);
//            glGenerateMipmap(GL_TEXTURE_2D);
//            //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
//            //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);  // Répétition sur l'axe S (horizontal)
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);  // Répétition sur l'axe T (vertical)
//
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
//            blockTexturesUnits[i] = textureID; // Stocke l'unité de texture
//            texture.setId(i);
//            i++;
//        }
//
//        ShaderUtils.PLAN_SHADERS.setUniform("texturesNumber", i);
//
//        int j=1;
//        i++;
//        for (Texture texture : TextureTypes.GUIS.get()) {
//            int textureID = loadTexture(TEXTURES_PATH + texture.getPath());
//            glActiveTexture(textureID); // Active l'unité de texture correspondante
//            glBindTexture(GL_TEXTURE_2D, textureID);
//            glGenerateMipmap(GL_TEXTURE_2D);
//            //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
//            //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);  // Répétition sur l'axe S (horizontal)
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);  // Répétition sur l'axe T (vertical)
//
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
//            guiTexturesUnits[j] = textureID; // Stocke l'unité de texture
//            texture.setId(i);
//            j++;
//            i++;
//        }
//
//        // Associe chaque unité de texture au sampler2D correspondant dans le shader
//        for(Shader shader : GAME.getGraphicModule().getShaders()){
//            shader.setUniform("textures", blockTexturesUnits);
//        }
//        ShaderUtils.PLAN_SHADERS.setUniform("guiTextures", guiTexturesUnits);
//
//        System.out.println("Done!");
//    }


    private static int loadTexture(String path) {
        int textureID = glGenTextures();
        glActiveTexture(GL_TEXTURE0 + textureID);
        glBindTexture(GL_TEXTURE_2D, textureID);

        System.out.println(path + " -> " + textureID );

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);
        ByteBuffer image = STBImage.stbi_load(path, width, height, comp, 4);

        if (image != null) {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(), height.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            glGenerateMipmap(GL_TEXTURE_2D);
            STBImage.stbi_image_free(image);
        } else {
            System.err.println("Erreur lors du chargement de la texture " + path);
        }

        return textureID;
    }
}
