package fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers;

import fr.rhumun.game.worldcraftopengl.blocks.Block;
import fr.rhumun.game.worldcraftopengl.blocks.Mesh;
import fr.rhumun.game.worldcraftopengl.blocks.Model;
import fr.rhumun.game.worldcraftopengl.blocks.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.blocks.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.BlockUtil;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import lombok.Getter;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import static fr.rhumun.game.worldcraftopengl.Game.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;

@Getter
public class ChunkRenderer {

    /**
     * POUR OPTIMISER LE CHARGEMENT: SEPARER LA CREATION DU TABLEAU ET SA MISE DANS LE VAO
     * POUR PERMETTRE DE REALISER LE CALCUL LONG HORS DE OPENGL
     * PUIS DE SIMPLEMENT TRANSFERER LES DONNEES QUAND CELUI-CI EST PRET
     *
     * NE PAS OUBLIER: UN CHUNK MODIFIE DOIT S'UPDATE IMMEDIATEMENT DONC FAIRE LES 2 ETAPES
     */

    private final Chunk chunk;

    private final ArrayList<Renderer> renderers = new ArrayList<>();
    //private final GlobalRenderer globalRenderer = new GlobalRenderer(GAME.getGraphicModule());
    //private final GlobalRenderer transparentBlocksRenderer = new GlobalRenderer(GAME.getGraphicModule());

    private boolean areRenderersInitialized = false;

    private boolean isDataUpdating = false;
    private boolean isDataReady = false;

    private int verticesNumber;


    public ChunkRenderer(Chunk chunk) {
        this.chunk = chunk;
        this.renderers.add(new GlobalRenderer(GAME.getGraphicModule()));
        this.renderers.add(new GlobalRenderer(GAME.getGraphicModule()));
        this.renderers.add(new GlobalRenderer(GAME.getGraphicModule()));
    }

    public void render() {

        if(chunk.isToUpdate()) update();

        //System.out.println("Rendering chunk " + chunk);

        this.renderers.get(OpacityType.OPAQUE.getPriority()).render();
        if(this.renderers.get(OpacityType.LIQUID.getPriority()).getIndicesArray().length != 0){
            glEnable(GL_BLEND);
            //glDepthMask(false);

            this.renderers.get(OpacityType.LIQUID.getPriority()).render();

            //glDepthMask(true);
            glDisable(GL_BLEND);
        }
        if(this.renderers.get(OpacityType.TRANSPARENT.getPriority()).getIndicesArray().length != 0){
            glEnable(GL_BLEND);
            //glDepthMask(false);

            this.renderers.get(OpacityType.TRANSPARENT.getPriority()).render();

            //glDepthMask(true);
            glDisable(GL_BLEND);
        }
    }
    private void update() {
        if (!chunk.isGenerated() || (isDataUpdating && !isDataReady)) return; // Vérifie que le chunk est prêt

        if (!isDataReady && !isDataUpdating) {
            // Lance le calcul dans un thread séparé
            isDataUpdating = true;
            Thread th = new Thread(() -> {
                updateData();
                isDataReady = true; // Marque les données comme prêtes
            });
            th.start();
        } else {
            // Les données sont prêtes, on peut mettre à jour le VAO
            updateVAO();
            chunk.setToUpdate(false);
            isDataUpdating = false;
            isDataReady = false; // Réinitialise pour la prochaine mise à jour
        }

//        updateData();
//        updateVAO();
//        chunk.setToUpdate(false);
    }


    public void updateVAO() {
        if(!areRenderersInitialized) {
            for(Renderer renderer : this.renderers) renderer.init();
            this.areRenderersInitialized = true;
        }

        this.renderers.getFirst().getGraphicModule().updateLights();

        for(Renderer renderer : this.renderers) {
            glBindVertexArray(renderer.getVAO());

            glBindBuffer(GL_ARRAY_BUFFER, renderer.getVBO());
            glBufferData(GL_ARRAY_BUFFER, renderer.getVerticesArray().clone(), GL_STATIC_DRAW);

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, renderer.getEBO());
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, renderer.getIndicesArray().clone(), GL_STATIC_DRAW);

            glBindVertexArray(0);
        }
    }

    public void updateData() {
        for (Renderer renderer : this.renderers) {
            renderer.getVertices().clear();
            renderer.getIndices().clear();
            renderer.setIndice(0);
        }

        ArrayList<Block> blocks = new ArrayList<>(chunk.getVisibleBlock());
//        Stack<Block> blocks = new Stack<>();
//        blocks.addAll(chunk.getVisibleBlock());
        for (Block block : blocks) {
            if (block == null || block.getMaterial() == null) continue;

            Model model = block.getModel();
            if (model == null) continue;

            Mesh obj = model.get();
            if (obj == null) continue;

            if (block.isSurrounded()) continue;

            raster(block, obj);
        }

//        for (Block block = blocks.pop() ; !blocks.isEmpty(); block = blocks.pop() ) {
//            if (block == null || block.getMaterial() == null) continue;
//
//            Model model = block.getModel();
//            if (model == null) continue;
//
//            Mesh obj = model.get();
//            if (obj == null) continue;
//
//            if (block.isSurrounded()) continue;
//
//            raster(block, obj);
//        }

        this.verticesNumber = 0;

        for (Renderer renderer : this.renderers) {
            renderer.toArrays();
            if(SHOWING_RENDERER_DATA) {
                int length = renderer.getVertices().size();
                //GAME.log("DATA LOADED: " + length + " floats");
                this.verticesNumber += length;
            }
        }
    }


    private void raster(Block block, Mesh obj) {
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

            // Coordonnées de texture
            float u = texCoordsBuffer.get(vertexIndex * 2);
            float v = texCoordsBuffer.get(vertexIndex * 2 + 1);

            // Ajoute le sommet dans le renderer approprié

            float[] vertexData = new float[]{vx, vy, vz, u, v, block.getMaterial().getTextureID(), nx, ny, nz};
            this.addVertex(this.renderers.get(block.getMaterial().getOpacity().getPriority()), vertexData);
        }
    }

    private boolean isBlock(Block block, double x, double y, double z) {
        if(block.getModel() != Model.BLOCK) return false;
        Renderer renderer = this.renderers.get(block.getMaterial().getOpacity().getPriority());

        for(int face=0; face<6; face++){
            for(int v=0; v<4; v++){
                float[] vertex = new float[9];
                int start = face*8*4+v*8;

                float nx = BlockUtil.verticesWithAttributes[start+3];
                float ny = BlockUtil.verticesWithAttributes[start+4];
                float nz = BlockUtil.verticesWithAttributes[start+5];

                //if (block.hasBlockAtFace(nx, ny, nz)) continue;

                vertex[0] = (float) (x + BlockUtil.verticesWithAttributes[start]);
                vertex[1] = (float) (y + BlockUtil.verticesWithAttributes[start+1]);
                vertex[2] = (float) (z + BlockUtil.verticesWithAttributes[start+2]);
                vertex[3] = (BlockUtil.verticesWithAttributes[start+6]);
                vertex[4] = (BlockUtil.verticesWithAttributes[start+7]);
                vertex[5] = (block.getMaterial().getTextureID());
                vertex[6] = nx;
                vertex[7] = ny;
                vertex[8] = nz;

                renderer.getVertices().add(vertex);
            }
        }

        renderer.addRawIndices(BlockUtil.indices);

        return true;
    }

    private void addVertex(Renderer renderer, float[] vertexData) {
        renderer.getVertices().add(vertexData);
        renderer.addIndice();
    }
}
