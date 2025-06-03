package fr.rhumun.game.worldcraftopengl.worlds.generators.biomes;

import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.worlds.generators.utils.Seed;

public class DesertBiome extends Biome {

    public DesertBiome() {
        super("Desert", 0f, Material.SAND, Material.SAND);
    }

    @Override
    public void spawnVegetation(Chunk chunk, Block block, int x, int z, Seed seed) {

    }
}
