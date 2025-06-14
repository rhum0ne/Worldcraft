package fr.rhumun.game.worldcraftopengl.worlds.generators;

import de.articdive.jnoise.core.api.functions.Interpolation;
import de.articdive.jnoise.generators.noise_parameters.fade_functions.FadeFunction;
import de.articdive.jnoise.generators.noise_parameters.simplex_variants.Simplex2DVariant;
import de.articdive.jnoise.generators.noise_parameters.simplex_variants.Simplex3DVariant;
import de.articdive.jnoise.generators.noise_parameters.simplex_variants.Simplex4DVariant;
import de.articdive.jnoise.modules.octavation.fractal_functions.FractalFunction;
import de.articdive.jnoise.pipeline.JNoise;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.worlds.LightChunk;
import fr.rhumun.game.worldcraftopengl.worlds.World;
import fr.rhumun.game.worldcraftopengl.worlds.generators.biomes.Biome;
import fr.rhumun.game.worldcraftopengl.worlds.generators.biomes.Biomes;
import fr.rhumun.game.worldcraftopengl.worlds.generators.utils.HeightCalculation;
import fr.rhumun.game.worldcraftopengl.worlds.generators.utils.Seed;
import lombok.Getter;

import static fr.rhumun.game.worldcraftopengl.Game.*;

@Getter
public class NormalWorldGenerator extends WorldGenerator {

    private final JNoise continentalness;
    private final JNoise erosion;
    private final JNoise pav;

    private final JNoise temperature;
    private final JNoise humidity;

    private final JNoise caves;

    private HeightCalculation heightCalculator;

    private final Seed seed;
    private final int waterHigh = 70;


    public NormalWorldGenerator(World world) {
        super(world);
        this.seed = world.getSeed();

               // .perlin(seed, Interpolation.LINEAR, FadeFunction.SMOOTHSTEP)
        // Grande échelle - contrôle des continents
        continentalness = JNoise.newBuilder()
                .superSimplex(seed.getLong(), Simplex2DVariant.CLASSIC, Simplex3DVariant.IMPROVE_XZ, Simplex4DVariant.CLASSIC)
                .octavate(8, 0.5, 1.3, FractalFunction.FBM, true)
                .build();

        // Grande échelle - contrôle des continents
        erosion = JNoise.newBuilder()
                .superSimplex(seed.getLong(), Simplex2DVariant.CLASSIC, Simplex3DVariant.CLASSIC, Simplex4DVariant.CLASSIC)
                //.perlin(seed, Interpolation.COSINE, FadeFunction.SQUARTIC_POLY)
                .octavate(5, 0.3, 1.3, FractalFunction.TURBULENCE, true)
                .build();

        // Grande échelle - contrôle des continents
        pav = JNoise.newBuilder()
                .superSimplex(seed.getLong(), Simplex2DVariant.CLASSIC, Simplex3DVariant.CLASSIC, Simplex4DVariant.CLASSIC)
                .octavate(5, 0.7, 2.2, FractalFunction.FBM, true)
                .build();

        // Température
        this.temperature = JNoise.newBuilder()
                .perlin(seed.getLong() + 1, Interpolation.LINEAR, FadeFunction.CUBIC_RATIONAL)
                .octavate(2, 0.4, 1.6, FractalFunction.FBM, false)
                .abs()
                .build();

        // Humidité
        this.humidity = JNoise.newBuilder()
                .perlin(seed.getLong() + 2, Interpolation.LINEAR, FadeFunction.CUBIC_RATIONAL)
                .octavate(2, 0.4, 1.6, FractalFunction.FBM, false)
                .abs()
                .build();


        // Petits détails - relief local
        this.caves = JNoise.newBuilder()
                .superSimplex(seed.getLong(), Simplex2DVariant.CLASSIC, Simplex3DVariant.CLASSIC, Simplex4DVariant.CLASSIC)
                .octavate(3, 0.6, 1.5, FractalFunction.FBM, false)
                .build();


        this.heightCalculator = new HeightCalculation(this, continentalness, erosion, pav);
    }


