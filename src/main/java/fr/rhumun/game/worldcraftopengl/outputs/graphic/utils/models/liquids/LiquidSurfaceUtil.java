package fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.models.liquids;

import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.ChunkRenderer;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.Renderer;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.content.materials.Material;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import static fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.models.liquids.LiquidsUtil.hasFluidAbove;
import static fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.models.liquids.LiquidsUtil.isToRender;

public class LiquidSurfaceUtil {


    protected static Block loadLiquidSurface(Chunk chunk, Block corner1, int X, int Y, int Z, LinkedHashSet<Block> blocks){
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
                if (testBlock.getMaterial() == corner1.getMaterial() && testBlock.getState() == corner1.getState()) {

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
                        if (testBlock.getMaterial() == corner1.getMaterial() && testBlock.getState() == corner1.getState()) {
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

    protected static void addFace(ArrayList<float[]> vertices, ArrayList<Integer> indices, float[][] face, int offset){
        for(float[] v : face) vertices.add(v);
        indices.add(offset);
        indices.add(offset+2);
        indices.add(offset+1);
        indices.add(offset+1);
        indices.add(offset+2);
        indices.add(offset+3);
    }

    protected static void rasterBlockGroup(Block corner1, Block corner2, ChunkRenderer chunkRenderer) {
        if(corner1.getState() == 0.0) return;

        float x1 = (float) corner1.getLocation().getX() - 0.5f;
        float y1 = (float) corner1.getLocation().getY() + 1f -0.1f;
        float z1 = (float) corner1.getLocation().getZ() - 0.5f;

        float x2 = (float) corner2.getLocation().getX() + 0.5f;
        float y2 = (float) corner2.getLocation().getY();
        float z2 = (float) corner2.getLocation().getZ() + 0.5f;

        float diff = (9 - (float) corner1.getState()) / 10f;
        y1 = Math.max(y1 - diff, 0f);

        Block east = corner1.getBlockAtEast();
        Block west = corner1.getBlockAtWest();
        Block north = corner1.getBlockAtNorth();
        Block south = corner1.getBlockAtSouth();

        float hNorth = getAdjHeight(north, corner1.getMaterial(), y1);
        float hSouth = getAdjHeight(south, corner1.getMaterial(), y1);
        float hWest = getAdjHeight(west, corner1.getMaterial(), y1);
        float hEast = getAdjHeight(east, corner1.getMaterial(), y1);

        float hNWC = north == null ? 0 : Math.max(y1, getAdjHeight(corner1.getBlockAtNorth().getBlockAtWest(), corner1.getMaterial(), y1));
        float hNEC = east == null ? 0 : Math.max(y1, getAdjHeight(corner1.getBlockAtEast().getBlockAtNorth(), corner1.getMaterial(), y1));
        float hSWC = south == null ? 0 : Math.max(y1, getAdjHeight(corner1.getBlockAtSouth().getBlockAtWest(), corner1.getMaterial(), y1));
        float hSEC = east == null ? 0 : Math.max(y1, getAdjHeight(corner1.getBlockAtEast().getBlockAtSouth(), corner1.getMaterial(), y1));

        float hNW = Math.max(y1, Math.max(hNorth, Math.max(hWest, hNWC)));
        float hNE = Math.max(y1, Math.max(hNorth, Math.max(hEast, hNEC)));
        float hSW = Math.max(y1, Math.max(hSouth, Math.max(hWest, hSWC)));
        float hSE = Math.max(y1, Math.max(hSouth, Math.max(hEast, hSEC)));


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
                {x1, hSE, z1, 0.0f, 0.0f, texIDTop, 0.0f, 1.0f, 0.0f},
                {x2, hNE, z1, texScaleX, 0.0f, texIDTop, 0.0f, 1.0f, 0.0f},
                {x1, hSW, z2, 0.0f, texScaleZ, texIDTop, 0.0f, 1.0f, 0.0f},
                {x2, hNW, z2, texScaleX, texScaleZ, texIDTop, 0.0f, 1.0f, 0.0f}
        };
        addFace(vertices, indices, faceTop, offset);
        offset += 4;

        if(showDown){
            float[][] faceBottom = new float[][]{
                    {x1, y2, z2, 0.0f, 0.0f, texIDBottom, 0.0f, -1.0f, 0.0f},
                    {x2, y2, z2, texScaleX, 0.0f, texIDBottom, 0.0f, -1.0f, 0.0f},
                    {x1, y2, z1, 0.0f, texScaleZ, texIDBottom, 0.0f, -1.0f, 0.0f},
                    {x2, y2, z1, texScaleX, texScaleZ, texIDBottom, 0.0f, -1.0f, 0.0f}
            };
            addFace(vertices, indices, faceBottom, offset);
            offset += 4;
        }

        if(showSouth){
            float[][] faceLeft = new float[][]{
                    {x1, y2, z2, 0.0f, 0.0f, texIDLeft, 1.0f, 0.0f, 0.0f},
                    {x1, y2, z1, texScaleZ, 0.0f, texIDLeft, 1.0f, 0.0f, 0.0f},
                    {x1, hSW, z2, 0.0f, texScaleY, texIDLeft, 1.0f, 0.0f, 0.0f},
                    {x1, hSE, z1, texScaleZ, texScaleY, texIDLeft, 1.0f, 0.0f, 0.0f}
            };
            addFace(vertices, indices, faceLeft, offset);
            offset += 4;
        }

        if(showNorth){
            float[][] faceRight = new float[][]{
                    {x2, y2, z1, 0.0f, 0.0f, texIDRight, -1.0f, 0.0f, 0.0f},
                    {x2, y2, z2, texScaleZ, 0.0f, texIDRight, -1.0f, 0.0f, 0.0f},
                    {x2, hNE, z1, 0.0f, texScaleY, texIDRight, -1.0f, 0.0f, 0.0f},
                    {x2, hNW, z2, texScaleZ, texScaleY, texIDRight, -1.0f, 0.0f, 0.0f}
            };
            addFace(vertices, indices, faceRight, offset);
            offset += 4;
        }

        if(showWest){
            float[][] faceBack = new float[][]{
                    {x2, y2, z2, texScaleX, 0.0f, texIDBack, 0.0f, 0.0f, 1.0f},
                    {x1, y2, z2, 0.0f, 0.0f, texIDBack, 0.0f, 0.0f, 1.0f},
                    {x2, hNW, z2, texScaleX, texScaleY, texIDBack, 0.0f, 0.0f, 1.0f},
                    {x1, hSW, z2, 0.0f, texScaleY, texIDBack, 0.0f, 0.0f, 1.0f}
            };
            addFace(vertices, indices, faceBack, offset);
            offset += 4;
        }

        if(showEast){
            float[][] faceFront = new float[][]{
                    {x1, y2, z1, 0.0f, 0.0f, texIDFront, 0.0f, 0.0f, -1.0f},
                    {x2, y2, z1, texScaleX, 0.0f, texIDFront, 0.0f, 0.0f, -1.0f},
                    {x1, hSE, z1, 0.0f, texScaleY, texIDFront, 0.0f, 0.0f, -1.0f},
                    {x2, hNE, z1, texScaleX, texScaleY, texIDFront, 0.0f, 0.0f, -1.0f}
            };
            addFace(vertices, indices, faceFront, offset);
            offset += 4;
        }

        renderer.addAllVertices(vertices.toArray(new float[0][]));
        renderer.addAllIndices(indices.stream().mapToInt(Integer::intValue).toArray());
    }

    private static float getAdjHeight(Block block, Material mat, float def) {
        if (block != null && block.getMaterial() == mat) {
            float yBase = (float) block.getLocation().getY() + 1f - 0.1f;
            float d = (9 - (float) block.getState()) / 10f;
            return Math.max(yBase - d, 0f);
        }
        return def;
    }

}
