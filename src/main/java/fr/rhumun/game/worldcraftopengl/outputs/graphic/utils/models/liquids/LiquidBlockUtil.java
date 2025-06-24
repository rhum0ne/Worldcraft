package fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.models.liquids;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.ChunkRenderer;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.Renderer;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import static fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.models.liquids.LiquidsUtil.hasFluidAbove;
import static fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.models.liquids.LiquidsUtil.isToRender;

public class LiquidBlockUtil {

    public static void loadDataFor(Block block, ChunkRenderer chunkRenderer, int X, int Y, int Z, LinkedHashSet<Block> blocks){
        Chunk chunk = chunkRenderer.getChunk();
        boolean tempo = true;

        if(!Game.GREEDY_MESHING || tempo) {
            blocks.add(block);
            rasterLiquidGroup(block, block, chunkRenderer);
            return;
        }

        Block corner1 = block;
        Block corner2 = block;

        int x = X;
        int z = Z;
        int y = Y;

        for(x++; x < 16; x++){
            Block testBlock = chunk.getBlocks()[x][y][z];
            if (blocks.contains(testBlock)) break;
            if (canMerge(corner1, testBlock)) {
                blocks.add(testBlock);
                corner2 = testBlock;
            } else break;
        }

        boolean isGood = true;
        for(z++; z < 16 && isGood; z++){
            ArrayList<Block> addedBlocks = new ArrayList<>();
            Block lastBlock = corner2;

            for(int xtest = X; xtest <= corner2.getChunkX(); xtest++){
                Block testBlock = chunk.getBlocks()[xtest][y][z];
                if (canMerge(corner1, testBlock)) {
                    if (!blocks.contains(testBlock)) {
                        addedBlocks.add(testBlock);
                        lastBlock = testBlock;
                        continue;
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

        isGood = true;
        for(y--; y > 0 && isGood; y--){
            ArrayList<Block> addedBlocks = new ArrayList<>();
            Block lastBlock = corner2;

            for(int xtest = X; xtest <= corner2.getChunkX(); xtest++){
                for(int ztest = Z; ztest <= corner2.getChunkZ(); ztest++){
                    Block testBlock = chunk.getBlocks()[xtest][y][ztest];
                    if (canMerge(corner1, testBlock)) {
                        if (!blocks.contains(testBlock)) {
                            addedBlocks.add(testBlock);
                            lastBlock = testBlock;
                            continue;
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

        rasterLiquidGroup(corner1, corner2, chunkRenderer);
    }

    private static boolean canMerge(Block base, Block other) {
        return false;
//        return other.getModel() == base.getModel()
//                && other.getMaterialID() == base.getMaterialID()
//                && !hasFluidAbove(other)
//                && !other.isSurrounded();
    }

    private static void rasterLiquidGroup(Block corner1, Block corner2, ChunkRenderer chunkRenderer) {
        float x1 = (float) corner1.getLocation().getX() - 0.5f;
        float y1 = (float) corner1.getLocation().getY() + 1f;
        float z1 = (float) corner1.getLocation().getZ() - 0.5f;

        float x2 = (float) corner2.getLocation().getX() + 0.5f;
        float y2 = (float) corner2.getLocation().getY();
        float z2 = (float) corner2.getLocation().getZ() + 0.5f;

        boolean showNorth = isToRender(corner1, corner1.getBlockAtNorth());
        boolean showSouth = isToRender(corner1, corner1.getBlockAtSouth());
        boolean showWest  = isToRender(corner1, corner1.getBlockAtWest());
        boolean showEast  = isToRender(corner1, corner1.getBlockAtEast());
        boolean showDown  = isToRender(corner1, corner1.getBlockAtDown());
        boolean showUp    = isToRender(corner1, corner1.getBlockAtUp());

        float texScaleX = x2 - x1;
        float texScaleY = y1 - y2;
        float texScaleZ = z2 - z1;

        float texTop    = corner1.getMaterial().getTopTexture().getId();
        float texBottom = corner1.getMaterial().getBottomTexture().getId();
        float texLeft   = corner1.getMaterial().getLeftTexture().getId();
        float texRight  = corner1.getMaterial().getRightTexture().getId();
        float texFront  = corner1.getMaterial().getFrontTexture().getId();
        float texBack   = corner1.getMaterial().getBackTexture().getId();

        Renderer renderer = chunkRenderer.getRenderers().get(corner1.getMaterial().getOpacity().getPriority());
        int offset = (renderer.getIndices().isEmpty()) ? 0 : renderer.getIndices().getLast() + 1;

        ArrayList<float[]> vertices = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();

        // always show top face
        if (showUp) {
            addFace(vertices, indices, new float[][] {
                    {x1, y1, z1, 0.0f, 0.0f, texTop, 0, 1, 0},
                    {x2, y1, z1, texScaleX, 0.0f, texTop, 0, 1, 0},
                    {x1, y1, z2, 0.0f, texScaleZ, texTop, 0, 1, 0},
                    {x2, y1, z2, texScaleX, texScaleZ, texTop, 0, 1, 0}
            }, offset);
            offset += 4;
        }

        if(showDown){
            addFace(vertices, indices, new float[][] {
                    {x1, y2, z2, 0.0f, 0.0f, texBottom, 0, -1, 0},
                    {x2, y2, z2, texScaleX, 0.0f, texBottom, 0, -1, 0},
                    {x1, y2, z1, 0.0f, texScaleZ, texBottom, 0, -1, 0},
                    {x2, y2, z1, texScaleX, texScaleZ, texBottom, 0, -1, 0}
            }, offset);
            offset += 4;
        }

        if(showSouth){
            addFace(vertices, indices, new float[][] {
                    {x1, y2, z2, 0.0f, 0.0f, texLeft, 1, 0, 0},
                    {x1, y2, z1, texScaleZ, 0.0f, texLeft, 1, 0, 0},
                    {x1, y1, z2, 0.0f, texScaleY, texLeft, 1, 0, 0},
                    {x1, y1, z1, texScaleZ, texScaleY, texLeft, 1, 0, 0}
            }, offset);
            offset += 4;
        }

        if(showNorth){
            addFace(vertices, indices, new float[][] {
                    {x2, y2, z1, 0.0f, 0.0f, texRight, -1, 0, 0},
                    {x2, y2, z2, texScaleZ, 0.0f, texRight, -1, 0, 0},
                    {x2, y1, z1, 0.0f, texScaleY, texRight, -1, 0, 0},
                    {x2, y1, z2, texScaleZ, texScaleY, texRight, -1, 0, 0}
            }, offset);
            offset += 4;
        }

        if(showWest){
            addFace(vertices, indices, new float[][] {
                    {x2, y2, z2, texScaleX, 0.0f, texBack, 0, 0, 1},
                    {x1, y2, z2, 0.0f, 0.0f, texBack, 0, 0, 1},
                    {x2, y1, z2, texScaleX, texScaleY, texBack, 0, 0, 1},
                    {x1, y1, z2, 0.0f, texScaleY, texBack, 0, 0, 1}
            }, offset);
            offset += 4;
        }

        if(showEast){
            addFace(vertices, indices, new float[][] {
                    {x1, y2, z1, 0.0f, 0.0f, texFront, 0, 0, -1},
                    {x2, y2, z1, texScaleX, 0.0f, texFront, 0, 0, -1},
                    {x1, y1, z1, 0.0f, texScaleY, texFront, 0, 0, -1},
                    {x2, y1, z1, texScaleX, texScaleY, texFront, 0, 0, -1}
            }, offset);
        }

        renderer.addAllVertices(vertices.toArray(new float[0][]));
        renderer.addAllIndices(indices.stream().mapToInt(i -> i).toArray());
    }

    private static void addFace(ArrayList<float[]> vertices, ArrayList<Integer> indices, float[][] face, int offset){
        for(float[] v : face) vertices.add(v);
        indices.add(offset);     indices.add(offset + 2); indices.add(offset + 1);
        indices.add(offset + 1); indices.add(offset + 2); indices.add(offset + 3);
    }
}
