package fr.rhumun.game.worldcraftopengl.worlds.generators;

import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.worlds.World;

public class Normal extends WorldGenerator {

    private NoiseGenerator noise = new NoiseGenerator();

    public Normal(World world) {
        super(world);
    }

    @Override
    public void generate(Chunk chunk) {

    }

    @Override
    public void populate(Chunk chunk) {

    }
}
