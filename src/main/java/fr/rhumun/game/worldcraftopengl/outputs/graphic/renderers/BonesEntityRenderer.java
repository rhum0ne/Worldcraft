package fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.GraphicModule;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.Shader;
import lombok.Getter;
import lombok.Setter;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

@Getter
@Setter
public class BonesEntityRenderer extends Renderer {

    public BonesEntityRenderer(GraphicModule graphicModule, Shader shader) {
        super(graphicModule, shader);
    }

    @Override
    public void init() {
        super.init();
        glBindVertexArray(this.getVAO());
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

// Configuration des attributs de sommet pour position, coordonnées de texture et ID de texture
        glBindBuffer(GL_ARRAY_BUFFER, this.getVBO());
        glBufferData(GL_ARRAY_BUFFER, this.getVerticesArray(), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.getEBO());

// Attribut de position (3 floats)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 17 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

// Attribut de coordonnées de texture (2 floats)
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 17 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

// Attribut de l'ID de texture (1 int)
        glVertexAttribPointer(2, 1, GL_FLOAT, false, 17 * Float.BYTES, 5 * Float.BYTES);
        glEnableVertexAttribArray(2);


// Attribut des normales
        glVertexAttribPointer(3, 3, GL_FLOAT, false, 17 * Float.BYTES, 6 * Float.BYTES);
        glEnableVertexAttribArray(3);

        // Bone indices
        glVertexAttribPointer(4, 4, GL_FLOAT, false, 17 * Float.BYTES, 9 * Float.BYTES);
        glEnableVertexAttribArray(4);

        // Bone weights
        glVertexAttribPointer(5, 4, GL_FLOAT, false, 17 * Float.BYTES, 13 * Float.BYTES);
        glEnableVertexAttribArray(5);

        // Délier le VAO
        glBindVertexArray(0);
    }

    @Override
    public void render() {
        glBindVertexArray(this.getVAO());
        /*glBindBuffer(GL_ARRAY_BUFFER, this.getVBO());
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.getEBO());*/
        glDrawElements(GL_TRIANGLES, this.getIndicesArray().length, GL_UNSIGNED_INT, 0);

        //glBindBuffer(GL_ARRAY_BUFFER, 0);
        //glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    @Override
    public void cleanup() {
        glDeleteBuffers(this.getVBO());
        glDeleteVertexArrays(this.getVAO());
        glDeleteBuffers(this.getEBO());
    }
}
