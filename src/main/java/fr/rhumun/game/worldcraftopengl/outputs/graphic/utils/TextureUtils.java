package fr.rhumun.game.worldcraftopengl.outputs.graphic.utils;

import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.Shader;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.ShaderUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;
import static fr.rhumun.game.worldcraftopengl.Game.TEXTURES_PATH;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class TextureUtils {

    public static void initTextures(){
        int[] textureUnits = new int[Texture.textures.size()+1]; // Supposons que tu as 4 textures
        int i = 1;
        for (Texture texture : Texture.textures) {
            int textureID = loadTexture(TEXTURES_PATH + texture.getPath());
            //glActiveTexture(GL_TEXTURE0 + i); // Active l'unité de texture correspondante
            glBindTexture(GL_TEXTURE_2D, textureID);
            glGenerateMipmap(GL_TEXTURE_2D);
            //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            textureUnits[i] = i; // Stocke l'unité de texture
            System.out.println(texture + " -> " + i);
            i++;
        }

        // Associe chaque unité de texture au sampler2D correspondant dans le shader
        for(Shader shader : GAME.getGraphicModule().getShaders()){
            shader.setUniform("textures", textureUnits);
        }
        System.out.println("Done!");
    }


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
