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

        for (int y = height-1; y >= 0; y -= lod) {
            for (int x = 0; x < size; x += lod) {
                for (int z = 0; z < size; z += lod) {

                    if (used[x][y][z] || !chunk.getIsVisible()[x][y][z]) continue;

                    Material baseMat = chunk.getMaterials()[x][y][z];
                    if (baseMat == null) continue;

                    int maxX = x;
                    int maxZ = z;
                    int maxY = y;

                    // Expansion en X
                    for (int x1 = x + lod; x1 < size; x1 += lod) {
                        if (used[x1][y][z] || !chunk.getIsVisible()[x1][y][z] || !baseMat.equals(chunk.getMaterials()[x1][y][z])) break;
                        maxX = x1;
                    }

                    // Expansion en Z
                    for (int z1 = z + lod; z1 < size; z1 += lod) {
                        boolean ok = true;
                        for (int dx = x; dx <= maxX; dx += lod) {
                            if (used[dx][y][z1] || !chunk.getIsVisible()[dx][y][z1] || !baseMat.equals(chunk.getMaterials()[dx][y][z1])) {
                                ok = false;
                                break;
                            }
                        }
                        if (!ok) break;
                        maxZ = z1;
                    }

                    // Expansion en Y
                    for (int y1 = y - lod; y1 >= 0; y1 -= lod) {
                        boolean ok = true;
                        for (int dx = x; dx <= maxX; dx += lod) {
                            for (int dz = z; dz <= maxZ; dz += lod) {
                                if (used[dx][y1][dz] || !chunk.getIsVisible()[dx][y1][dz] || !baseMat.equals(chunk.getMaterials()[dx][y1][dz])) {
                                    ok = false;
                                    break;
                                }
                            }
                            if (!ok) break;
                        }
                        if (!ok) break;
                        maxY = y1;
                    }

                    // Marquage des blocs comme utilisés
                    for (int dx = x; dx <= maxX; dx += lod) {
                        for (int dy = y; dy >= maxY; dy -= lod){
                            for (int dz = z; dz <= maxZ; dz += lod) {
                                used[dx][dy][dz] = true;
                            }
                        }
                    }

                    float worldX = chunk.getX() * CHUNK_SIZE + x;
                    float worldY = maxY;
                    float worldZ = chunk.getZ() * CHUNK_SIZE + z;

                    this.getRenderers().getFirst().getVertices().add(new float[] {
                            worldX - 0.5f, worldY, worldZ - 0.5f,
                            baseMat.getTexture().getId(),
                            maxX - x + lod, y - maxY + lod, maxZ - z + lod
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
