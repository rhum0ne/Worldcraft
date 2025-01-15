package fr.rhumun.game.worldcraftopengl.worlds.generators.biomes;

import fr.rhumun.game.worldcraftopengl.content.Block;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import lombok.Getter;

@Getter
public class Biome {

    public static Biome PLAIN = new Biome("Plain", 0, Material.GRASS_BLOCK, Material.DIRT);
    public static Biome HILL = new Biome("Hill", 0, Material.GRASS_BLOCK, Material.DIRT);
    public static Biome MOUNTAIN = new Biome("Mountain", 1.7f, Material.COBBLE, Material.STONE);
    public static Biome BEACH = new Biome("Beach", 0, Material.SAND, Material.STONE);
    public static Biome MESA = new Biome("Mesa", 0f, Material.ORANGE_WOOL, Material.BROWN_WOOL);
    public static Biome DESERT = new Biome("Desert", 0f, Material.SAND, Material.SAND);
    public static Biome OCEAN = new Biome("Ocean", 0.5f, Material.DARK_STONE, Material.DARK_COBBLE);


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
}
