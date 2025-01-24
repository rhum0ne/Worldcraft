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

        // Plantes au sol : Herbes et fleurs
        if ((1 + x * z + chunkZ) % (seed.get(7) + 1) == 0 && (chunkX + x + seed.getCombinaisonOf(1, 7, 2) * z) % 7 == 0) {
            chunk.get(x, y, z).setMaterial(Material.GRASS);
        } else if ((1 + x + z + chunkZ) % (seed.get((x * x) % 9) + 1) == 0 && (chunkX + x + 2 * z) % 7 == 0) {
            chunk.get(x, y, z).setMaterial(Material.BLUE_FLOWER);
        } else if ((1 + x + z + chunkZ) % 5 == 0 && (chunkX + x + 2 * z) % (seed.get((z + x) % 9) + 1) == 0) {
            chunk.get(x, y, z).setMaterial(Material.RED_FLOWER);
        }

        // Arbres : Rareté modérée
        else if (seed.get((x + chunkX * chunkZ) % 9) >= 1 && seed.get((z + x) % 9) >= 3) {
            // Conditions simplifiées pour augmenter la probabilité
            if ((x + z + chunkX + chunkZ) % 11 == 0 || (x + chunkX + z) % 13 == 0) {
                TreeType.BIRCH.buildAt(chunk, x, y, z);
            }
        }
    }

}
