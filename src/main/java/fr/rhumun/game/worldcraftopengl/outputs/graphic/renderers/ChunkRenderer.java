package fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers;

import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.content.Mesh;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderManager;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.models.BlockUtil;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.models.LiquidsUtil;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.models.SlabUtils;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.GLStateManager;
import fr.rhumun.game.worldcraftopengl.worlds.AbstractChunk;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.worlds.LightChunk;
import lombok.Getter;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import static fr.rhumun.game.worldcraftopengl.Game.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;

@Getter
public class ChunkRenderer extends AbstractChunkRenderer{

    private Chunk chunk;

    public ChunkRenderer(Chunk chunk) {
        this.chunk = chunk;

        ArrayList<Renderer> renderers = this.getRenderers();
        renderers.add(new GlobalRenderer(GAME.getGraphicModule(), ShaderManager.GLOBAL_SHADERS));
        renderers.add(new GlobalRenderer(GAME.getGraphicModule(), ShaderManager.LIQUID_SHADER));
        renderers.add(new GlobalRenderer(GAME.getGraphicModule(), ShaderManager.GLOBAL_SHADERS));
        renderers.add(new GlobalRenderer(GAME.getGraphicModule(), ShaderManager.GLOBAL_SHADERS));
    }

    public static AbstractChunkRenderer createChunkRenderer(AbstractChunk abstractChunk) {
        if(abstractChunk instanceof Chunk chunk) return new ChunkRenderer(chunk);
        else if(abstractChunk instanceof LightChunk lightChunk) return new LightChunkRenderer(lightChunk);

        return null;
    }

    public synchronized void render() {

        if(chunk.isToUpdate()) update();

        GLStateManager.useProgram(ShaderManager.GLOBAL_SHADERS.id);
        GLStateManager.enable(GL_DEPTH_TEST);

        renderOpaque();

        if(this.getRenderers().get(OpacityType.LIQUID.getPriority()).getIndicesArray().length != 0){
            GLStateManager.enable(GL_BLEND);
            GLStateManager.useProgram(ShaderManager.LIQUID_SHADER.id);
            renderLiquids();
            GLStateManager.disable(GL_BLEND);
        }

        if(this.getRenderers().get(OpacityType.TRANSPARENT.getPriority()).getIndicesArray().length != 0){
            GLStateManager.enable(GL_BLEND);
            GLStateManager.useProgram(ShaderManager.GLOBAL_SHADERS.id);
            renderTransparent();
            GLStateManager.disable(GL_BLEND);
        }

        if(OpacityType.CLOSE_TRANSPARENT.getMaxChunkDistance() > this.getDistanceFromPlayer()
                && this.getRenderers().get(OpacityType.CLOSE_TRANSPARENT.getPriority()).getIndicesArray().length != 0){
            GLStateManager.enable(GL_BLEND);
            GLStateManager.useProgram(ShaderManager.GLOBAL_SHADERS.id);
            renderCloseTransparent();
            GLStateManager.disable(GL_BLEND);
        }
    }

    public synchronized void renderOpaque(){
        if(chunk.isToUpdate()) update();
        this.getRenderers().get(OpacityType.OPAQUE.getPriority()).render();
    }

    public synchronized void renderLiquids(){
        if(chunk.isToUpdate()) update();
        this.getRenderers().get(OpacityType.LIQUID.getPriority()).render();
    }

    public synchronized void renderTransparent(){
        if(chunk.isToUpdate()) update();
        this.getRenderers().get(OpacityType.TRANSPARENT.getPriority()).render();
    }

    public synchronized void renderCloseTransparent(){
        if(chunk.isToUpdate()) update();
        this.getRenderers().get(OpacityType.CLOSE_TRANSPARENT.getPriority()).render();
    }
    public synchronized void update() {
        if (!chunk.isGenerated()) return;
        chunk.setToUpdate(false);
        updateData();
        updateVAO();
    }


    public synchronized void updateVAO() {
        if(!this.isAreRenderersInitialized()) {
            for(Renderer renderer : this.getRenderers()) renderer.init();
            this.setAreRenderersInitialized(true);
        }

        this.getRenderers().getFirst().getGraphicModule().getLightningsUtils().updateLights();

        for(Renderer renderer : this.getRenderers()) {
            glBindVertexArray(renderer.getVAO());

            glBindBuffer(GL_ARRAY_BUFFER, renderer.getVBO());
            glBufferData(GL_ARRAY_BUFFER, renderer.getVerticesArray().clone(), GL_STATIC_DRAW);

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, renderer.getEBO());
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, renderer.getIndicesArray().clone(), GL_STATIC_DRAW);

