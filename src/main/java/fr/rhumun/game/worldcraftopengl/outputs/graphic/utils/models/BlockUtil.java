package fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.models;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.content.items.Item;
import fr.rhumun.game.worldcraftopengl.content.Block;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Slot;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.ChunkRenderer;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.Renderer;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;

import java.util.ArrayList;
import java.util.Arrays;

public class BlockUtil {

    public static void loadDataFor(Block block, ChunkRenderer chunkRenderer, int X, int Y, int Z, ArrayList<Block> blocks){
        Chunk chunk = chunkRenderer.getChunk();

        if(!Game.GREEDY_MESHING) {
            rasterBlockGroup(block, block, chunkRenderer);
            return;
        }

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

        rasterBlockGroup(corner1, corner2, chunkRenderer);
    }

    private static void rasterBlockGroup(Block corner1, Block corner2, ChunkRenderer chunkRenderer) {
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
        Renderer renderer = chunkRenderer.getRenderers().get((corner1.getMaterial().getOpacity().getPriority()));
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



    public static void rasterBlockItem(Item block, Slot slot, ArrayList<float[]> verticesList, ArrayList<Integer> indicesList) {
        // Coordonnées des coins (corner1 est en bas à gauche, corner2 est en haut à droite)
        float x1 = slot.getX(); // Déplacer pour utiliser le coin avant-gauche
        float y1 = slot.getY(); // Déplacer pour utiliser le coin bas
        float z1 = 0f; // Déplacer pour utiliser le coin avant

        float x2 = slot.getWidth() + slot.getX(); // Déplacer pour utiliser le coin arrière-droit
        float y2 = slot.getY() + slot.getHeigth(); // Déplacer pour utiliser le coin haut
        float z2 = 1f; // Déplacer pour utiliser le coin arrière

        //Tailles du regroupement de blocks:
        float texScaleX = 1f;
        float texScaleY = 1f;
        float texScaleZ = 1f;

        // Texture ID (supposé identique pour tous les blocs du groupe)
        float texIDFront = block.getMaterial().getMaterial().getFrontTexture().getId();
        float texIDBack = block.getMaterial().getMaterial().getBackTexture().getId();
        float texIDTop = block.getMaterial().getMaterial().getTopTexture().getId();
        float texIDBottom = block.getMaterial().getMaterial().getBottomTexture().getId();
        float texIDLeft = block.getMaterial().getMaterial().getLeftTexture().getId();
        float texIDRight = block.getMaterial().getMaterial().getRightTexture().getId();


        // Sommets du rectangle englobant (2 triangles par face)
        float[][] vertices = {
                // Face avant (2 triangles)
                {x1, y2, z1, 0.0f, 0.0f, texIDFront}, // Bas gauche
                {x2, y2, z1, texScaleX, 0.0f, texIDFront}, // Bas droite
                {x2, y1, z1, texScaleX, texScaleY, texIDFront}, // Haut droite
                {x1, y1, z1, 0.0f, texScaleY, texIDFront}, // Haut gauche

                // Face arrière (2 triangles)
                {x1, y2, z2, 0.0f, 0.0f, texIDBack}, // Bas gauche
                {x2, y2, z2, texScaleX, 0.0f, texIDBack}, // Bas droite
                {x2, y1, z2, texScaleX, texScaleY, texIDBack}, // Haut droite
                {x1, y1, z2, 0.0f, texScaleY, texIDBack}, // Haut gauche

                // Face gauche (2 triangles)
                {x1, y2, z1, 0.0f, 0.0f, texIDLeft}, // Bas gauche
                {x1, y2, z2, texScaleZ, 0.0f, texIDLeft}, // Bas droite
                {x1, y1, z1, 0.0f, texScaleY, texIDLeft}, // Haut gauche
                {x1, y1, z2, texScaleZ, texScaleY, texIDLeft}, // Haut droite

                // Face droite (2 triangles)
                {x2, y2, z1, 0.0f, 0.0f, texIDRight}, // Bas gauche
                {x2, y2, z2, texScaleZ, 0.0f, texIDRight}, // Bas droite
                {x2, y1, z1, 0.0f, texScaleY, texIDRight}, // Haut gauche
                {x2, y1, z2, texScaleZ, texScaleY, texIDRight}, // Haut droite

                // Face supérieure (2 triangles)
                {x1, y1, z1, 0.0f, 0.0f, texIDTop}, // Bas gauche
                {x2, y1, z1, texScaleX, 0.0f, texIDTop}, // Bas droite
                {x1, y1, z2, 0.0f, texScaleZ, texIDTop}, // Haut gauche
                {x2, y1, z2, texScaleX, texScaleZ, texIDTop}, // Haut droite

                // Face inférieure (2 triangles)
                {x1, y2, z1, 0.0f, 0.0f, texIDBottom}, // Bas gauche
                {x2, y2, z1, texScaleX, 0.0f, texIDBottom}, // Bas droite
                {x1, y2, z2, 0.0f, texScaleZ, texIDBottom}, // Haut gauche
                {x2, y2, z2, texScaleX, texScaleZ, texIDBottom}, // Haut droite
        };

        // Indices pour dessiner le rectangle
        int offset = (indicesList.isEmpty()) ? 0 : indicesList.getLast() + 1;
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
        addAllVertices(vertices, verticesList);
        addAllIndices(indices, indicesList);
    }

    private static void addAllIndices(int[] indices, ArrayList<Integer> indicesList){
        for(int i : indices) indicesList.add(i);
    }
    private static void addAllVertices(float[][] vertices, ArrayList<float[]> verticesList) {
        verticesList.addAll(Arrays.asList(vertices));
    }

}
