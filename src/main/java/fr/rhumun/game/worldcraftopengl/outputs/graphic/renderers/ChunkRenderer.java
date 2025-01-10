package fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers;

import fr.rhumun.game.worldcraftopengl.blocks.Block;
import fr.rhumun.game.worldcraftopengl.blocks.Mesh;
import fr.rhumun.game.worldcraftopengl.blocks.Model;
import fr.rhumun.game.worldcraftopengl.blocks.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderUtils;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.BlockUtil;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import lombok.Getter;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static fr.rhumun.game.worldcraftopengl.Game.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20C.glUseProgram;
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

        glUseProgram(ShaderUtils.GLOBAL_SHADERS.id);
        //glBindTexture(GL_TEXTURE_2D, TextureUtils.ATLAS);
        this.renderers.get(OpacityType.OPAQUE.getPriority()).render();

        if(this.renderers.get(OpacityType.LIQUID.getPriority()).getIndicesArray().length != 0){
            glEnable(GL_BLEND);
            //glDepthMask(false);

            glUseProgram(ShaderUtils.LIQUID_SHADER.id);
            this.renderers.get(OpacityType.LIQUID.getPriority()).render();

            //glDepthMask(true);
            glDisable(GL_BLEND);
        }
        if(this.renderers.get(OpacityType.TRANSPARENT.getPriority()).getIndicesArray().length != 0){
            glEnable(GL_BLEND);
            //glDepthMask(false);

            glUseProgram(ShaderUtils.GLOBAL_SHADERS.id);
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

        ArrayList<Block> blocks = new ArrayList<>();
        for (int X=0; X<chunk.getBlocks().length; X++ ) {
            for(int Y=chunk.getBlocks()[X].length-1; Y>0; Y--) {
                for(int Z=0; Z<chunk.getBlocks()[X][Y].length; Z++) {
                    Block block = chunk.getBlocks()[X][Y][Z];
                    if(blocks.contains(block)) continue;

                    if (block == null || block.getMaterial() == null) continue;

                    Model model = block.getModel();
                    if (model == null) continue;

                    Mesh obj = model.get();
                    if (obj == null) continue;

                    if (block.isSurrounded()) continue;

                    blocks.add(block);

                    if(model==Model.BLOCK){
                        Block corner1 = block;
                        Block corner2 = block;

                        int x=X;
                        int z=Z;
                        int y = Y;

                        for(x++; x<16; x++){
                            Block testBlock = chunk.getBlocks()[x][y][z];
                            if (blocks.contains(testBlock)) break;
                            if (testBlock.isSurrounded()) break;

                            if (testBlock.getModel() == Model.BLOCK) {
                                if (testBlock.getMaterial() == corner1.getMaterial()) {
                                    blocks.add(testBlock);
                                    corner2 = testBlock;
                                } else break;
                            } else break;
                        }

                        // Expansion en Z
                        boolean isGood = true;
                        for (z++; z < 16 && isGood; z++) {
                            ArrayList<Block> addedBlocks = new ArrayList<>();
                            Block lastBlock = corner2;

                            for(int xtest=X; xtest<=corner2.getChunkX(); xtest++){
                                Block testBlock = chunk.getBlocks()[xtest][y][z];

                                if (!blocks.contains(testBlock)) {
                                    if (testBlock.getModel() == Model.BLOCK) {
                                        if (testBlock.getMaterial() == corner1.getMaterial()) {
                                            if (!testBlock.isSurrounded()) {
                                                addedBlocks.add(testBlock);
                                                lastBlock = testBlock;

                                                continue;
                                            }
                                        }
                                    }
                                }
                                isGood = false;
                                break;
                            }

                            if(isGood){
                                blocks.addAll(addedBlocks);
                                corner2 = lastBlock;
                            }
                        }

                        // Expansion en Y
                        isGood = true;
                        for (y--; y>0 && isGood; y--) {
                            ArrayList<Block> addedBlocks = new ArrayList<>();
                            Block lastBlock = corner2;

                            for(int xtest=X; xtest<=corner2.getChunkX() && isGood; xtest++){
                                for(int ztest=corner1.getChunkZ(); ztest<=corner2.getChunkZ(); ztest++) {
                                    Block testBlock = chunk.getBlocks()[xtest][y][ztest];

                                    if (!blocks.contains(testBlock)) {
                                        if (testBlock.getModel() == Model.BLOCK) {
                                            if (testBlock.getMaterial() == corner1.getMaterial()) {
                                                if (!testBlock.isSurrounded()) {
                                                    addedBlocks.add(testBlock);
                                                    lastBlock = testBlock;
                                                    continue;
                                                }
                                            }
                                        }
                                    }
                                    isGood = false;
                                    break;
                                }
                            }

                            if(isGood){
                                blocks.addAll(addedBlocks);
                                corner2 = lastBlock;
                            }
                        }

                        rasterBlockGroup(corner1, corner2);
                    } else raster(block, obj);

                }
            }
        }

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

    private void rasterBlockGroup(Block corner1, Block corner2) {
        // Coordonnées des coins (corner1 est en bas à gauche, corner2 est en haut à droite)
        float x1 = (float) corner1.getLocation().getX() - 0.5f; // Déplacer pour utiliser le coin avant-gauche
        float y1 = (float) corner1.getLocation().getY() + 1f; // Déplacer pour utiliser le coin bas
        float z1 = (float) corner1.getLocation().getZ() - 0.5f; // Déplacer pour utiliser le coin avant

        float x2 = (float) corner2.getLocation().getX() + 0.5f; // Déplacer pour utiliser le coin arrière-droit
        float y2 = (float) corner2.getLocation().getY(); // Déplacer pour utiliser le coin haut
        float z2 = (float) corner2.getLocation().getZ() + 0.5f; // Déplacer pour utiliser le coin arrière

        //Tailles du regroupement de blocks:
        float texScaleX = x2-x1;
        float texScaleY = y1-y2;
        float texScaleZ = z2-z1;

        // Texture ID (supposé identique pour tous les blocs du groupe)
        float texIDFront = corner1.getMaterial().getMaterial().getFrontTexture().getId();
        float texIDBack = corner2.getMaterial().getMaterial().getBackTexture().getId();
        float texIDTop = corner1.getMaterial().getMaterial().getTopTexture().getId();
        float texIDBottom = corner2.getMaterial().getMaterial().getBottomTexture().getId();
        float texIDLeft = corner1.getMaterial().getMaterial().getLeftTexture().getId();
        float texIDRight = corner2.getMaterial().getMaterial().getRightTexture().getId();


        // Sommets du rectangle englobant (2 triangles par face)
        float[][] vertices = {
                // Face avant (2 triangles)
                {x1, y2, z1, 0.0f, 0.0f, texIDFront, 0.0f, 0.0f, 1.0f}, // Bas gauche
                {x2, y2, z1, texScaleX, 0.0f, texIDFront, 0.0f, 0.0f, 1.0f}, // Bas droite
                {x2, y1, z1, texScaleX, texScaleY, texIDFront, 0.0f, 0.0f, 1.0f}, // Haut droite
                {x1, y1, z1, 0.0f, texScaleY, texIDFront, 0.0f, 0.0f, 1.0f}, // Haut gauche

                // Face arrière (2 triangles)
                {x1, y2, z2, 0.0f, 0.0f, texIDBack, 0.0f, 0.0f, -1.0f}, // Bas gauche
                {x2, y2, z2, texScaleX, 0.0f, texIDBack, 0.0f, 0.0f, -1.0f}, // Bas droite
                {x2, y1, z2, texScaleX, texScaleY, texIDBack, 0.0f, 0.0f, -1.0f}, // Haut droite
                {x1, y1, z2, 0.0f, texScaleY, texIDBack, 0.0f, 0.0f, -1.0f}, // Haut gauche

                // Face gauche (2 triangles)
                {x1, y2, z1, 0.0f, 0.0f, texIDLeft, -1.0f, 0.0f, 0.0f}, // Bas gauche
                {x1, y2, z2, texScaleZ, 0.0f, texIDLeft, -1.0f, 0.0f, 0.0f}, // Bas droite
                {x1, y1, z1, 0.0f, texScaleY, texIDLeft, -1.0f, 0.0f, 0.0f}, // Haut gauche
                {x1, y1, z2, texScaleZ, texScaleY, texIDLeft, -1.0f, 0.0f, 0.0f}, // Haut droite

                // Face droite (2 triangles)
                {x2, y2, z1, 0.0f, 0.0f, texIDRight, 1.0f, 0.0f, 0.0f}, // Bas gauche
                {x2, y2, z2, texScaleZ, 0.0f, texIDRight, 1.0f, 0.0f, 0.0f}, // Bas droite
                {x2, y1, z1, 0.0f, texScaleY, texIDRight, 1.0f, 0.0f, 0.0f}, // Haut gauche
                {x2, y1, z2, texScaleZ, texScaleY, texIDRight, 1.0f, 0.0f, 0.0f}, // Haut droite

                // Face supérieure (2 triangles)
                {x1, y1, z1, 0.0f, 0.0f, texIDTop, 0.0f, 1.0f, 0.0f}, // Bas gauche
                {x2, y1, z1, texScaleX, 0.0f, texIDTop, 0.0f, 1.0f, 0.0f}, // Bas droite
                {x1, y1, z2, 0.0f, texScaleZ, texIDTop, 0.0f, 1.0f, 0.0f}, // Haut gauche
                {x2, y1, z2, texScaleX, texScaleZ, texIDTop, 0.0f, 1.0f, 0.0f}, // Haut droite

                // Face inférieure (2 triangles)
                {x1, y2, z1, 0.0f, 0.0f, texIDBottom, 0.0f, -1.0f, 0.0f}, // Bas gauche
                {x2, y2, z1, texScaleX, 0.0f, texIDBottom, 0.0f, -1.0f, 0.0f}, // Bas droite
                {x1, y2, z2, 0.0f, texScaleZ, texIDBottom, 0.0f, -1.0f, 0.0f}, // Haut gauche
                {x2, y2, z2, texScaleX, texScaleZ, texIDBottom, 0.0f, -1.0f, 0.0f}, // Haut droite
        };

        // Indices pour dessiner le rectangle
        Renderer renderer = this.renderers.get(corner1.getMaterial().getOpacity().getPriority());
        int offset = (renderer.getIndices().isEmpty()) ? 0 : renderer.getIndices().getLast() + 1;
        int[] indices = {
                // Face avant
                offset + 2, offset + 1, offset + 0,  // Triangle 1
                offset + 3, offset + 2, offset + 0,  // Triangle 2

                // Face arrière
                offset + 4, offset + 5, offset + 6,  // Triangle 1
                offset + 4, offset + 6, offset + 7,  // Triangle 2

                // Face gauche
                offset + 8, offset + 9, offset + 10,  // Triangle 1
                offset + 10, offset + 9, offset + 11, // Triangle 2 //FACE AVANT QUAND ON REJOINT LE JEU

                // Face droite
                offset + 12, offset + 14, offset + 13,  // Triangle 1
                offset + 14, offset + 15, offset + 13,  // Triangle 2 //FACE ARRIERE C4EST GOOD

                // Face haut
                offset + 18, offset + 17, offset + 16,  // Triangle 1
                offset + 19, offset + 17, offset + 18,  // Triangle 2

                // Face bas
                offset + 20, offset + 21, offset + 22,  // Triangle 1
                offset + 22, offset + 21, offset + 23   // Triangle 2
        };


        // Ajouter les données au renderer correspondant
        renderer.addAllVertices(vertices);
        renderer.addAllIndices(indices);
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

            Texture texture = block.getMaterial().getMaterial().getTextureFromFaceWithNormal(vx, vy, vz);
            // Coordonnées de texture
            float u = texCoordsBuffer.get(vertexIndex * 2);
            float v = texCoordsBuffer.get(vertexIndex * 2 + 1);

            // Ajoute le sommet dans le renderer approprié

            float[] vertexData = new float[]{vx, vy, vz, u, v, texture.getId(), nx, ny, nz};
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
