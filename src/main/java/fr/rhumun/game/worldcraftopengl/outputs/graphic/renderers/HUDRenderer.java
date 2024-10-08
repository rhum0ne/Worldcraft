package fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.GraphicModule;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class HUDRenderer extends Renderer {

    // Coordonnées du crosshair (deux lignes, au centre de l'écran)
    private final float[] crosshairVertices = {
            // Ligne horizontale
            -0.01f,  0.0f, 0.0f,   // Point gauche
            0.01f,  0.0f, 0.0f,   // Point droit
            // Ligne verticale
            0.0f,  -0.01f, 0.0f,  // Point bas
            0.0f,   0.01f, 0.0f   // Point haut
    };

    public HUDRenderer(GraphicModule graphicModule) {
        super(graphicModule);
    }

    @Override
    public void init() {
        glBindVertexArray(this.getVAO());

        glBindBuffer(GL_ARRAY_BUFFER, this.getVBO());

        // Envoi des vertices au VBO
        glBufferData(GL_ARRAY_BUFFER, crosshairVertices, GL_STATIC_DRAW);

        // Spécifier les attributs de vertex
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        // Désactiver le VBO et VAO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    @Override
    public void render() {
        // Activer le VAO
        glBindVertexArray(this.getVAO());

        // Désactiver la profondeur pour s'assurer que le crosshair est toujours visible
        glDisable(GL_DEPTH_TEST);

        // Dessiner les deux lignes (4 points, GL_LINES connecte les points 2 par 2)
        glDrawArrays(GL_LINES, 0, 4);

        // Réactiver la profondeur
        glEnable(GL_DEPTH_TEST);

        // Désactiver le VAO
        glBindVertexArray(0);
    }

    @Override
    public void cleanup() {
        glDeleteBuffers(this.getVBO());
        glDeleteVertexArrays(this.getVAO());
    }
}
