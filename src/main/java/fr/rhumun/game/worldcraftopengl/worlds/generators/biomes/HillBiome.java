package fr.rhumun.game.worldcraftopengl.worlds.generators.biomes;

import fr.rhumun.game.worldcraftopengl.content.Block;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.worlds.generators.utils.Seed;
import fr.rhumun.game.worldcraftopengl.worlds.generators.utils.trees.TreeType;

public class HillBiome extends Biome {

    public HillBiome() {
        super("Hill", 0, Material.GRASS_BLOCK, Material.DIRT);
    }

    @Override
    public void spawnVegetation(Chunk chunk, Block block, int x, int z, Seed seed) {
        int chunkX = chunk.getX();
        int chunkZ = chunk.getZ();
        int y = (int) (block.getY() + 1);

        if ((1 + x * z + chunkZ + seed.get(5)) % (seed.get(7) + 3) == 0 && (chunkX + x + seed.getCombinaisonOf(1, 7, 2) * z + seed.get(6)) % 11 == 0) {
            chunk.get(x, y, z).setMaterial(Material.GRASS);
        } else if ((1 + x + z + chunkZ + seed.get(3)) % (seed.get((x * x + z) % 9) + 4) == 0 && (chunkX * seed.get(2) + x + 3 * z) % 13 == 0) {
            chunk.get(x, y, z).setMaterial(Material.BLUE_FLOWER);
        } else if ((1 + x + z + chunkZ + seed.get(8)) % 7 == 0 && (chunkX + x + 2 * z + seed.get(2) * seed.getCombinaisonOf(4, 3, 1)) % (seed.get((z + x * seed.get(1)) % 9) + 3) == 0) {
            chunk.get(x, y, z).setMaterial(Material.RED_FLOWER);
        }
    }


}
