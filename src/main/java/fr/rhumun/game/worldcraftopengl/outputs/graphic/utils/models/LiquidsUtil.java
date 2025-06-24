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

        if(hasFluidAbove(block) || isFullyEnclosed(block))
            return;

        if(!Game.GREEDY_MESHING) {
            rasterBlockGroup(block, block, chunkRenderer);
            return;
        }

        Block corner1 = block;
        Block corner2 = loadLiquidSurface(chunk, corner1, X, Y, Z, blocks);

        rasterBlockGroup(corner1, corner2, chunkRenderer);
    }

    private static boolean isToRender(Block block, Block counterBlock){
        return counterBlock == null;
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
            if(hasFluidAbove(testBlock)) break;

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
                if(hasFluidAbove(testBlock)) { isGood = false; break; }

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

    private static void addFace(ArrayList<float[]> vertices, ArrayList<Integer> indices, float[][] face, int offset){
        for(float[] v : face) vertices.add(v);
        indices.add(offset);
        indices.add(offset+2);
        indices.add(offset+1);
        indices.add(offset+1);
        indices.add(offset+2);
        indices.add(offset+3);
    }

    private static void rasterBlockGroup(Block corner1, Block corner2, ChunkRenderer chunkRenderer) {
        float x1 = (float) corner1.getLocation().getX() - 0.5f;
        float y1 = (float) corner1.getLocation().getY() + 1f;
        float z1 = (float) corner1.getLocation().getZ() - 0.5f;

        float x2 = (float) corner2.getLocation().getX() + 0.5f;
        float y2 = (float) corner2.getLocation().getY();
        float z2 = (float) corner2.getLocation().getZ() + 0.5f;

        boolean showNorth = isToRender(corner1, corner1.getBlockAtNorth());
        boolean showSouth = isToRender(corner1, corner1.getBlockAtSouth());
        boolean showWest = isToRender(corner1, corner1.getBlockAtWest());
        boolean showEast = isToRender(corner1, corner1.getBlockAtEast());
        boolean showDown = isToRender(corner1, corner1.getBlockAtDown());

        float texScaleX = x2 - x1;
        float texScaleY = y1 - y2;
        float texScaleZ = z2 - z1;

        float texIDFront = corner1.getMaterial().getFrontTexture().getId();
        float texIDBack = corner2.getMaterial().getBackTexture().getId();
        float texIDTop = corner1.getMaterial().getTopTexture().getId();
        float texIDBottom = corner2.getMaterial().getBottomTexture().getId();
        float texIDLeft = corner1.getMaterial().getLeftTexture().getId();
        float texIDRight = corner2.getMaterial().getRightTexture().getId();

        Renderer renderer = chunkRenderer.getRenderers().get((corner1.getMaterial().getOpacity().getPriority()));
        int offset = (renderer.getIndices().isEmpty()) ? 0 : renderer.getIndices().getLast() + 1;

        ArrayList<float[]> vertices = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();

        float[][] faceTop = new float[][]{
                {x1, y1, z1, 0.0f, 0.0f, texIDTop, 0.0f, 1.0f, 0.0f},
                {x2, y1, z1, texScaleX, 0.0f, texIDTop, 0.0f, 1.0f, 0.0f},
                {x1, y1, z2, 0.0f, texScaleZ, texIDTop, 0.0f, 1.0f, 0.0f},
                {x2, y1, z2, texScaleX, texScaleZ, texIDTop, 0.0f, 1.0f, 0.0f}
        };
        addFace(vertices, indices, faceTop, offset);
        offset += 4;

        if(showDown){
            float[][] faceBottom = new float[][]{
                    {x1, y2, z1, 0.0f, 0.0f, texIDBottom, 0.0f, -1.0f, 0.0f},
                    {x2, y2, z1, texScaleX, 0.0f, texIDBottom, 0.0f, -1.0f, 0.0f},
                    {x1, y2, z2, 0.0f, texScaleZ, texIDBottom, 0.0f, -1.0f, 0.0f},
                    {x2, y2, z2, texScaleX, texScaleZ, texIDBottom, 0.0f, -1.0f, 0.0f}
            };
            addFace(vertices, indices, faceBottom, offset);
            offset += 4;
        }

        if(showSouth){
            float[][] faceLeft = new float[][]{
                    {x1, y2, z1, 0.0f, 0.0f, texIDLeft, 1.0f, 0.0f, 0.0f},
                    {x1, y2, z2, texScaleZ, 0.0f, texIDLeft, 1.0f, 0.0f, 0.0f},
                    {x1, y1, z1, 0.0f, texScaleY, texIDLeft, 1.0f, 0.0f, 0.0f},
                    {x1, y1, z2, texScaleZ, texScaleY, texIDLeft, 1.0f, 0.0f, 0.0f}
            };
            addFace(vertices, indices, faceLeft, offset);
            offset += 4;
        }

        if(showNorth){
            float[][] faceRight = new float[][]{
                    {x2, y2, z1, 0.0f, 0.0f, texIDRight, -1.0f, 0.0f, 0.0f},
                    {x2, y2, z2, texScaleZ, 0.0f, texIDRight, -1.0f, 0.0f, 0.0f},
                    {x2, y1, z1, 0.0f, texScaleY, texIDRight, -1.0f, 0.0f, 0.0f},
                    {x2, y1, z2, texScaleZ, texScaleY, texIDRight, -1.0f, 0.0f, 0.0f}
            };
            addFace(vertices, indices, faceRight, offset);
            offset += 4;
        }

        if(showWest){
            float[][] faceBack = new float[][]{
                    {x1, y2, z2, 0.0f, 0.0f, texIDBack, 0.0f, 0.0f, 1.0f},
                    {x2, y2, z2, texScaleX, 0.0f, texIDBack, 0.0f, 0.0f, 1.0f},
                    {x2, y1, z2, texScaleX, texScaleY, texIDBack, 0.0f, 0.0f, 1.0f},
                    {x1, y1, z2, 0.0f, texScaleY, texIDBack, 0.0f, 0.0f, 1.0f}
            };
            addFace(vertices, indices, faceBack, offset);
            offset += 4;
        }

        if(showEast){
            float[][] faceFront = new float[][]{
                    {x1, y2, z1, 0.0f, 0.0f, texIDFront, 0.0f, 0.0f, -1.0f},
                    {x2, y2, z1, texScaleX, 0.0f, texIDFront, 0.0f, 0.0f, -1.0f},
                    {x2, y1, z1, texScaleX, texScaleY, texIDFront, 0.0f, 0.0f, -1.0f},
                    {x1, y1, z1, 0.0f, texScaleY, texIDFront, 0.0f, 0.0f, -1.0f}
            };
            addFace(vertices, indices, faceFront, offset);
            offset += 4;
        }

        renderer.addAllVertices(vertices.toArray(new float[0][]));
        renderer.addAllIndices(indices.stream().mapToInt(Integer::intValue).toArray());
    }


    private static boolean hasFluidAbove(Block block){
        Block up = block.getBlockAtUp();
        return up != null && up.getMaterial() == block.getMaterial();
    }

    private static boolean isFullyEnclosed(Block block){
        Block matUp = block.getBlockAtUp();
        Block matDown = block.getBlockAtDown();
        Block matNorth = block.getBlockAtNorth();
        Block matSouth = block.getBlockAtSouth();
        Block matEast = block.getBlockAtEast();
        Block matWest = block.getBlockAtWest();

        if(matUp == null || matDown == null || matNorth == null || matSouth == null || matEast == null || matWest == null)
            return false;

        return matUp.getMaterial() == block.getMaterial() &&
                matDown.getMaterial() == block.getMaterial() &&
                matNorth.getMaterial() == block.getMaterial() &&
                matSouth.getMaterial() == block.getMaterial() &&
                matEast.getMaterial() == block.getMaterial() &&
                matWest.getMaterial() == block.getMaterial();
    }

}
