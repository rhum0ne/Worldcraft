package fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.GraphicModule;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.glDrawArraysInstanced;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

@Getter
@Setter
public class FarRenderer extends Renderer {

    private int dummyVBO;

    public FarRenderer(GraphicModule graphicModule) {
        super(graphicModule, ShaderUtils.GLOBAL_SHADERS);
    }

    @Override
    public void init() {
        super.init();

        GAME.debug("FAR RENDERER INITIALIZED");

        float[] dummyVertices = new float[36]; // Peut être n’importe quoi

        dummyVBO = glGenBuffers();
        glBindVertexArray(this.getVAO());

        glBindBuffer(GL_ARRAY_BUFFER, dummyVBO);
        glBufferData(GL_ARRAY_BUFFER, dummyVertices, GL_STATIC_DRAW);

        // Attribut fictif pour activer gl_VertexID (on n'utilise pas réellement ces données)
        glVertexAttribPointer(3, 1, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(3);

        // Création du VAO
        glBindBuffer(GL_ARRAY_BUFFER, this.getVBO());
        glBufferData(GL_ARRAY_BUFFER, 0, GL_DYNAMIC_DRAW);

        // Attribut 0 : vec3 position
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 7 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribDivisor(0, 1); // 1 : changement par instance

        // Attribut 1 : float texID
        glVertexAttribPointer(1, 1, GL_FLOAT, false, 7 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
        glVertexAttribDivisor(1, 1); // 1 : changement par instance

        // Attribut 2 : vec3 scale
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 7 * Float.BYTES, 4 * Float.BYTES);
        glEnableVertexAttribArray(2);
        glVertexAttribDivisor(2, 1);


        glBindVertexArray(0);
    }

    @Override
    public void render() {
        if (this.getIndice() == 0) return;

        //System.out.println("RENDERING FAR CHUNK " + this.getVerticesArray().length/7 + " blocks");
        glBindVertexArray(this.getVAO());
        // 36 sommets par cube
        glDrawArraysInstanced(GL_TRIANGLES, 0, 36, this.getIndice());

        glBindVertexArray(0);
    }

    @Override
    public void cleanup() {
        glDeleteBuffers(this.getVBO());
        glDeleteVertexArrays(this.getVAO());
        glDeleteBuffers(this.dummyVBO); // Supprime aussi ce buffer
        glDeleteBuffers(this.getEBO());
    }
}
