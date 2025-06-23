package fr.rhumun.game.worldcraftopengl.worlds.generators.biomes;

import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.worlds.generators.utils.Seed;
import lombok.Getter;

@Getter
public abstract class Biome {

    private final String name;
    private final float SmallPeaks;

    private final Material top;
    private final Material secondary;

    public Biome(String name, float smallPeaks, Material top, Material secondary) {
        this.name = name;
        SmallPeaks = smallPeaks;
        this.top = top;
        this.secondary = secondary;
    }

    public abstract void spawnVegetation(Chunk chunk, Block block, int x, int z, Seed seed);

}
