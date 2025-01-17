package fr.rhumun.game.worldcraftopengl.worlds.generators;

import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.worlds.World;
import lombok.Getter;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public abstract class WorldGenerator {

    private final World world;
    private final ExecutorService executor;

    public WorldGenerator(World world) {
        this.world = world;
        this.executor = Executors.newFixedThreadPool(8);
    }


    public void tryGenerate(Chunk chunk){
        if(chunk.isGenerated()) return;
        executor.submit(chunk::generate);

        /*for(Block block : chunk.getBlockList()){
            block.getSideBlocks();
        }

        for(int x=0; x<16; x++)
            for(int z=0; z<16; z++)
                chunk.get(x, world.getHeigth()-1, z).updateLight();
        */

        //chunk.setToUpdate(false);
    }

    public abstract void generate(Chunk chunk);
    public abstract void populate(Chunk chunk);

}
