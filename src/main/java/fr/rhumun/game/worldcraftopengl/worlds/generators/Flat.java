package fr.rhumun.game.worldcraftopengl.worlds.generators;

import fr.rhumun.game.worldcraftopengl.blocks.Material;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.worlds.World;
import fr.rhumun.game.worldcraftopengl.worlds.structures.Structure;

public class Flat extends WorldGenerator {

    public Flat(World world) {
        super(world);
    }

    @Override
    public void generate(Chunk chunk){
        for(int x=0; x<16; x++){
            for(int z=0; z<16; z++){
                for(int y=1; y<10; y++) {
                    if(y==9) chunk.setBlock(x, y, z, Material.GRASS);
                    else if(y==8 || y==7) chunk.setBlock(x, y, z, Material.DIRT);
                    else chunk.setBlock(x, y, z, Material.STONE);
                }
            }
        }
    }

    @Override
    public void populate(Chunk chunk) {
        for(int x=0; x<16; x++){
            for(int z=0; z<16; z++){
                if((1+x+z+chunk.getZ())%5==0 && (chunk.getX()+x+2*z)%7==0)
                    this.getWorld().spawnStructure(Structure.TREE, 16*chunk.getX() + x, 10, 16*chunk.getZ() + z);

            }
        }
        chunk.setBlock(8, 10, 8, Material.COBBLE);
    }
}
