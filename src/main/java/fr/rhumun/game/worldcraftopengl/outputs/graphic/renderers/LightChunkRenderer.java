package fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers;

import fr.rhumun.game.worldcraftopengl.content.materials.Material;
import fr.rhumun.game.worldcraftopengl.worlds.LightChunk;
import lombok.Getter;

import static fr.rhumun.game.worldcraftopengl.Game.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;

@Getter
public class LightChunkRenderer extends AbstractChunkRenderer{

    private LightChunk chunk;
    private int lodLevel = 0;

    public LightChunkRenderer(LightChunk chunk){
        this.chunk = chunk;

        this.getRenderers().add(new FarRenderer(GAME.getGraphicModule()));
    }

    public synchronized void update() {
        if (!chunk.isGenerated()) return; // Vérifie que le chunk est prêt
        chunk.setToUpdate(false);

        updateData();
        updateVAO();
    }


    public synchronized void updateVAO() {
        if(!this.isAreRenderersInitialized()) {
            for(Renderer renderer : this.getRenderers()) renderer.init();
            this.setAreRenderersInitialized(true);
        }

        for(Renderer renderer : this.getRenderers()) {
            glBindVertexArray(renderer.getVAO());

            glBindBuffer(GL_ARRAY_BUFFER, renderer.getVBO());
            glBufferData(GL_ARRAY_BUFFER, renderer.getVerticesArray().clone(), GL_DYNAMIC_DRAW);

            glBindVertexArray(0);
        }
    }

    @Override
    public void updateData() {
        if (!chunk.isGenerated() || lodLevel < 1) return;
        long start = System.currentTimeMillis();

        int lod = this.lodLevel;
        int size = CHUNK_SIZE;
        int height = chunk.getWorld().getHeigth();

        for (Renderer renderer : this.getRenderers()) {
            renderer.getVertices().clear();
            renderer.getIndices().clear();
            renderer.setIndice(0);
        }

        boolean[][][] used = new boolean[size][height][size];

        for (int y = 0; y < height; y += lod) {
            for (int x = 0; x < size; x += lod) {
                for (int z = 0; z < size; z += lod) {

                    if (used[x][y][z] || !chunk.getIsVisible()[x][y][z]) continue;

                    Material baseMat = chunk.getMaterials()[x][y][z];
                    if (baseMat == null) continue;

                    int width = lod;
                    int depth = lod;

                    // Greedy expansion in X
                    outerX:
                    while (x + width < size) {
                        for (int dz = 0; dz < depth; dz += lod) {
                            if (used[x + width][y][z + dz]) break outerX;
                            Material m = chunk.getMaterials()[x + width][y][z + dz];
                            if (m == null || !m.equals(baseMat) || !chunk.getIsVisible()[x + width][y][z + dz])
                                break outerX;
                        }
                        width += lod;
                    }

                    // Greedy expansion in Z
                    outerZ:
                    while (z + depth < size) {
                        for (int dx = 0; dx < width; dx += lod) {
                            if (used[x + dx][y][z + depth]) break outerZ;
                            Material m = chunk.getMaterials()[x + dx][y][z + depth];
                            if (m == null || !m.equals(baseMat) || !chunk.getIsVisible()[x + dx][y][z + depth])
                                break outerZ;
                        }
                        depth += lod;
                    }

                    int heightM = lod;
                    // Optional vertical expansion
                    outerY:
                    while (y + heightM < height) {
                        for (int dx = 0; dx < width; dx += lod) {
                            for (int dz = 0; dz < depth; dz += lod) {
                                if (used[x + dx][y + heightM][z + dz]) break outerY;
                                Material m = chunk.getMaterials()[x + dx][y + heightM][z + dz];
                                if (m == null || !m.equals(baseMat) || !chunk.getIsVisible()[x + dx][y + heightM][z + dz])
                                    break outerY;
                            }
                        }
                        heightM += lod;
                    }

                    for (int dx = 0; dx < width; dx += lod) {
                        for (int dy = 0; dy < heightM; dy += lod) {
                            for (int dz = 0; dz < depth; dz += lod) {
                                used[x + dx][y + dy][z + dz] = true;
                            }
                        }
                    }

                    float worldX = chunk.getX() * CHUNK_SIZE + x;
                    float worldY = y;
                    float worldZ = chunk.getZ() * CHUNK_SIZE + z;

                    this.getRenderers().getFirst().getVertices().add(new float[] {
                            worldX - 0.5f, worldY - 1f, worldZ - 0.5f,
                            baseMat.getTexture().getId(),
                            width, heightM, depth
                    });
                    this.getRenderers().getFirst().setIndice(
                            this.getRenderers().getFirst().getIndice() + 1
                    );
                }
            }
        }

        this.setVerticesNumber(0);
        for (Renderer renderer : this.getRenderers()) {
            renderer.toArrays();
            if (SHOWING_RENDERER_DATA)
                this.setVerticesNumber(this.getVerticesNumber() + renderer.getVertices().size());
        }
    }




    @Override
    public void render() {
        if(chunk.isToUpdate()) update();

        this.getRenderers().getFirst().render();
    }

    @Override
    public void cleanup() {
        this.chunk = null;
    }

    @Override
    public void setDistanceFromPlayer(int distance) {
        if(distance == this.getDistanceFromPlayer()) return;
        super.setDistanceFromPlayer(distance);

        int lod = chunk.getLODLevel(distance);
        if(this.lodLevel != lod){
            this.lodLevel = lod;
            this.chunk.setToUpdate(true);
        }

    }
}
