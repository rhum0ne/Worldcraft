package fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.content.Block;
import fr.rhumun.game.worldcraftopengl.content.Mesh;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.models.BlockUtil;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.models.LiquidsUtil;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.models.SlabUtils;
import fr.rhumun.game.worldcraftopengl.worlds.LightChunk;
import lombok.Getter;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import static fr.rhumun.game.worldcraftopengl.Game.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;

@Getter
public class LightChunkRenderer extends AbstractChunkRenderer{

    private LightChunk chunk;
    private int lodLevel = 0;

    public LightChunkRenderer(LightChunk chunk){
        this.chunk = chunk;

        this.getRenderers().add(new FarRenderer(GAME.getGraphicModule()));
    }

    public void update() {
        if (!chunk.isGenerated() || (this.isDataUpdating() && !this.isDataReady())) return; // Vérifie que le chunk est prêt

        if (!this.isDataReady() && !this.isDataUpdating()) {
            // Lance le calcul dans un thread séparé
            setDataUpdating(true);
            GAME.getGraphicModule().getChunkLoader().updateDataFor(this);
        } else {
            // Les données sont prêtes, on peut mettre à jour le VAO
            updateVAO();
            chunk.setToUpdate(false);
            setDataUpdating(false);
            setDataReady(false); // Réinitialise pour la prochaine mise à jour
        }

//        updateData();
//        updateVAO();
//        chunk.setToUpdate(false);
    }


    public void updateVAO() {
        if(!this.isAreRenderersInitialized()) {
            for(Renderer renderer : this.getRenderers()) renderer.init();
            this.setAreRenderersInitialized(true);
        }

        for(Renderer renderer : this.getRenderers()) {
            glBindVertexArray(renderer.getVAO());

            glBindBuffer(GL_ARRAY_BUFFER, renderer.getVBO());
            glBufferData(GL_ARRAY_BUFFER, renderer.getVerticesArray().clone(), GL_DYNAMIC_DRAW);

            glBindVertexArray(0);

            GAME.debug("FINISHED VAO UPDATE FOR LIGHTCHUNK");
        }
    }

    @Override
    public void updateData() {
        if (!chunk.isGenerated() || lodLevel < 1) return;
        long start = System.currentTimeMillis();

        double dist = this.getDistanceFromPlayer();
        int lod = this.lodLevel;
        int size = CHUNK_SIZE;
        int height = chunk.getWorld().getHeigth();

        for (Renderer renderer : this.getRenderers()) {
            renderer.getVertices().clear();
            renderer.getIndices().clear();
            renderer.setIndice(0);
        }

        for (int x = 0; x < size; x += lod) {
            for (int z = 0; z < size; z += lod) {
                for (int y = 0; y < height; y += lod) {

                    // Sélection du matériau dominant
                    Material dominant = chunk.getMaterials()[x][y][z];
                    if(dominant==null) continue;

                    // Coordonnées globales
                    float worldX = chunk.getX() * CHUNK_SIZE + x;
                    float worldY = y;
                    float worldZ = chunk.getZ() * CHUNK_SIZE + z;

                    // Envoie un cube simplifié (ici juste 1 point avec texture ID, à remplacer par cube si besoin)
                    this.getRenderers().getFirst().getVertices().add(new float[]{
                            worldX, worldY, worldZ, dominant.getTextureID()
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
            if (SHOWING_RENDERER_DATA) {
                int length = renderer.getVertices().size();
                this.setVerticesNumber(this.getVerticesNumber() + length);
            }
        }

        long end = System.currentTimeMillis();
        GAME.debug("Finished updating full-volume LOD data in " + (end - start) + " ms");
        this.setDataReady(true);
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
            System.out.println("new lod is " + lod);
            this.lodLevel = lod;
            this.chunk.setToUpdate(true);
        }

    }
}
