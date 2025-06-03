package fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.models;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.ChunkRenderer;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.Renderer;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class LiquidsUtil {

    public static void loadDataFor(Block block, ChunkRenderer chunkRenderer, int X, int Y, int Z, LinkedHashSet<Block> blocks){
        Chunk chunk = chunkRenderer.getChunk();

        if(!Game.GREEDY_MESHING) {
            rasterBlockGroup(block, block, chunkRenderer);
            return;
        }

        Block corner1 = block;
        Block corner2 = loadLiquidSurface(chunk, corner1, X, Y, Z, blocks);

        rasterBlockGroup(corner1, corner2, chunkRenderer);
    }

    private static boolean isToRender(Block block, Block counterBlock){
        return counterBlock != null && !counterBlock.isOpaque() && block.getMaterial() != counterBlock.getMaterial();
    }

    private static Block loadLiquidSurface(Chunk chunk, Block corner1, int X, int Y, int Z, LinkedHashSet<Block> blocks){
        Block corner2 = corner1;
        Model model = corner1.getModel();

        int x=X;
        int z=Z;
        int y = Y;

        boolean hasBlockNorth = isToRender(corner1, corner1.getBlockAtNorth());
        boolean hasBlockSouth = isToRender(corner1, corner1.getBlockAtSouth());
        boolean hasBlockWest = isToRender(corner1, corner1.getBlockAtWest());
        boolean hasBlockEast = isToRender(corner1, corner1.getBlockAtEast());
        boolean hasBlockDown = isToRender(corner1, corner1.getBlockAtDown());

        for(x++; x<16; x++){
            Block testBlock = chunk.getBlocks()[x][y][z];
            if (testBlock.isSurrounded()) break;
            if (blocks.contains(testBlock)) break;

            if (testBlock.getModel() == model) {
                if (testBlock.getMaterial() == corner1.getMaterial()) {

                    if(isToRender(testBlock, testBlock.getBlockAtNorth()) != hasBlockNorth) break;
                    if(isToRender(testBlock, testBlock.getBlockAtSouth()) != hasBlockSouth) break;
                    if(isToRender(testBlock, testBlock.getBlockAtWest()) != hasBlockWest) break;
                    if(isToRender(testBlock, testBlock.getBlockAtEast()) != hasBlockEast) break;
                    if(isToRender(testBlock, testBlock.getBlockAtDown()) != hasBlockDown) break;

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
                    if (testBlock.getModel() == model) {
                        if (testBlock.getMaterial() == corner1.getMaterial()) {
                            if (!testBlock.isSurrounded()) {
                                if(isToRender(testBlock, testBlock.getBlockAtNorth()) != hasBlockNorth) break;
                                if(isToRender(testBlock, testBlock.getBlockAtSouth()) != hasBlockSouth) break;
                                if(isToRender(testBlock, testBlock.getBlockAtWest()) != hasBlockWest) break;
                                if(isToRender(testBlock, testBlock.getBlockAtEast()) != hasBlockEast) break;
                                if(isToRender(testBlock, testBlock.getBlockAtDown()) != hasBlockDown) break;

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

        return corner2;
    }

    private static void rasterBlockGroup(Block corner1, Block corner2, ChunkRenderer chunkRenderer) {
        // Coordonnées des coins (corner1 est en bas à gauche, corner2 est en haut à droite)
        float x1 = (float) corner1.getLocation().getX() - 0.5f; // Déplacer pour utiliser le coin avant-gauche
        float y1 = (float) corner1.getLocation().getY() + 1f; // Déplacer pour utiliser le coin bas
        float z1 = (float) corner1.getLocation().getZ() - 0.5f; // Déplacer pour utiliser le coin avant

        float x2 = (float) corner2.getLocation().getX() + 0.5f; // Déplacer pour utiliser le coin arrière-droit
        float y2 = (float) corner2.getLocation().getY(); // Déplacer pour utiliser le coin haut
        float z2 = (float) corner2.getLocation().getZ() + 0.5f; // Déplacer pour utiliser le coin arrière

//        boolean hasBlockNorth = isToRender(corner1, corner1.getBlockAtNorth());
//        boolean hasBlockSouth = isToRender(corner1, corner1.getBlockAtSouth());
//        boolean hasBlockWest = isToRender(corner1, corner1.getBlockAtWest());
//        boolean hasBlockEast = isToRender(corner1, corner1.getBlockAtEast());
//        boolean hasBlockDown = isToRender(corner1, corner1.getBlockAtDown());

        int i=1;
        //if(hasBlockNorth)i++;
//        if(hasBlockSouth)i++;
//        if(hasBlockWest)i++;
//        if(hasBlockEast)i++;
//        if(hasBlockDown)i++;

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

        float[][] vertices = new float[i*4][];

        int j=0;

        vertices[j++] = new float[]{x1, y1, z1, 0.0f, 0.0f, texIDTop, 0.0f, 1.0f, 0.0f}; // Bas gauche
        vertices[j++] = new float[]{x2, y1, z1, texScaleX, 0.0f, texIDTop, 0.0f, 1.0f, 0.0f}; // Bas droite
        vertices[j++] = new float[]{x1, y1, z2, 0.0f, texScaleZ, texIDTop, 0.0f, 1.0f, 0.0f}; // Haut gauche
        vertices[j++] = new float[]{x2, y1, z2, texScaleX, texScaleZ, texIDTop, 0.0f, 1.0f, 0.0f}; // Haut droite

//        if(hasBlockWest){
//            vertices[j++] = new float[]{x1, y1, z1, 0.0f, 0.0f, texIDFront, 0.0f, 0.0f, 1.0f};
//            vertices[j++] = new float[]{x2, y1, z1, texScaleX, 0.0f, texIDFront, 0.0f, 0.0f, 1.0f};
//            vertices[j++] = new float[]{x2, y2, z1, texScaleX, texScaleY, texIDFront, 0.0f, 0.0f, 1.0f};
//            vertices[j++] = new float[]{x1, y2, z1, 0.0f, texScaleY, texIDFront, 0.0f, 0.0f, 1.0f};
//        }

//        if(hasBlockEast){
//            vertices[j++] = new float[]{x1, y1, z2, 0.0f, 0.0f, texIDBack, 0.0f, 0.0f, -1.0f};
//            vertices[j++] = new float[]{x2, y1, z2, texScaleX, 0.0f, texIDBack, 0.0f, 0.0f, -1.0f};
//            vertices[j++] = new float[]{x2, y2, z2, texScaleX, texScaleY, texIDBack, 0.0f, 0.0f, -1.0f};
//            vertices[j++] = new float[]{x1, y2, z2, 0.0f, texScaleY, texIDBack, 0.0f, 0.0f, -1.0f};
//        }
//
//        if(hasBlockSouth){
//            vertices[j++] = new float[]{x1, y1, z1, 0.0f, 0.0f, texIDLeft, -1.0f, 0.0f, 0.0f};
//            vertices[j++] = new float[]{x1, y2, z1, texScaleZ, 0.0f, texIDLeft, -1.0f, 0.0f, 0.0f};
//            vertices[j++] = new float[]{x1, y1, z2, 0.0f, texScaleY, texIDLeft, -1.0f, 0.0f, 0.0f};
//            vertices[j++] = new float[]{x1, y2, z2, texScaleZ, texScaleY, texIDLeft, -1.0f, 0.0f, 0.0f};
//        }
//
//        if(hasBlockNorth){
//            vertices[j++] = new float[]{x2, y1, z1, 0.0f, 0.0f, texIDRight, 1.0f, 0.0f, 0.0f};
//            vertices[j++] = new float[]{x2, y2, z1, texScaleZ, 0.0f, texIDRight, 1.0f, 0.0f, 0.0f};
//            vertices[j++] = new float[]{x2, y1, z2, 0.0f, texScaleY, texIDRight, 1.0f, 0.0f, 0.0f};
//            vertices[j++] = new float[]{x2, y2, z2, texScaleZ, texScaleY, texIDRight, 1.0f, 0.0f, 0.0f};
//        }
//
//        if(hasBlockDown){
//            vertices[j++] = new float[]{x1, y2, z1, 0.0f, 0.0f, texIDBottom, 0.0f, -1.0f, 0.0f}; // Bas gauche
//            vertices[j++] = new float[]{x2, y2, z1, texScaleX, 0.0f, texIDBottom, 0.0f, -1.0f, 0.0f}; // Bas droite
//            vertices[j++] = new float[]{x1, y2, z2, 0.0f, texScaleZ, texIDBottom, 0.0f, -1.0f, 0.0f}; // Haut gauche
//            vertices[j++] = new float[]{x2, y2, z2, texScaleX, texScaleZ, texIDBottom, 0.0f, -1.0f, 0.0f}; // Haut droite
//        }

        Renderer renderer = chunkRenderer.getRenderers().get((corner1.getMaterial().getOpacity().getPriority()));
        int offset = (renderer.getIndices().isEmpty()) ? 0 : renderer.getIndices().getLast() + 1;

        int [] indices = new int[i*6];
        indices[0] = offset+0;
        indices[1] = offset+2;
        indices[2] = offset+1;
        indices[3] = offset+1;
        indices[4] = offset+2;
        indices[5] = offset+3;

        if(i>1){
            indices[6] = offset+4;
            indices[7] = offset+5;
            indices[8] = offset+6;
            indices[9] = offset+4;
            indices[10] = offset+6;
            indices[11] = offset+7;

            if(i>2){
                indices[12] = offset+8;
                indices[13] = offset+9;
                indices[14] = offset+10;
                indices[15] = offset+10;
                indices[16] = offset+9;
                indices[17] = offset+11;

                if(i>3){
                    indices[18] = offset+12;
                    indices[19] = offset+14;
                    indices[20] = offset+13;
                    indices[21] = offset+13;
                    indices[22] = offset+14;
                    indices[23] = offset+15;

                    if(i>4){
                        indices[24] = offset+18;
                        indices[25] = offset+17;
                        indices[26] = offset+16;
                        indices[27] = offset+19;
                        indices[28] = offset+17;
                        indices[29] = offset+18;

                        if(i>5) {
                            indices[30] = offset + 20;
                            indices[31] = offset + 21;
                            indices[32] = offset + 22;
                            indices[33] = offset + 22;
                            indices[34] = offset + 21;
                            indices[35] = offset + 23;
                        }
                    }
                }
            }
        }

        // Ajouter les données au renderer correspondant
        renderer.addAllVertices(vertices);
        renderer.addAllIndices(indices);
    }

}
