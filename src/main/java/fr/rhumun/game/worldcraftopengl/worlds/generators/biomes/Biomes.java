package fr.rhumun.game.worldcraftopengl.worlds.generators.biomes;

import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import lombok.Getter;

@Getter
public abstract class Biomes {

    public static Biome PLAIN = new PlainBiome();
    public static Biome HILL = new HillBiome();
    public static Biome MOUNTAIN = new MountainBiome();
    public static Biome BEACH = new BeachBiome();
    public static Biome MESA = new MesaBiome();
    public static Biome DESERT = new DesertBiome();
    public static Biome OCEAN = new OceanBiome();
}