            glBindVertexArray(0);
        }
    }

    public void updateData() {
        if(!chunk.isGenerated()) return;

        long start = System.currentTimeMillis();

        for (Renderer renderer : this.getRenderers()) {
            renderer.getVertices().clear();
            renderer.getIndices().clear();
            renderer.setIndice(0);
        }

        LinkedHashSet<Block> blocks = new LinkedHashSet<>();
        if(chunk.getBlocks() == null) {
            this.setVerticesNumber(0);

            for (Renderer renderer : this.getRenderers()) {
                renderer.toArrays();
                if(SHOWING_RENDERER_DATA) {
                    int length = renderer.getVertices().size();
                    //GAME.log("DATA LOADED: " + length + " floats");
                    this.setVerticesNumber(this.getVerticesNumber()+length);
                }
            }
            return;
        }

        for (int X=0; X<chunk.getBlocks().length; X++ ) {
            for(int Y=chunk.getBlocks()[X].length-1; Y>=0; Y--) {
                for(int Z=0; Z<chunk.getBlocks()[X][Y].length; Z++) {

                    Block block = chunk.getBlocks()[X][Y][Z];
                    if (block == null || block.getMaterial() == null) continue;

                    Model model = block.getModel();
                    if (model == null) continue;

                    if (block.isSurrounded()) continue;

                    if(blocks.contains(block)) continue;

                    blocks.add(block);

                    if(block.getMaterial().isLiquid()) LiquidsUtil.loadDataFor(block, this, X, Y,Z,blocks);
                    else if(model==Model.BLOCK) BlockUtil.loadDataFor(block, this, X, Y, Z, blocks);
                    else if(model==Model.SLAB) SlabUtils.loadDataFor(block, this, X, Y, Z, blocks);
                    else raster(block, model);

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
    }


    private void raster(Block block, Model model) {
        Mesh obj = model.get();
        if (obj == null) return;

        FloatBuffer verticesBuffer = obj.getVerticesBuffer().duplicate();
        FloatBuffer normalsBuffer = obj.getNormalsBuffer().duplicate();
        FloatBuffer texCoordsBuffer = obj.getTexCoordsBuffer().duplicate();
        IntBuffer indicesBuffer = obj.getIndicesBuffer().duplicate();

        double x = block.getLocation().getX();
        double y = block.getLocation().getY();
        double z = block.getLocation().getZ();

        //if(isBlock(block, x, y ,z)) return;

        while (indicesBuffer.hasRemaining()) {
            int vertexIndex = indicesBuffer.get();

            // Normales
            float nx = normalsBuffer.get(vertexIndex * 3);
            float ny = normalsBuffer.get(vertexIndex * 3 + 1);
            float nz = normalsBuffer.get(vertexIndex * 3 + 2);

            // Ignore les faces cachées
            if (block.hasBlockAtFace(nx, ny, nz)) continue;

            // Position du sommet
            float vx = (float) (x + verticesBuffer.get(vertexIndex * 3));
            float vy = (float) (y + verticesBuffer.get(vertexIndex * 3 + 1));
            float vz = (float) (z + verticesBuffer.get(vertexIndex * 3 + 2));

            Texture texture = block.getMaterial().getTextureFromFaceWithNormal(nx, ny, nz);
            // Coordonnées de texture
            float u = texCoordsBuffer.get(vertexIndex * 2);
            float v = texCoordsBuffer.get(vertexIndex * 2 + 1);

            // Ajoute le sommet dans le renderer approprié

            float[] vertexData = new float[]{vx, vy, vz, u, v, texture.getId(), nx, ny, nz};
            this.addVertex(this.getRenderers().get(block.getMaterial().getOpacity().getPriority()), vertexData);
        }
    }

    private void addVertex(Renderer renderer, float[] vertexData) {
        renderer.getVertices().add(vertexData);
        renderer.addIndice();
    }

    public void cleanup(){
        this.chunk = null;
    }
}
