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
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE1;

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

        glBindVertexArray(this.getVAO());
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
        glDrawArrays(GL_TRIANGLES, 0, 12);
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
