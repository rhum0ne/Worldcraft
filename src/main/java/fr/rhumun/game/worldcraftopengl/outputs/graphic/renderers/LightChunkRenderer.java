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
import java.util.LinkedHashSet;

import static fr.rhumun.game.worldcraftopengl.Game.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;

@Getter
public class LightChunkRenderer extends AbstractChunkRenderer{

    private LightChunk chunk;

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

    public void updateData() {
        if(!chunk.isGenerated()) return;
        long start = System.currentTimeMillis();

        chunk.updateAllBlock();

        for (Renderer renderer : this.getRenderers()) {
            renderer.getVertices().clear();
            renderer.getIndices().clear();
            renderer.setIndice(0);
        }

        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < chunk.getWorld().getHeigth(); y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    if(!chunk.getIsVisible()[x][y][z]) continue;

                    Material mat = chunk.getMaterials()[x][y][z];
                    this.getRenderers().getFirst().getVertices().add(new float[]{chunk.getX()*CHUNK_SIZE+ x, y, chunk.getZ()*CHUNK_SIZE+z, mat.getTextureID()});
                    this.getRenderers().getFirst().setIndice(this.getRenderers().getFirst().getIndice()+1);
                }
            }
        }

        this.setVerticesNumber(0);

        for (Renderer renderer : this.getRenderers()) {
            renderer.toArrays();
            if(SHOWING_RENDERER_DATA) {
                int length = renderer.getVertices().size();
                //GAME.log("DATA LOADED: " + length + " floats");
                this.setVerticesNumber(this.getVerticesNumber()+length);
            }
        }

        long end = System.currentTimeMillis();
        GAME.debug("Finished updating data for " + chunk + " in " + (end - start) + " ms");
        this.setDataReady(true); // Marque les données comme prêtes
    }

    @Override
    public void render() {
        if(chunk.isToUpdate()) update();

        //System.out.println("RENDERING FAR CHUNK " + chunk.getX() + " " + chunk.getZ());
        glDisable(GL_CULL_FACE);

        this.getRenderers().getFirst().render();
        glEnable(GL_CULL_FACE);

    }

    @Override
    public void cleanup() {
        this.chunk = null;
    }
}
