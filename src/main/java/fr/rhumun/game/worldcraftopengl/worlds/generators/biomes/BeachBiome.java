package fr.rhumun.game.worldcraftopengl.worlds.generators.biomes;

import fr.rhumun.game.worldcraftopengl.content.Block;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.worlds.generators.utils.Seed;

public class BeachBiome extends Biome {

    public BeachBiome() {
        super("Beach", 0, Material.SAND, Material.STONE);
    }

    @Override
    public void spawnVegetation(Chunk chunk, Block block, int x, int z, Seed seed) {

    }
}
