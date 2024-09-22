package fr.rhumun.game.worldcraftopengl.outputs.graphic;

import fr.rhumun.game.worldcraftopengl.props.Material;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static fr.rhumun.game.worldcraftopengl.Game.TEXTURES_PATH;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1iv;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class TextureUtils {

    static void initTextures(){
        int[] textureUnits = new int[Material.values().length+1]; // Supposons que tu as 4 textures
        int i = 1;
        for (Material mat : Material.values()) {
            int textureID = loadTexture(TEXTURES_PATH + mat.getTexturePath());
            //glActiveTexture(GL_TEXTURE0 + i); // Active l'unité de texture correspondante
            glBindTexture(GL_TEXTURE_2D, textureID);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            textureUnits[i] = i; // Stocke l'unité de texture
            System.out.println(mat + " -> " + i);
            i++;
        }

        // Associe chaque unité de texture au sampler2D correspondant dans le shader
        int texturesLocation = glGetUniformLocation(GraphicModule.shaders, "textures");
        glUniform1iv(texturesLocation, textureUnits); // Passe le tableau d'unités de texture au shader
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
