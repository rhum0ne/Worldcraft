package fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.GraphicModule;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderManager;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static fr.rhumun.game.worldcraftopengl.Game.CHUNK_SIZE;
import static fr.rhumun.game.worldcraftopengl.Game.SHOW_DISTANCE;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

/**
 * Simple renderer generating a layer of moving clouds.
 */
public class CloudRenderer extends Renderer {
    private static final float CLOUD_SIZE = 32f;
    private static final int CLOUD_COUNT = 40;

    private final List<Vector2f> clouds = new ArrayList<>();
    private final Random random = new Random();
    private float offset = 0f;

    public CloudRenderer(GraphicModule graphicModule) {
        super(graphicModule, ShaderManager.CLOUD_SHADER);
        generateClouds();
        init();
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
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glBindVertexArray(0);
    }

    private void generateClouds() {
        int range = SHOW_DISTANCE * CHUNK_SIZE;
        for (int i = 0; i < CLOUD_COUNT; i++) {
            float x = random.nextFloat() * range * 2 - range;
            float z = random.nextFloat() * range * 2 - range;
            clouds.add(new Vector2f(x, z));
        }
    }

    private void buildVertices() {
        getVertices().clear();
        getIndices().clear();
        float y = getGraphicModule().getWorld().getHeigth() + 50;
        for (Vector2f p : clouds) {
            float x1 = p.x + offset;
            float z1 = p.y;
            float x2 = x1 + CLOUD_SIZE;
            float z2 = z1 + CLOUD_SIZE;
            addAllVertices(new float[][]{
                    { x1, y, z1 },
                    { x2, y, z1 },
                    { x2, y, z2 },
                    { x1, y, z2 }
            });
            addRawIndices(new int[]{0,2,1,0,3,2});
        }
        toArrays();
    }

    @Override
    public void render() {
        buildVertices();

        glBindVertexArray(this.getVAO());
        glDrawElements(GL_TRIANGLES, this.getIndicesArray().length, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);

        offset += 0.01f;
    }

    @Override
    public void cleanup() {

    }
}
