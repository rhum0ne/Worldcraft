package fr.rhumun.game.worldcraftopengl.worlds.generators.biomes;

import fr.rhumun.game.worldcraftopengl.content.materials.types.Materials;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.worlds.generators.utils.Seed;

public class MesaBiome extends Biome {

    public MesaBiome() {
        super("Mesa", 0f, Materials.ORANGE_WOOL, Materials.BROWN_WOOL);
    }

    @Override
    public void spawnVegetation(Chunk chunk, Block block, int x, int z, Seed seed) {

    }
}
