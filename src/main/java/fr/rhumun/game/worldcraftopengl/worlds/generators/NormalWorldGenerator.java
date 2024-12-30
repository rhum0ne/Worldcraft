package fr.rhumun.game.worldcraftopengl.worlds.generators;

import de.articdive.jnoise.core.api.functions.Interpolation;
import de.articdive.jnoise.generators.noise_parameters.fade_functions.FadeFunction;
import de.articdive.jnoise.modules.octavation.fractal_functions.FractalFunction;
import de.articdive.jnoise.pipeline.JNoise;
import fr.rhumun.game.worldcraftopengl.blocks.Block;
import fr.rhumun.game.worldcraftopengl.blocks.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.worlds.World;
import fr.rhumun.game.worldcraftopengl.worlds.structures.Structure;
import lombok.Getter;

import static fr.rhumun.game.worldcraftopengl.Game.CHUNK_SIZE;

@Getter
public class NormalWorldGenerator extends WorldGenerator {

    private final JNoise continentalness;
    private final JNoise secondary;
    private final JNoise detail;
    private final long seed;
    private final int waterHigh = 29;


    public NormalWorldGenerator(World world) {
        super(world);

        this.seed = 123456789;

        // Grande échelle - contrôle des continents
        this.continentalness = JNoise.newBuilder()
                .perlin(seed, Interpolation.COSINE, FadeFunction.QUINTIC_POLY)
                .octavate(3, 0.5, 2.2, FractalFunction.FBM, false)
                .build();

        // Variations intermédiaires - collines et plateaux
        this.secondary = JNoise.newBuilder()
                .perlin(seed, Interpolation.COSINE, FadeFunction.QUINTIC_POLY)
                .octavate(4, 0.4, 1.8, FractalFunction.FBM, false)
                .build();

        // Petits détails - relief local
        this.detail = JNoise.newBuilder()
                .perlin(seed, Interpolation.COSINE, FadeFunction.QUINTIC_POLY)
                .octavate(6, 0.6, 1.5, FractalFunction.FBM, false)
                .build();
    }


    @Override
    public void generate(Chunk chunk) {
        init();

        shapeTerrain(chunk);
        fillWater(chunk);
        paint(chunk);

        chunk.updateBordersChunks();
    }

    private void paint(Chunk chunk) {
        for(int x=0; x<CHUNK_SIZE; x++)
            for(int z=0; z<CHUNK_SIZE; z++){
                Block block = chunk.getHighestBlock(x, z);
                if(block == null) {
                    System.out.println("No block found for x="+x+",z="+z);
                    continue;
                }
                if(block.getMaterial() != Material.STONE) continue;
                block.setMaterial(Material.GRASS_BLOCK);
                for(int i=0; i<3; i++){
                    block = block.getBlockAtDown();
                    if(block == null) break;
                    block.setMaterial(Material.DIRT);
                }
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
                double xH = (chunk.getX()*CHUNK_SIZE+x)/100.0;
                double zH = (chunk.getZ()*CHUNK_SIZE+z)/100.0;


                int height = calculateHeight(xH, zH);


                for(int y=0; y<height; y++){
                    Block block = chunk.get(x,y,z);
                    if(block == null) continue;
                    block.setMaterial(Material.STONE);
                }
            }
        }
    }

//    private int calculateHeight(double xH, double zH) {
//        float continentalnessValue = (float) continentalness.evaluateNoise(xH, zH);
//        int h = getWorld().getHeigth();
//
//        float x = continentalnessValue;
//
//        if(continentalnessValue < 0.1) return (int) (40*x+20);
//        x-=0.1;
//        if(continentalnessValue < 0.5) return (int) (20*x+29);
//        x-=0.4;
//        if(continentalnessValue < 0.7) return (int) (25*x+34);
//        x-=0.2;
//        if(continentalnessValue < 0.8) return (int) (45*x+39);
//        x-=0.1;
//        return (int) (15*continentalnessValue+73);
//    }

    private int calculateHeight(double xH, double zH) {
        float continentalnessValue = (float) continentalness.evaluateNoise(xH, zH);

        // Ajouter des variations supplémentaires avec d'autres bruits
        float secondaryNoise = (float) secondary.evaluateNoise(xH * 0.5, zH * 0.5); // Bruit de plus basse fréquence
        float detailNoise = (float) detail.evaluateNoise(xH * 5, zH * 5); // Bruit de plus haute fréquence

        // Ajuster le continentalness avec les variations secondaires
        float adjustedValue = continentalnessValue +0.2f *secondaryNoise;

        // Normaliser les valeurs pour éviter les extrêmes
        adjustedValue = Math.max(0, Math.min(1, adjustedValue));

        int baseHeight;
        if(continentalnessValue < 0.1) baseHeight = (int) (40*adjustedValue+20);
        else if(continentalnessValue < 0.5) baseHeight = (int) (20*(adjustedValue-0.1f)+29);
        else if(continentalnessValue < 0.7) baseHeight = (int) (25*(adjustedValue-0.5f)+34);
        else if(continentalnessValue < 0.8) baseHeight = (int) (45*(adjustedValue-0.7f)+39);
        else baseHeight = (int) (15 * (adjustedValue-0.8f) + 73);


        // Ajouter du détail local pour éviter l'uniformité
        int finalHeight = baseHeight + (int) (5 * detailNoise);

        // Appliquer une hauteur minimale et maximale
        finalHeight = Math.max(20, Math.min(120, finalHeight));

        return finalHeight;
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
        Block block = chunk.getHighestBlock(x, z);

        if(block.getMaterial() == Material.GRASS_BLOCK)
            if((1+x+z+chunk.getZ())%5==0 && (chunk.getX()+x+2*z)%7==0)
                chunk.get(x, (int) (block.getLocation().getY() + 1), z).setMaterial(Material.GRASS);
            else if((1+x+z+chunk.getZ())%4==0 && (chunk.getX()+x+2*z)%7==0)
                chunk.get(x, (int) (block.getLocation().getY() + 1), z).setMaterial(Material.BLUE_FLOWER);
            else if((1+x+z+chunk.getZ())%5==0 && (chunk.getX()+x+2*z)%9==0)
                chunk.get(x, (int) (block.getLocation().getY() + 1), z).setMaterial(Material.RED_FLOWER);
            else if((1+z+chunk.getX())%5==0 && (chunk.getZ()+x)%7==0)
                chunk.getWorld().spawnStructure(Structure.TREE, CHUNK_SIZE * chunk.getX() + x, (int) (block.getLocation().getY() + 1), CHUNK_SIZE * chunk.getZ() + z);
    }
}
