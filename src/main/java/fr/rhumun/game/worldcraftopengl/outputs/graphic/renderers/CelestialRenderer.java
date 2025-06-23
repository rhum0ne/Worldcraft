package fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.GraphicModule;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class CelestialRenderer extends Renderer {

    private boolean initialized = false;
    private int sunTex;
    private int moonTex;

    public CelestialRenderer(GraphicModule graphicModule) {
        super(graphicModule, ShaderManager.CELESTIAL_SHADER);
    }

    @Override
    public void init() {
        super.init();

        float[] vertices = {
                // sun quad
                -1f,  1f, 0f, 0f, 0f, 0f,
                 1f,  1f, 0f, 1f, 0f, 0f,
                -1f, -1f, 0f, 0f, 1f, 0f,
                 1f, -1f, 0f, 1f, 1f, 0f,
                // moon quad
                -1f,  1f, 0f, 0f, 0f, 1f,
                 1f,  1f, 0f, 1f, 0f, 1f,
                -1f, -1f, 0f, 0f, 1f, 1f,
                 1f, -1f, 0f, 1f, 1f, 1f
        };

        int[] indices = {
                0,1,2, 1,3,2,
                4,5,6, 5,7,6
        };

        glBindVertexArray(this.getVAO());
        glBindBuffer(GL_ARRAY_BUFFER, this.getVBO());
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.getEBO());
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, 1, GL_FLOAT, false, 6 * Float.BYTES, 5 * Float.BYTES);
        glEnableVertexAttribArray(2);

        glBindVertexArray(0);

        sunTex = loadTexture(Game.TEXTURES_PATH + "world/sun.png");
        moonTex = loadTexture(Game.TEXTURES_PATH + "world/moon.png");

        ShaderManager.CELESTIAL_SHADER.use();
        ShaderManager.CELESTIAL_SHADER.setUniform("sunTexture", 0);
        ShaderManager.CELESTIAL_SHADER.setUniform("moonTexture", 1);

        initialized = true;
    }

    @Override
    public void render() {
        if(!initialized) init();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, sunTex);
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, moonTex);

        glBindVertexArray(this.getVAO());
        glDrawElements(GL_TRIANGLES, 12, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    @Override
    public void cleanup() {
        glDeleteBuffers(this.getVBO());
        glDeleteVertexArrays(this.getVAO());
        glDeleteBuffers(this.getEBO());
        glDeleteTextures(sunTex);
        glDeleteTextures(moonTex);
    }

    private int loadTexture(String path) {
        int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);
        ByteBuffer image = STBImage.stbi_load(path, width, height, comp, 4);
        if (image != null) {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(), height.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            glGenerateMipmap(GL_TEXTURE_2D);
            STBImage.stbi_image_free(image);
        } else {
            Game.GAME.errorLog("Erreur lors du chargement de la texture " + path);
        }
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        return textureID;
    }
}
