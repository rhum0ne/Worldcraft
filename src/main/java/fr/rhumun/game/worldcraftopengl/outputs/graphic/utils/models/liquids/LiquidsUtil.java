package fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.models.liquids;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.models.BlockUtil;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.ChunkRenderer;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.Renderer;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import static fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.models.liquids.LiquidSurfaceUtil.loadLiquidSurface;
import static fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.models.liquids.LiquidSurfaceUtil.rasterBlockGroup;

public class LiquidsUtil {

    public static void loadDataFor(Block block, ChunkRenderer chunkRenderer, int X, int Y, int Z, LinkedHashSet<Block> blocks){
        Chunk chunk = chunkRenderer.getChunk();

        if(isFullyEnclosed(block))
            return;

        if(!Game.GREEDY_MESHING) {
            rasterBlockGroup(block, block, chunkRenderer);
            return;
        }

        if(hasFluidAbove(block)){
            LiquidBlockUtil.loadDataFor(block, chunkRenderer, X, Y, Z, blocks);
            return;
        }

        //Ici on a une surface
        Block corner1 = block;
        Block corner2 = loadLiquidSurface(chunk, corner1, X, Y, Z, blocks);

        rasterBlockGroup(corner1, corner2, chunkRenderer);
    }

    protected static boolean isToRender(Block block, Block counterBlock){
        return (counterBlock == null || counterBlock.isAir() || (!counterBlock.isOpaque() && !counterBlock.getMaterial().isLiquid()));
    }

    protected static boolean hasFluidAbove(Block block){
        Block up = block.getBlockAtUp();
        return up != null && up.getMaterial() == block.getMaterial();
    }

    protected static boolean isFullyEnclosed(Block block){
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
