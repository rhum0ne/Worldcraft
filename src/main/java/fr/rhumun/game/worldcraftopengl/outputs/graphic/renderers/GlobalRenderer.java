package fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.GraphicModule;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.Shader;
import lombok.Getter;
import lombok.Setter;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

@Getter
@Setter
public class GlobalRenderer extends Renderer {

    private final boolean useSkinning;

    public GlobalRenderer(GraphicModule graphicModule, Shader shader) {
        this(graphicModule, shader, false);
    }

    public GlobalRenderer(GraphicModule graphicModule, Shader shader, boolean useSkinning) {
        super(graphicModule, shader);
        this.useSkinning = useSkinning;
    }

    @Override
    public void init() {
        super.init();
        glBindVertexArray(this.getVAO());
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glBindBuffer(GL_ARRAY_BUFFER, this.getVBO());
        glBufferData(GL_ARRAY_BUFFER, this.getVerticesArray(), GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.getEBO());

        // Attributs communs
        glVertexAttribPointer(0, 3, GL_FLOAT, false, (useSkinning ? 10 : 9) * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 2, GL_FLOAT, false, (useSkinning ? 10 : 9) * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, 1, GL_FLOAT, false, (useSkinning ? 10 : 9) * Float.BYTES, 5 * Float.BYTES);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, 3, GL_FLOAT, false, (useSkinning ? 10 : 9) * Float.BYTES, 6 * Float.BYTES);
        glEnableVertexAttribArray(3);

        if (useSkinning) {
            glVertexAttribIPointer(4, 1, GL_INT, 10 * Float.BYTES, 9 * Float.BYTES);
            glEnableVertexAttribArray(4);
        }

        glBindVertexArray(0);
    }

    @Override
    public void render() {
        glBindVertexArray(this.getVAO());
        glDrawElements(GL_TRIANGLES, this.getIndicesArray().length, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    @Override
    public void cleanup() {
        glDeleteBuffers(this.getVBO());
        glDeleteVertexArrays(this.getVAO());
        glDeleteBuffers(this.getEBO());
        freeBuffers();
    }
}
