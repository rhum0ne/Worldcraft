package fr.rhumun.game.worldcraftopengl.worlds.generators;

import de.articdive.jnoise.core.api.functions.Interpolation;
import de.articdive.jnoise.generators.noise_parameters.fade_functions.FadeFunction;
import de.articdive.jnoise.generators.noise_parameters.simplex_variants.Simplex2DVariant;
import de.articdive.jnoise.generators.noise_parameters.simplex_variants.Simplex3DVariant;
import de.articdive.jnoise.generators.noise_parameters.simplex_variants.Simplex4DVariant;
import de.articdive.jnoise.modules.octavation.fractal_functions.FractalFunction;
import de.articdive.jnoise.pipeline.JNoise;
import fr.rhumun.game.worldcraftopengl.content.Block;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.worlds.World;
import fr.rhumun.game.worldcraftopengl.worlds.generators.biomes.Biome;
import fr.rhumun.game.worldcraftopengl.worlds.generators.utils.HeightCalculation;
import fr.rhumun.game.worldcraftopengl.worlds.generators.utils.Seed;
import fr.rhumun.game.worldcraftopengl.worlds.structures.Structure;
import lombok.Getter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
                .superSimplex(seed.getLong(), Simplex2DVariant.CLASSIC, Simplex3DVariant.CLASSIC, Simplex4DVariant.CLASSIC)
                .octavate(6, 0.5, 1.3, FractalFunction.FBM, true)
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
                .perlin(seed.getLong() + 1, Interpolation.LINEAR, FadeFunction.SMOOTHSTEP)
                .octavate(2, 0.4, 2.0, FractalFunction.FBM, false)
                .abs()
                .build();

        // Humidité
        this.humidity = JNoise.newBuilder()
                .perlin(seed.getLong() + 2, Interpolation.LINEAR, FadeFunction.SMOOTHSTEP)
                .octavate(2, 0.4, 2.0, FractalFunction.FBM, false)
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
        
        shapeTerrain(chunk);
        fillWater(chunk);
        paint(chunk);
        createCaves(chunk);
        populate(chunk);

        //chunk.updateBordersChunks();
    }

    private void createCaves(Chunk chunk) {
        for(int x=0; x<CHUNK_SIZE; x++)
            for(int z=0; z<CHUNK_SIZE; z++)
                for(int y=0; y< getWorld().getHeigth(); y++) {
                    Block block = chunk.getBlockNoVerif(x, y ,z);

                    if(block == null || block.getMaterial() == null || block.getMaterial() == Material.DARK_COBBLE) continue;
                    if ((block.getMaterial() != Material.STONE && block.getMaterial() != Material.DIRT && block.getMaterial() != Material.GRASS_BLOCK)) continue;

                    double xH = (chunk.getX()*CHUNK_SIZE+x)/400.0;
                    double zH = (chunk.getZ()*CHUNK_SIZE+z)/400.0;

                    float noise = (float) caves.evaluateNoise(xH, y / 64f, zH);

                    float w = (block.getMaterial() == Material.STONE) ? 0.1f : 0.02f;
                    float t = (block.getMaterial() == Material.STONE) ? 0 : 0.01f;
                    if(y<5) w+= (float) 1 /y+1;

                    if (noise > t && noise < w) block.setMaterial(null);
                }
    }

    private void paint(Chunk chunk) {
        for(int x=0; x<CHUNK_SIZE; x++)
            for(int z=0; z<CHUNK_SIZE; z++){
                Block block = chunk.getHighestBlock(x, z, false);
                if(block == null) {
                    GAME.errorLog("No block found for x="+x+",z="+z);
                    continue;
                }
                if(block.getMaterial() != Material.STONE) continue;
                block.setMaterial(block.getBiome().getTop());
                for(int i=0; i<3; i++){
                    block = block.getBlockAtDown();
                    if(block == null) break;
                    block.setMaterial(block.getBiome().getSecondary());
                }

                chunk.setBlock(x, 0, z, Material.DARK_COBBLE);
            }
    }

    private void fillWater(Chunk chunk) {
        //System.out.println("Filling water");
        for(int x=0; x<CHUNK_SIZE; x++)
            for(int z=0; z<CHUNK_SIZE; z++)
                for(int y=0; y< waterHigh; y++) {
                    Block block = chunk.getBlocks()[x][y][z];
                    if (block.getMaterial() != null) continue;
                    block.setMaterial(Material.WATER);
                }
    }

    private void init() {

    }

    private void shapeTerrain(Chunk chunk) {

        for(int x=0; x<CHUNK_SIZE; x++){
            for(int z=0; z<CHUNK_SIZE; z++){
                int xH = (chunk.getX()*CHUNK_SIZE+x);
                int zH = (chunk.getZ()*CHUNK_SIZE+z);

                double continentalValue = continentalness.evaluateNoise(xH/512f, zH/512f);
                double erosionValue = erosion.evaluateNoise(xH/612f, zH/612f);

                double pavLargeScale = pav.evaluateNoise(xH / 500.0, zH / 500.0); // Relief large
                double pavSmallScale = pav.evaluateNoise(xH / 40.0, zH / 40.0); // Détails fins

                Biome biome = getBiome(xH, zH, continentalValue, erosionValue, pavLargeScale, pavSmallScale);

                int height = heightCalculator.calcHeight(xH, zH, continentalValue, erosionValue, pavLargeScale, pavSmallScale);

                for (int y = 0; y < this.getWorld().getHeigth(); y++) {
                    Block block = chunk.get(x, y, z);
                    if (block == null) continue;

                    if (y < height) {
                        block.setMaterial(Material.STONE);
                    }
                    block.setBiome(biome);
                }

            }
        }

//        try {
//            exportTerrainToImage(terrain, TEXTURES_PATH + "terrain.png", chunk.getX(), chunk.getZ());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    private Biome getBiome(double x, double z, double continentalValue, double erosionValue, double pavLargeScale, double pavSmallScale) {

        double temperatureValue = temperature.evaluateNoise(x / 300.0, z / 300.0);
        double humidityValue = humidity.evaluateNoise(x / 300.0, z / 300.0);


        // Logique de sélection des biomes
        if (continentalValue < 0.1) {
            if (erosionValue < 40) {
                return Biome.BEACH; // Plage avec faible érosion
            } else {
                return Biome.OCEAN; // Océan profond ou lagon
            }
        } else {
            if (temperatureValue > 0.3 && humidityValue < 0) {
                return Biome.DESERT;
            } else if (pavSmallScale > 40) {
                if (temperatureValue > 0.5 && humidityValue > 0.7)
                    return Biome.MESA;
                return Biome.MOUNTAIN;
            } else if (erosionValue > 30) {
                return Biome.PLAIN;
            } else {
                return Biome.HILL;
            }
        }
    }


    private void exportTerrainToImage(double[][] terrain, String filePath, int xC, int zC) throws IOException{
        File file = new File(filePath);

        int width = terrain.length;
        int height = terrain[0].length;

        BufferedImage image;
        if(file.exists())
            image = ImageIO.read(file);
        else image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = xC*16; x < xC*16+16; x++) {
            for (int z = zC*16; z < zC*16+16; z++) {
                if(x<width && x>-1 && z<height && z>0) {
                    int heightValue = (int) terrain[x][z];
                    int color = (heightValue << 16) | (heightValue << 8) | heightValue; // Grisaille
                    image.setRGB(x, z, color);
                }
            }
        }

        ImageIO.write(image, "png", file);
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

        if(block.getMaterial() == Material.GRASS_BLOCK)
            if((1+x*z+chunk.getZ())%5==0 && (chunk.getX()+x+2*z)%7==0)
                chunk.get(x, (int) (block.getLocation().getY() + 1), z).setMaterial(Material.GRASS);
            else if((1+x+z+chunk.getZ())%4==0 && (chunk.getX()+x+2*z)%7==0)
                chunk.get(x, (int) (block.getLocation().getY() + 1), z).setMaterial(Material.BLUE_FLOWER);
            else if((1+x+z+chunk.getZ())%5==0 && (chunk.getX()+x+2*z)%9==0)
                chunk.get(x, (int) (block.getLocation().getY() + 1), z).setMaterial(Material.RED_FLOWER);
            else if((1+z*x+chunk.getX())%5==0 && (chunk.getZ()+x*z)%7==0)
                chunk.getWorld().spawnStructure(Structure.TREE, CHUNK_SIZE * chunk.getX() + x, (int) (block.getLocation().getY() + 1), CHUNK_SIZE * chunk.getZ() + z);
    }
}
