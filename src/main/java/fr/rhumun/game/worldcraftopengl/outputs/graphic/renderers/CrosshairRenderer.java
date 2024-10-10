package fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers;

import fr.rhumun.game.worldcraftopengl.Player;
import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.GraphicModule;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.ShaderUtils;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class CrosshairRenderer extends Renderer {

    // Coordonnées des vertices d'un quad avec leurs coordonnées de texture (UV)
    private final float[] crosshairVertices = {
            // Positions        // Coordonnées de texture
            -0.07f,  0.07f, 0.0f,   0.0f, 1.0f, Texture.CROSSHAIR.getId(),   // Haut gauche
            0.07f,  0.07f, 0.0f,   1.0f, 1.0f, Texture.CROSSHAIR.getId(),    // Haut droit
            0.07f, -0.07f, 0.0f,   1.0f, 0.0f, Texture.CROSSHAIR.getId(),    // Bas droit
            -0.07f, -0.07f, 0.0f,   0.0f, 0.0f, Texture.CROSSHAIR.getId(),     // Bas gauche
    };

    // Indices pour dessiner un quad avec deux triangles
    private final int[] quadIndices = {
            0, 1, 2,   // Premier triangle
            2, 3, 0    // Deuxième triangle
    };


    public CrosshairRenderer(GraphicModule graphicModule) {
        super(graphicModule);
    }

    @Override
    public void init() {
        glUseProgram(ShaderUtils.PLAN_SHADERS.id);
        glBindVertexArray(this.getVAO());
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);


// Envoi des vertices au VBO
        glBindBuffer(GL_ARRAY_BUFFER, this.getVBO());
        glBufferData(GL_ARRAY_BUFFER, crosshairVertices, GL_STATIC_DRAW);
// Envoi des indices au IBO
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.getEBO());
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, quadIndices, GL_STATIC_DRAW);

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

    }

    public void update(Player player) {

    }

    @Override
    public void render() {
        glEnable(GL_BLEND);
        glBindVertexArray(this.getVAO());

        // Activer la texture
        //glBindTexture(GL_TEXTURE_2D, Texture.CROSSHAIR.getId());

        // Dessiner le quad avec les indices
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

        glBindVertexArray(0);
        glDisable(GL_BLEND);
    }


    @Override
    public void cleanup() {
        glDeleteBuffers(this.getVBO());
        glDeleteVertexArrays(this.getVAO());
    }
}
