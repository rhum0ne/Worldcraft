package fr.rhumun.game.worldcraftopengl.worlds.fluid;

import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.worlds.World;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import java.util.Set;
import static fr.rhumun.game.worldcraftopengl.Game.GAME;

public class FluidSimulator {
    public static void update(World world){
        Set<Chunk> chunks = GAME.getGraphicModule().getLoadedChunks();
        for(Chunk chunk : chunks){
            if(!chunk.isToUpdate() || !chunk.isGenerated()) continue;
            simulateChunk(chunk);
            chunk.setToUpdate(false);
        }
    }

    private static void simulateChunk(Chunk chunk){
        Block[][][] blocks = chunk.getBlocks();
        for(int x=0; x<blocks.length; x++){
            for(int y=1; y<blocks[x].length; y++){
                for(int z=0; z<blocks[x][y].length; z++){
                    Block block = blocks[x][y][z];
                    if(block.getMaterial() == Material.WATER){
                        Block below = block.getBlockAtDown(false);
                        if(below != null && below.getMaterial() == null){
                            below.setMaterial(Material.WATER);
                            block.setMaterial(null);
                            continue;
                        }
                        spread(block, block.getBlockAtNorth(false));
                        spread(block, block.getBlockAtSouth(false));
                        spread(block, block.getBlockAtEast(false));
                        spread(block, block.getBlockAtWest(false));
                    }
                }
            }
        }
    }

    private static void spread(Block source, Block target){
        if(target != null && target.getMaterial() == null){
            target.setMaterial(Material.WATER);
        }
    }
}
