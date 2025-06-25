package fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.models;

import fr.rhumun.game.worldcraftopengl.content.items.ItemStack;
import fr.rhumun.game.worldcraftopengl.content.materials.Material;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.ChunkRenderer;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.Renderer;
import fr.rhumun.game.worldcraftopengl.entities.Location;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Slot;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.models.BlockUtil;
import fr.rhumun.game.worldcraftopengl.content.Mesh;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;

public class StairsUtils {

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

        int offset = renderer.getIndices().isEmpty() ? 0 : renderer.getIndices().getLast()+1;
        int[] indices = BlockUtil.createIndices(offset);
        renderer.addAllVertices(vertices);
        renderer.addAllIndices(indices);
    }

    private static void rasterBlock(Block block, ChunkRenderer chunkRenderer){
        float[] tex = getTextureIDs(block);
        Renderer renderer = chunkRenderer.getRenderers().get(block.getMaterial().getOpacity().getPriority());
        int state = block.getState();
        boolean top = (state & 4) != 0;
        int orientation = state & 3;

        float bx = (float) block.getLocation().getX();
        float by = (float) block.getLocation().getY();
        float bz = (float) block.getLocation().getZ();

        // base slab
        float bx1 = bx - 0.5f;
        float bx2 = bx + 0.5f;
        float bz1 = bz - 0.5f;
        float bz2 = bz + 0.5f;
        if(top){
            rasterBox(bx1, by +1f, bz1, bx2, by +0.5f, bz2, tex, renderer);
        }else{
            rasterBox(bx1, by +0.5f, bz1, bx2, by, bz2, tex, renderer);
        }

        float x1=bx1,x2=bx2,z1=bz1,z2=bz2;
        if(!top){
            switch (orientation){
                case 0 -> z2 = bz;
                case 1 -> x2 = bx;
                case 2 -> z1 = bz;
                case 3 -> x1 = bx;
            }
            rasterBox(x1, by +1f, z1, x2, by +0.5f, z2, tex, renderer);
        }else{
            switch (orientation){
                case 0 -> z2 = bz;
                case 1 -> x2 = bx;
                case 2 -> z1 = bz;
                case 3 -> x1 = bx;
            }
            rasterBox(x1, by +0.5f, z1, x2, by, z2, tex, renderer);
        }
    }

    // Items rendering
    public static void rasterBlockItem(ItemStack block, Slot slot, ArrayList<float[]> verticesList, ArrayList<Integer> indicesList){
        Mesh mesh = block.getModel().get();
        if(mesh == null) {
            BlockUtil.rasterBlockItem(block, slot, verticesList, indicesList);
            return;
        }

        FloatBuffer verticesBuffer = mesh.getVerticesBuffer().duplicate();
        FloatBuffer texCoordsBuffer = mesh.getTexCoordsBuffer().duplicate();
        IntBuffer indicesBuffer = mesh.getIndicesBuffer().duplicate();

        while(indicesBuffer.hasRemaining()) {
            int vertexIndex = indicesBuffer.get();

            float vx = (verticesBuffer.get(vertexIndex * 3) + 0.5f) * slot.getWidth() + slot.getX();
            float vy = verticesBuffer.get(vertexIndex * 3 + 1) * slot.getHeight() + slot.getY();
            float vz = verticesBuffer.get(vertexIndex * 3 + 2);

            float u = texCoordsBuffer.get(vertexIndex * 2);
            float v = 1 - texCoordsBuffer.get(vertexIndex * 2 + 1);

            verticesList.add(new float[]{vx, vy, vz, u, v, block.getMaterial().getTexture().getId()});
            indicesList.add(indicesList.isEmpty() ? 0 : indicesList.getLast() + 1);
        }
    }

    public static void rasterDroppedStairsItem(Location loc, Material mat, ArrayList<float[]> verticesList, ArrayList<Integer> indicesList){
        BlockUtil.rasterDroppedBlockItem(loc, mat, verticesList, indicesList);
    }
}
