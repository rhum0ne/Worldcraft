package fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers;

import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
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

        for (int x = 0; x < size; x += lod) {
            for (int z = 0; z < size; z += lod) {
                for (int y = height - 1; y >= 0; y -= lod) {

                    if (used[x][y][z] || !chunk.getIsVisible()[x][y][z]) continue;

                    Material baseMat = chunk.getMaterials()[x][y][z];
                    if (baseMat == null) continue;

                    int xM = lod, yM = lod, zM = lod;

                    // Expansion en Y d'abord
                    outerY:
                    for (int yTest = height -1 -lod; yTest >= 0; yTest -= lod) {
                        for (int dx = 0; dx < xM; dx += lod) {
                            for (int dz = 0; dz < zM; dz += lod) {
                                if (used[x + dx][yTest][z + dz]) break outerY;
                                Material m = chunk.getMaterials()[x + dx][yTest][z + dz];
                                if (m == null || !m.equals(baseMat)) break outerY;
                            }
                        }
                        yM += lod;
                    }

                    // Expansion en X
                    outerX:
                    for (int xTest = x + lod; xTest < size; xTest += lod) {
                        for (int dy = 0; dy < yM; dy += lod) {
                            for (int dz = 0; dz < zM; dz += lod) {
                                if (used[xTest][y + dy][z + dz]) break outerX;
                                Material m = chunk.getMaterials()[xTest][y + dy][z + dz];
                                if (m == null || !m.equals(baseMat)) break outerX;
                            }
                        }
                        xM += lod;
                    }

                    // Expansion en Z
                    outerZ:
                    for (int zTest = z + lod; zTest < size; zTest += lod) {
                        for (int dx = 0; dx < xM; dx += lod) {
                            for (int dy = 0; dy < yM; dy += lod) {
                                if (used[x + dx][y + dy][zTest]) break outerZ;
                                Material m = chunk.getMaterials()[x + dx][y + dy][zTest];
                                if (m == null || !m.equals(baseMat)) break outerZ;
                            }
                        }
                        zM += lod;
                    }

                    // Marque tout le bloc comme utilisé
                    for (int dx = 0; dx < xM; dx += lod) {
                        for (int dy = 0; dy < yM; dy += lod) {
                            for (int dz = 0; dz < zM; dz += lod) {
                                used[x + dx][y + dy][z + dz] = true;
                            }
                        }
                    }

                    float worldX = chunk.getX() * CHUNK_SIZE + x;
                    float worldY = y;
                    float worldZ = chunk.getZ() * CHUNK_SIZE + z;

                    this.getRenderers().getFirst().getVertices().add(new float[] {
                            worldX- 0.5f, worldY-1f, worldZ- 0.5f,
                            baseMat.getTextureID(),
                            xM, yM, zM
                    });
                    this.getRenderers().getFirst().setIndice(
                            this.getRenderers().getFirst().getIndice()+1
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
