package fr.rhumun.game.worldcraftopengl.worlds.generators;

import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.worlds.World;
import lombok.Getter;

@Getter
public abstract class WorldGenerator {

    private final World world;

    public WorldGenerator(World world) {
        this.world = world;
    }


    public void tryGenerate(Chunk chunk){
        if(chunk.isGenerated()) return;
        this.generate(chunk);
        this.populate(chunk);

        /*for(Block block : chunk.getBlockList()){
            block.getSideBlocks();
        }

        for(int x=0; x<16; x++)
            for(int z=0; z<16; z++)
                chunk.get(x, world.getHeigth()-1, z).updateLight();
        */

        //chunk.setToUpdate(false);
        chunk.setGenerated(true);
    }

    public abstract void generate(Chunk chunk);
    public abstract void populate(Chunk chunk);

}
