package fr.rhumun.game.worldcraftopengl.worlds.generators.biomes;

import fr.rhumun.game.worldcraftopengl.content.Block;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.worlds.generators.utils.Seed;

public class PlainBiome extends Biome {

    public PlainBiome() {
        super("Plain", 0, Material.GRASS_BLOCK, Material.DIRT);
    }

    @Override
    public void spawnVegetation(Chunk chunk, Block block, int x, int z, Seed seed) {
        if((1+x*z+chunk.getZ())%(seed.get(7)+1)==0 && (chunk.getX()+x+seed.getCombinaisonOf(1,7,2)*z)%7==0)
            chunk.get(x, (int) (block.getY() + 1), z).setMaterial(Material.GRASS);
    }
}