    @Override
    public void generate(Chunk chunk) {

        init();

        int[][] heightMap = shapeTerrain(chunk);
        fillWater(chunk, heightMap);
        paint(chunk, heightMap);
        createCaves(chunk, heightMap);
        populate(chunk);

        chunk.updateAllBordersChunks();
    }

    private void createCaves(Chunk chunk, int[][] heights) {
        int worldHeight = getWorld().getHeigth();
        for (int x = 0; x < CHUNK_SIZE; x++) {
            double xH = (chunk.getX() * CHUNK_SIZE + x) / 400.0;
            for (int z = 0; z < CHUNK_SIZE; z++) {
                double zH = (chunk.getZ() * CHUNK_SIZE + z) / 400.0;
                int columnHeight = Math.min(heights[x][z], worldHeight);
                for (int y = 0; y < columnHeight; y++) {
                    Block block = chunk.getBlockNoVerif(x, y, z);

                    if (block == null || block.getMaterial() == null || block.getMaterial() == Material.DARK_COBBLE)
                        continue;
                    if ((block.getMaterial() != Material.STONE && block.getMaterial() != Material.DIRT && block.getMaterial() != Material.GRASS_BLOCK))
                        continue;


                    float noise = (float) caves.evaluateNoise(xH, y / 64f, zH);

//                    float w = (block.getMaterial() == Material.STONE) ? 0.1f : 0.02f;
//                    float t = (block.getMaterial() == Material.STONE) ? 0 : 0.01f;
                    float w = 0.15f + y/70f;
                    float t = -0.2f + y/70f;
                    if (y < 5) w += (float) 1 / y + 1;

                    if (noise > t && noise < w) block.setMaterial(null);
                }
            }
        }
    }

    private void paint(Chunk chunk, int[][] heights) {
        int worldHeight = getWorld().getHeigth();
        for (int x = 0; x < CHUNK_SIZE; x++)
            for (int z = 0; z < CHUNK_SIZE; z++) {
                int top = Math.max(0, Math.min(heights[x][z] - 1, worldHeight - 1));
                Block block = chunk.getBlockNoVerif(x, top, z);
                if (block == null) {
                    GAME.errorLog("No block found for x=" + x + ",z=" + z);
                    continue;
                }
                if (block.getMaterial() != Material.STONE) continue;
                Biome biome = block.getBiome();
                if(biome == null) block.setMaterial(Material.BLUE_TERRACOTTA);
                else block.setMaterial(biome.getTop());
                for(int i=0; i<3; i++){
                    block = block.getBlockAtDown();
                    if(block == null) break;
                    block.setMaterial(block.getBiome().getSecondary());
                }

                chunk.setBlock(x, 0, z, Material.DARK_COBBLE);
            }
    }

    private void fillWater(Chunk chunk, int[][] heights) {
        for (int x = 0; x < CHUNK_SIZE; x++)
            for (int z = 0; z < CHUNK_SIZE; z++)
                for (int y = heights[x][z]; y < waterHigh; y++) {
                    Block block = chunk.getBlockNoVerif(x, y, z);
                    if (block.getMaterial() != null) continue;
                    block.setMaterial(Material.WATER);
                }
    }

    private void init() {

    }

