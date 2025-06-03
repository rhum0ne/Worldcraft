package fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers;

import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderManager;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengles.GLES20.glDeleteBuffers;

public class GuiRenderer extends Renderer {

    private final float[] vertices;

    // Indices pour dessiner un quad avec deux triangles
    private final int[] indices = {
            0, 2, 1,   // Premier triangle
            2, 3, 1    // Deuxième triangle
    };

    private boolean isInitialized = false;

    public GuiRenderer(float x1, float y1, float x2, float y2, Texture texture) {
        super(GAME.getGraphicModule(), ShaderManager.PLAN_SHADERS);

        vertices = new float[]{
                // Positions        // Coordonnées de texture
                x1,  y1, 0.0f,   0.0f, 0.0f, texture.getId(),   // Haut gauche
                x2,  y1, 0.0f,   1.0f, 0.0f, texture.getId(),    // Haut droit
                x1, y2, 0.0f,   0.0f, 1.0f, texture.getId(),     // Bas gauche
                x2, y2, 0.0f,   1.0f, 1.0f, texture.getId(),    // Bas droit
        };
    }


    @Override
    public void init() {
        super.init();

        glUseProgram(ShaderManager.PLAN_SHADERS.id);
        glBindVertexArray(this.getVAO());
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);


// Envoi des vertices au VBO
        glBindBuffer(GL_ARRAY_BUFFER, this.getVBO());
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
// Envoi des indices au IBO
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.getEBO());
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

// Position (aPos)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

// Coordonnées de texture (aTexCoord)
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, 1, GL_FLOAT, false, 6 * Float.BYTES, 5 * Float.BYTES);
        glEnableVertexAttribArray(2);


// Désactiver VAO
        glBindVertexArray(0);

        this.isInitialized = true;
    }

    @Override
    public void render() {
        if(!this.isInitialized) this.init();

        glBindVertexArray(this.getVAO());
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    @Override
    public void cleanup() {
//        glDeleteBuffers(this.getVBO());
//        glDeleteVertexArrays(this.getVAO());
    }
}
