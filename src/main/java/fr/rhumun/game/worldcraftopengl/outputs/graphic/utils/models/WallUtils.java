package fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.models;

import fr.rhumun.game.worldcraftopengl.content.items.ItemStack;
import fr.rhumun.game.worldcraftopengl.content.materials.Material;
import fr.rhumun.game.worldcraftopengl.entities.Location;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Slot;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.ChunkRenderer;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.Renderer;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class WallUtils {

    public static void loadDataFor(Block block, ChunkRenderer chunkRenderer, int X, int Y, int Z, LinkedHashSet<Block> blocks){
        rasterBlock(block, chunkRenderer);
    }

    private static float[] getTextureIDs(Block block) {
        if(block.getMaterial() instanceof fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.RotableMaterial){
            int state = block.getState() & 3;
            return new float[]{
                    block.getMaterial().getTextures()[(2 + state)%4].getId(),
                    block.getMaterial().getTextures()[(state)%4].getId(),
                    block.getMaterial().getTextures()[(1 + state)%4].getId(),
                    block.getMaterial().getTextures()[(3 + state)%4].getId(),
                    block.getMaterial().getTopTexture().getId(),
                    block.getMaterial().getBottomTexture().getId()
            };
        }
        return new float[]{
                block.getMaterial().getFrontTexture().getId(),
                block.getMaterial().getBackTexture().getId(),
                block.getMaterial().getLeftTexture().getId(),
                block.getMaterial().getRightTexture().getId(),
                block.getMaterial().getTopTexture().getId(),
                block.getMaterial().getBottomTexture().getId()
        };
    }

    private static void rasterBox(float x1, float y1, float z1, float x2, float y2, float z2, float[] texIDs, Renderer renderer){
        float texScaleX = x2 - x1;
        float texScaleY = y1 - y2;
        float texScaleZ = z2 - z1;

        float[][] vertices = new float[][]{
                {x1, y2, z1, 0.0f, 0.0f, texIDs[0], 0.0f, 0.0f, -1.0f},
                {x2, y2, z1, texScaleX, 0.0f, texIDs[0], 0.0f, 0.0f, -1.0f},
                {x2, y1, z1, texScaleX, texScaleY, texIDs[0], 0.0f, 0.0f, -1.0f},
                {x1, y1, z1, 0.0f, texScaleY, texIDs[0], 0.0f, 0.0f, -1.0f},

                {x1, y2, z2, 0.0f, 0.0f, texIDs[1], 0.0f, 0.0f, 1.0f},
                {x2, y2, z2, texScaleX, 0.0f, texIDs[1], 0.0f, 0.0f, 1.0f},
                {x2, y1, z2, texScaleX, texScaleY, texIDs[1], 0.0f, 0.0f, 1.0f},
                {x1, y1, z2, 0.0f, texScaleY, texIDs[1], 0.0f, 0.0f, 1.0f},

                {x1, y2, z1, 0.0f, 0.0f, texIDs[2], 1.0f, 0.0f, 0.0f},
                {x1, y2, z2, texScaleZ, 0.0f, texIDs[2], 1.0f, 0.0f, 0.0f},
                {x1, y1, z1, 0.0f, texScaleY, texIDs[2], 1.0f, 0.0f, 0.0f},
                {x1, y1, z2, texScaleZ, texScaleY, texIDs[2], 1.0f, 0.0f, 0.0f},

                {x2, y2, z1, 0.0f, 0.0f, texIDs[3], -1.0f, 0.0f, 0.0f},
                {x2, y2, z2, texScaleZ, 0.0f, texIDs[3], -1.0f, 0.0f, 0.0f},
                {x2, y1, z1, 0.0f, texScaleY, texIDs[3], -1.0f, 0.0f, 0.0f},
                {x2, y1, z2, texScaleZ, texScaleY, texIDs[3], -1.0f, 0.0f, 0.0f},

                {x1, y1, z1, 0.0f, 0.0f, texIDs[4], 0.0f, 1.0f, 0.0f},
                {x2, y1, z1, texScaleX, 0.0f, texIDs[4], 0.0f, 1.0f, 0.0f},
                {x1, y1, z2, 0.0f, texScaleZ, texIDs[4], 0.0f, 1.0f, 0.0f},
                {x2, y1, z2, texScaleX, texScaleZ, texIDs[4], 0.0f, 1.0f, 0.0f},

                {x1, y2, z1, 0.0f, 0.0f, texIDs[5], 0.0f, -1.0f, 0.0f},
                {x2, y2, z1, texScaleX, 0.0f, texIDs[5], 0.0f, -1.0f, 0.0f},
                {x1, y2, z2, 0.0f, texScaleZ, texIDs[5], 0.0f, -1.0f, 0.0f},
                {x2, y2, z2, texScaleX, texScaleZ, texIDs[5], 0.0f, -1.0f, 0.0f}
        };

        int offset = renderer.getIndices().isEmpty() ? 0 : renderer.getIndices().getLast() + 1;
        int[] indices = BlockUtil.createIndices(offset);
        renderer.addAllVertices(vertices);
        renderer.addAllIndices(indices);
    }

    private static void rasterBlock(Block block, ChunkRenderer chunkRenderer){
        float[] tex = getTextureIDs(block);
        Renderer renderer = chunkRenderer.getRenderers().get(block.getMaterial().getOpacity().getPriority());
        int orientation = block.getState() & 3;

        float bx = block.getLocation().getX();
        float by = block.getLocation().getY();
        float bz = block.getLocation().getZ();

        float x1 = bx - 0.5f;
        float x2 = bx + 0.5f;
        float z1 = bz - 0.5f;
        float z2 = bz + 0.5f;

        if(orientation % 2 == 0){
            z1 = bz - 0.25f;
            z2 = bz + 0.25f;
        }else{
            x1 = bx - 0.25f;
            x2 = bx + 0.25f;
        }

        rasterBox(x1, by +1f, z1, x2, by, z2, tex, renderer);
    }

    // Items rendering
    public static void rasterBlockItem(ItemStack block, Slot slot, ArrayList<float[]> verticesList, ArrayList<Integer> indicesList){
        BlockUtil.rasterBlockItem(block, slot, verticesList, indicesList);
    }

    public static void rasterDroppedWallItem(Location loc, Material mat, ArrayList<float[]> verticesList, ArrayList<Integer> indicesList){
        BlockUtil.rasterDroppedBlockItem(loc, mat, verticesList, indicesList);
    }
}