    private int[][] shapeTerrain(Chunk chunk) {
        int worldHeight = this.getWorld().getHeigth();
        int[][] heights = new int[CHUNK_SIZE][CHUNK_SIZE];

        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                int xH = (chunk.getX() * CHUNK_SIZE + x);
                int zH = (chunk.getZ() * CHUNK_SIZE + z);

                double continentalValue = continentalness.evaluateNoise(xH / 512f, zH / 512f);
                double erosionValue = erosion.evaluateNoise(xH / 612f, zH / 612f);

                double pavLargeScale = pav.evaluateNoise(xH / 500.0, zH / 500.0); // Relief large
                double pavSmallScale = pav.evaluateNoise(xH / 40.0, zH / 40.0); // Détails fins

                int height = heightCalculator.calcHeight(xH, zH, continentalValue, erosionValue, pavLargeScale, pavSmallScale);
                heights[x][z] = height;
                Biome biome = getBiome(height, xH, zH, continentalValue, erosionValue, pavLargeScale, pavSmallScale);

                chunk.setBiome(chunk.getBlockNoVerif(x, 0, z), biome);
                for (int y = 0; y < height && y < worldHeight; y++) {
                    Block block = chunk.getBlockNoVerif(x, y, z);
                    if (block != null) {
                        block.setMaterial(Material.STONE);
                    }
                }
            }
        }

        return heights;
    }

    private Biome getBiome(int height, double x, double z, double continentalValue, double erosionValue, double pavLargeScale, double pavSmallScale) {

        double temperatureValue = temperature.evaluateNoise(x / 300.0, z / 300.0);
        double humidityValue = humidity.evaluateNoise(x / 300.0, z / 300.0);


        // Logique de sélection des biomes
        if (height<waterHigh-3) return Biomes.OCEAN;
        else if (height<waterHigh && erosionValue > 0) return Biomes.BEACH;

        if (temperatureValue > 0.6 && humidityValue < 0.4)  return Biomes.DESERT;

        else if (pavSmallScale > 0.8 || pavLargeScale > 0.6)
            if (temperatureValue > 0.5 && humidityValue > 0.7) return Biomes.MESA;
            else return Biomes.MOUNTAIN;

        else if (erosionValue > 0.5)
            if(humidityValue < 0.3) return Biomes.HILL;
            else return Biomes.BIRCH_FOREST;


        if(humidityValue < 0.3) return Biomes.PLAIN;
        return Biomes.FOREST;
    }

    @Override
    public void populate(Chunk chunk) {
        for(int x=0; x<CHUNK_SIZE; x++){
            for(int z=0; z<CHUNK_SIZE; z++){
                    spawnVegetation(chunk, x,z);
            }
        }
    }

    private void spawnVegetation(Chunk chunk, int x, int z) {
        Block block = chunk.getHighestBlock(x, z, true);
        Biome biome = chunk.getBiome(block);

        if(block.getMaterial() == Material.GRASS_BLOCK){
            biome.spawnVegetation(chunk, block, x, z, this.getWorld().getSeed());

        }

    }

    @Override
    public void generate(LightChunk chunk) {
        for (int x = 0; x < CHUNK_SIZE; x++) {
            int worldX = chunk.getX() * CHUNK_SIZE + x;
            for (int z = 0; z < CHUNK_SIZE; z++) {
                int worldZ = chunk.getZ() * CHUNK_SIZE + z;

                double continentalValue = continentalness.evaluateNoise(worldX / 512f, worldZ / 512f);
                double erosionValue = erosion.evaluateNoise(worldX / 612f, worldZ / 612f);
                double pavLargeScale = pav.evaluateNoise(worldX / 500.0, worldZ / 500.0);
                double pavSmallScale = pav.evaluateNoise(worldX / 40.0, worldZ / 40.0);

                int height = heightCalculator.calcHeight(worldX, worldZ, continentalValue, erosionValue, pavLargeScale, pavSmallScale);

                for (int y = 0; y < Math.min(height, this.getWorld().getHeigth()); y++) {
                    chunk.getMaterials()[x][y][z] = Material.STONE;
                }

                if (height < waterHigh) {
                    for (int y = height; y < waterHigh; y++) {
                        chunk.getMaterials()[x][y][z] = Material.WATER;
                    }
                }

                // Petite coloration simple
                if (height > waterHigh && height < this.getWorld().getHeigth()) {
                    chunk.getMaterials()[x][height][z] = Material.GRASS_BLOCK;
                }
            }
        }

        chunk.setGenerated(true);
    }

}
