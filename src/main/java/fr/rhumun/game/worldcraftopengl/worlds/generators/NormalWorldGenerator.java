package fr.rhumun.game.worldcraftopengl.worlds.generators;

import de.articdive.jnoise.core.api.functions.Interpolation;
import de.articdive.jnoise.generators.noise_parameters.fade_functions.FadeFunction;
import de.articdive.jnoise.generators.noise_parameters.simplex_variants.Simplex2DVariant;
import de.articdive.jnoise.generators.noise_parameters.simplex_variants.Simplex3DVariant;
import de.articdive.jnoise.generators.noise_parameters.simplex_variants.Simplex4DVariant;
import de.articdive.jnoise.modules.octavation.fractal_functions.FractalFunction;
import de.articdive.jnoise.pipeline.JNoise;
import fr.rhumun.game.worldcraftopengl.content.materials.Materials;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.worlds.LightChunk;
import fr.rhumun.game.worldcraftopengl.worlds.World;
import fr.rhumun.game.worldcraftopengl.worlds.generators.biomes.Biome;
import fr.rhumun.game.worldcraftopengl.worlds.generators.biomes.Biomes;
import fr.rhumun.game.worldcraftopengl.worlds.generators.biomes.PlainBiome;
import fr.rhumun.game.worldcraftopengl.worlds.generators.biomes.HillBiome;
import fr.rhumun.game.worldcraftopengl.worlds.generators.biomes.ForestBiome;
import fr.rhumun.game.worldcraftopengl.worlds.generators.biomes.BirchForestBiome;
import fr.rhumun.game.worldcraftopengl.worlds.generators.utils.HeightCalculation;
import fr.rhumun.game.worldcraftopengl.worlds.generators.utils.Seed;
import fr.rhumun.game.worldcraftopengl.content.materials.Material;
import lombok.Getter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static fr.rhumun.game.worldcraftopengl.Game.*;
import static fr.rhumun.game.worldcraftopengl.content.materials.Materials.*;

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

                    if (block == null || block.getMaterial() == null || block.getMaterial() == Materials.DARK_COBBLE)
                        continue;
                    if ((block.getMaterial() != STONE && block.getMaterial() != DIRT && block.getMaterial() != Materials.GRASS_BLOCK))
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
                if (block.getMaterial() != STONE) continue;
                Biome biome = block.getBiome();
                if(biome == null) block.setMaterial(Materials.BLUE_TERRACOTTA);
                else block.setMaterial(biome.getTop());
                for(int i=0; i<3; i++){
                    block = block.getBlockAtDown();
                    if(block == null) break;
                    block.setMaterial(block.getBiome().getSecondary());
                }

                chunk.setBlock(x, 0, z, Materials.DARK_COBBLE);
            }
    }

    private void fillWater(Chunk chunk, int[][] heights) {
        for (int x = 0; x < CHUNK_SIZE; x++)
            for (int z = 0; z < CHUNK_SIZE; z++)
                for (int y = heights[x][z]; y < getWaterHigh(); y++) {
                    Block block = chunk.getBlockNoVerif(x, y, z);
                    if (block.getMaterial() != null) continue;
                    block.setMaterial(Materials.WATER);
                    block.setState(8);
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
                        block.setMaterial(STONE);
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
        if (height<getWaterHigh()-3) return Biomes.OCEAN;
        else if (height<getWaterHigh() && erosionValue > 0) return Biomes.BEACH;

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

        if(block.getMaterial() == Materials.GRASS_BLOCK){
            biome.spawnVegetation(chunk, block, x, z, this.getWorld().getSeed());

        }

    }

    private void spawnVegetation(LightChunk chunk, Biome biome, int x, int z, int groundY) {
        Seed seed = getWorld().getSeed();
        int chunkX = chunk.getX();
        int chunkZ = chunk.getZ();
        int y = groundY + 1;

        if (biome instanceof fr.rhumun.game.worldcraftopengl.worlds.generators.biomes.PlainBiome ||
            biome instanceof fr.rhumun.game.worldcraftopengl.worlds.generators.biomes.HillBiome) {
            if ((1 + x * z + chunkZ + seed.get(5)) % (seed.get(7) + 3) == 0 &&
                    (chunkX + x + seed.getCombinaisonOf(1, 7, 2) * z + seed.get(6)) % 11 == 0) {
                chunk.setMaterial(x, y, z, Materials.GRASS);
            } else if ((1 + x + z + chunkZ + seed.get(3)) % (seed.get((x * x + z) % 9) + 4) == 0 &&
                    (chunkX * seed.get(2) + x + 3 * z) % (biome instanceof fr.rhumun.game.worldcraftopengl.worlds.generators.biomes.PlainBiome ? 17 : 13) == 0) {
                chunk.setMaterial(x, y, z, Materials.BLUE_FLOWER);
            } else if (biome instanceof fr.rhumun.game.worldcraftopengl.worlds.generators.biomes.HillBiome &&
                    (1 + x + z + chunkZ + seed.get(8)) % 7 == 0 &&
                    (chunkX + x + 2 * z + seed.get(2) * seed.getCombinaisonOf(4, 3, 1)) % (seed.get((z + x * seed.get(1)) % 9) + 3) == 0) {
                chunk.setMaterial(x, y, z, Materials.RED_FLOWER);
            }
        } else if (biome instanceof fr.rhumun.game.worldcraftopengl.worlds.generators.biomes.ForestBiome ||
                   biome instanceof fr.rhumun.game.worldcraftopengl.worlds.generators.biomes.BirchForestBiome) {
            if ((1 + x * z + chunkZ + seed.get(5)) % (seed.get(7) + 3) == 0 &&
                    (chunkX + x + seed.getCombinaisonOf(1, 7, 2) * z + seed.get(6)) % 11 == 0) {
                chunk.setMaterial(x, y, z, Materials.GRASS);
            } else if ((1 + x + z + chunkZ + seed.get(3)) % (seed.get((x * x + z) % 9) + 4) == 0 &&
                    (chunkX * seed.get(2) + x + 3 * z) % 13 == 0) {
                chunk.setMaterial(x, y, z, Materials.BLUE_FLOWER);
            } else if ((1 + x + z + chunkZ + seed.get(8)) % 7 == 0 &&
                    (chunkX + x + 2 * z + seed.get(2) * seed.getCombinaisonOf(4, 3, 1)) % (seed.get((z + x * seed.get(1)) % 9) + 3) == 0) {
                chunk.setMaterial(x, y, z, Materials.RED_FLOWER);
            } else if ((seed.get((x * chunkX + (z * chunkZ)) % 9) + x * z + seed.get(3)) % 11 == 0) {
                if ((x * x + z * z + chunkX * chunkZ + seed.get(1)) % 23 == 0 ||
                        (seed.getCombinaisonOf(x % 5, z % 3, chunkX % 7) + chunkX + z) % 19 == 0) {
                    if (biome instanceof fr.rhumun.game.worldcraftopengl.worlds.generators.biomes.BirchForestBiome) {
                        buildTree(chunk, x, y, z, Materials.BIRCH_LOG, Materials.BIRCH_LEAVES);
                    } else {
                        buildTree(chunk, x, y, z, Materials.LOG, Materials.LEAVES);
                    }
                }
            }
        }
    }

    private void buildTree(LightChunk chunk, int x, int y, int z, Material log, Material leaves) {
        int trunkHeight = 5;
        int radius = 2;
        int worldHeight = getWorld().getHeigth();

        for (int dy = 0; dy < trunkHeight && y + dy < worldHeight; dy++) {
            chunk.setMaterial(x, y + dy, z, log);
        }

        int leavesStartY = y + trunkHeight - 2;
        int leavesEndY = y + trunkHeight;

        for (int ly = leavesStartY; ly < leavesEndY && ly < worldHeight; ly++) {
            for (int lx = -radius; lx <= radius; lx++) {
                for (int lz = -radius; lz <= radius; lz++) {
                    if (ly < y + trunkHeight && lx == 0 && lz == 0) continue;
                    int ax = x + lx;
                    int az = z + lz;
                    if (ax < 0 || ax >= CHUNK_SIZE || az < 0 || az >= CHUNK_SIZE) continue;
                    if (Math.abs(lx) < 2 && Math.abs(lz) < 2 && ly + 2 < worldHeight) {
                        chunk.setMaterial(ax, ly + 2, az, leaves);
                    }
                    chunk.setMaterial(ax, ly, az, leaves);
                }
            }
        }
    }

    @Override
    public void generate(LightChunk chunk) {
        int worldHeight = getWorld().getHeigth();
        for (int x = 0; x < CHUNK_SIZE; x++) {
            int worldX = chunk.getX() * CHUNK_SIZE + x;
            for (int z = 0; z < CHUNK_SIZE; z++) {
                int worldZ = chunk.getZ() * CHUNK_SIZE + z;

                double continentalValue = continentalness.evaluateNoise(worldX / 512f, worldZ / 512f);
                double erosionValue = erosion.evaluateNoise(worldX / 612f, worldZ / 612f);
                double pavLargeScale = pav.evaluateNoise(worldX / 500.0, worldZ / 500.0);
                double pavSmallScale = pav.evaluateNoise(worldX / 40.0, worldZ / 40.0);

                int height = heightCalculator.calcHeight(worldX, worldZ, continentalValue, erosionValue, pavLargeScale, pavSmallScale);
                Biome biome = getBiome(height, worldX, worldZ, continentalValue, erosionValue, pavLargeScale, pavSmallScale);

                for (int y = 0; y < Math.min(height, worldHeight); y++) {
                    chunk.getMaterials()[x][y][z] = STONE;
                }

                if (height <= getWaterHigh()) {
                    for (int y = height; y < Math.min(getWaterHigh(), worldHeight); y++) {
                        chunk.getMaterials()[x][y][z] = Materials.WATER;
                    }
                }

                if (height > 0) {
                    int top = Math.min(height - 1, worldHeight - 1);
                    chunk.getMaterials()[x][top][z] = biome.getTop();
                    for (int i = 1; i <= 3 && top - i >= 0; i++) {
                        chunk.getMaterials()[x][top - i][z] = biome.getSecondary();
                    }
                    chunk.getMaterials()[x][0][z] = Materials.DARK_COBBLE;

                    if (height > getWaterHigh()) {
                        spawnVegetation(chunk, biome, x, z, top);
                    }
                }
            }
        }

        chunk.setGenerated(true);
    }

    public BufferedImage generateContinentalnessMap(int size, int centerX, int centerZ) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_BYTE_GRAY);
        for (int x = 0; x < size; x++) {
            for (int z = 0; z < size; z++) {
                double worldX = centerX - size / 2 + x;
                double worldZ = centerZ - size / 2 + z;

                double value = continentalness.evaluateNoise(worldX / 512.0, worldZ / 512.0);
                int brightness = (int) ((value + 1) / 2 * 255); // Normalisation [-1,1] -> [0,255]
                brightness = Math.max(0, Math.min(255, brightness));
                int rgb = (brightness << 16) | (brightness << 8) | brightness;
                image.setRGB(x, z, rgb);
            }
        }
        return image;
    }


}
