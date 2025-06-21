package fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers;

import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.GraphicModule;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderManager;
import fr.rhumun.game.worldcraftopengl.worlds.World;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
 * Renderer that generates clouds around the player using the world seed.
 * Clouds are white quads at a fixed altitude and slowly move along the X axis.
    private static final float SPEED = 0.01f;

    private final Player player;
    private final long seed;
        this.player = graphicModule.getPlayer();
        World world = graphicModule.getWorld();
        this.seed = world.getSeed().getLong();
    /**
     * Procedurally builds cloud vertices around the player based on the world seed.
     */

        World world = getGraphicModule().getWorld();
        float y = world.getHeigth() + 50;

        int centerX = player.getLocation().getChunk().getX();
        int centerZ = player.getLocation().getChunk().getZ();

        for (int cx = centerX - SHOW_DISTANCE; cx <= centerX + SHOW_DISTANCE; cx++) {
            for (int cz = centerZ - SHOW_DISTANCE; cz <= centerZ + SHOW_DISTANCE; cz++) {
                Random rand = new Random(seed + cx * 341873128712L + cz * 132897987541L);
                if (rand.nextFloat() > 0.3f) continue; // Only spawn a cloud sometimes

                float baseX = cx * CHUNK_SIZE + rand.nextFloat() * (CHUNK_SIZE - CLOUD_SIZE);
                float baseZ = cz * CHUNK_SIZE + rand.nextFloat() * (CHUNK_SIZE - CLOUD_SIZE);

                float x1 = baseX + offset;
                float z1 = baseZ;
                float x2 = x1 + CLOUD_SIZE;
                float z2 = z1 + CLOUD_SIZE;

                addAllVertices(new float[][]{
                        {x1, y, z1, 0f, 0f, 0f, 0f, -1f, 0f},
                        {x2, y, z1, 1f, 0f, 0f, 0f, -1f, 0f},
                        {x2, y, z2, 1f, 1f, 0f, 0f, -1f, 0f},
                        {x1, y, z2, 0f, 1f, 0f, 0f, -1f, 0f}
                });
                addRawIndices(new int[]{0, 1, 2, 0, 2, 3});
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
        updateVAO();
        glBindVertexArray(this.getVAO());
        glDrawElements(GL_TRIANGLES, this.getIndicesArray().length, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
        offset += SPEED;
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
