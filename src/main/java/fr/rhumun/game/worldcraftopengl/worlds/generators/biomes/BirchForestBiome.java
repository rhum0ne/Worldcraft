package fr.rhumun.game.worldcraftopengl.worlds.generators.biomes;

import fr.rhumun.game.worldcraftopengl.content.materials.types.Materials;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.worlds.generators.utils.Seed;
import fr.rhumun.game.worldcraftopengl.worlds.generators.utils.trees.TreeType;

public class BirchForestBiome extends Biome{
    public BirchForestBiome() {
        super("Birch Forest", 0, Materials.GRASS_BLOCK, Materials.DIRT);
    }

    @Override
    public void spawnVegetation(Chunk chunk, Block block, int x, int z, Seed seed) {
        int chunkX = chunk.getX();
        int chunkZ = chunk.getZ();
        int y = (int) (block.getY() + 1);

        if ((1 + x * z + chunkZ + seed.get(5)) % (seed.get(7) + 3) == 0 && (chunkX + x + seed.getCombinaisonOf(1, 7, 2) * z + seed.get(6)) % 11 == 0) {
            chunk.get(x, y, z).setMaterial(Materials.GRASS);
        } else if ((1 + x + z + chunkZ + seed.get(3)) % (seed.get((x * x + z) % 9) + 4) == 0 && (chunkX * seed.get(2) + x + 3 * z) % 13 == 0) {
            chunk.get(x, y, z).setMaterial(Materials.BLUE_FLOWER);
        } else if ((1 + x + z + chunkZ + seed.get(8)) % 7 == 0 && (chunkX + x + 2 * z + seed.get(2) * seed.getCombinaisonOf(4, 3, 1)) % (seed.get((z + x * seed.get(1)) % 9) + 3) == 0) {
            chunk.get(x, y, z).setMaterial(Materials.RED_FLOWER);
        }

        // Arbres : Rareté modérée avec plus de chaos
        else if ((seed.get((x * chunkX + (z * chunkZ)) % 9) + x * z + seed.get(3)) % 11 == 0) {
            // Conditions simplifiées mais avec plus de variations
            if ((x * x + z * z + chunkX * chunkZ + seed.get(1)) % 23 == 0 ||
                    (seed.getCombinaisonOf(x % 5, z % 3, chunkX % 7) + chunkX + z) % 19 == 0) {
                TreeType.BIRCH.buildAt(chunk, x, y, z);
            }
        }
    }
}
