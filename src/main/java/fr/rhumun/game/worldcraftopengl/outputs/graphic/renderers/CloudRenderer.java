package fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers;

import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.GraphicModule;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderManager;
import fr.rhumun.game.worldcraftopengl.worlds.World;
import fr.rhumun.game.worldcraftopengl.worlds.generators.utils.Seed;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

import static fr.rhumun.game.worldcraftopengl.Game.CHUNK_SIZE;
import static fr.rhumun.game.worldcraftopengl.Game.SHOW_DISTANCE;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

/**
 * Renderer that generates clouds around the player using the world seed.
 * Clouds are white quads at a fixed altitude and slowly move along the X axis.
 */
@Setter @Getter
public class CloudRenderer extends Renderer {
    private static final float CLOUD_SIZE = 32f;
    private static final float SPEED = 0.001f;

    private final Player player;
    private Seed seed;
    private float offset = 0f;

    public CloudRenderer(GraphicModule graphicModule) {
        super(graphicModule, ShaderManager.CLOUD_SHADER);
        this.player = graphicModule.getPlayer();
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

    /**
     * Procedurally builds cloud vertices around the player based on the world seed.
     */
    private void buildVertices() {
        if(seed == null) return;

        getVertices().clear();
        getIndices().clear();

        World world = getGraphicModule().getWorld();
        float y = world.getHeigth() - 100;

        int centerX = player.getLocation().getChunk().getX();
        int centerZ = player.getLocation().getChunk().getZ();

        for (int cx = centerX - SHOW_DISTANCE*2; cx <= centerX + SHOW_DISTANCE*2; cx++) {
            for (int cz = centerZ - SHOW_DISTANCE*2; cz <= centerZ + SHOW_DISTANCE*2; cz++) {
                Random rand = new Random(seed.getLong() + cx * 341873128712L + cz * 132897987541L);
                if (rand.nextFloat() > 0.3f) continue; // Only spawn a cloud sometimes

                float baseX = cx * CHUNK_SIZE + rand.nextFloat() * (CHUNK_SIZE - CLOUD_SIZE);
                float baseZ = cz * CHUNK_SIZE + rand.nextFloat() * (CHUNK_SIZE - CLOUD_SIZE);

                float x1 = baseX + offset;
                float z1 = baseZ;
                float x2 = x1 + CLOUD_SIZE;
                float z2 = z1 + CLOUD_SIZE;

                addAllVertices(new float[][]{
                        {x1, y, z1},
                        {x2, y, z1},
                        {x2, y, z2},
                        {x1, y, z2}
                });
                addRawIndices(new int[]{0, 1, 2, 0, 2, 3});
            }
        }
        toArrays();
    }

    private void updateVAO() {
        glBindVertexArray(this.getVAO());
        glBindBuffer(GL_ARRAY_BUFFER, this.getVBO());
        glBufferData(GL_ARRAY_BUFFER, this.getVerticesArray(), GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.getEBO());
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, this.getIndicesArray(), GL_STATIC_DRAW);
        glBindVertexArray(0);
    }

    @Override
    public void render() {
        buildVertices();
        updateVAO();
        glBindVertexArray(this.getVAO());
        glDrawElements(GL_TRIANGLES, this.getIndicesArray().length, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
        offset += SPEED;
    }

    @Override
    public void cleanup() {

    }
}