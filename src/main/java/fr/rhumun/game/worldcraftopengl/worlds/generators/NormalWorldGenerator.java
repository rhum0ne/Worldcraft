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

@Getter
public class NormalWorldGenerator extends WorldGenerator {

    private final JNoise continentalness;
    private final long seed;
    private int waterHigh = 29;


    public NormalWorldGenerator(World world) {
        super(world);

        this.seed = 123456789;
        this.continentalness=JNoise.newBuilder().perlin(seed, Interpolation.COSINE, FadeFunction.QUINTIC_POLY).octavate(8,0.5,2.0, FractalFunction.FBM,false).build();
    }

    @Override
    public void generate(Chunk chunk) {
        init();

        shapeTerrain(chunk);
        fillWater(chunk);
        paint(chunk);
    }

    private void paint(Chunk chunk) {
        for(int x=0; x<16; x++)
            for(int z=0; z<16; z++){
                Block block = chunk.getHighestBlock(x, z);
                if(block == null) {
                    System.out.println("No block found for x="+x+",z="+z);
                    continue;
                }
                if(block.getMaterial() != Material.STONE) continue;
                block.setMaterial(Material.GRASS_BLOCK);
                for(int i=0; i<3; i++){
                    block = block.getBlockAtDown();
                    block.setMaterial(Material.DIRT);
                }
            }
    }

    private void fillWater(Chunk chunk) {
        //System.out.println("Filling water");
        for(int x=0; x<16; x++)
            for(int z=0; z<16; z++)
                for(int y=0; y< waterHigh; y++) {
                    Block block = chunk.getBlocks()[x][y][z];
                    if (block.getMaterial() != null) continue;
                    block.setMaterial(Material.WATER);
                }
    }

    private void init() {

    }

    private void shapeTerrain(Chunk chunk) {
        for(int x=0; x<16; x++){
            for(int z=0; z<16; z++){
                double xH = (chunk.getX()*16+x)/100.0;
                double zH = (chunk.getZ()*16+z)/100.0;


                int height = calculateHeight(xH, zH);


                for(int y=0; y<height; y++){
                    Block block = chunk.get(x,y,z);
                    if(block == null) continue;
                    block.setMaterial(Material.STONE);
                }
            }
        }
    }

    private int calculateHeight(double xH, double zH) {
        float continentalnessValue = (float) continentalness.evaluateNoise(xH, zH);

        if(continentalnessValue < 0.3) return (int) (25*continentalnessValue+32);
        if(continentalnessValue < 0.5) return (int) (35*continentalnessValue+32);
        return (int) (15*continentalnessValue+32);
    }

    @Override
    public void populate(Chunk chunk) {
        for(int x=0; x<16; x++){
            for(int z=0; z<16; z++){
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
                chunk.getWorld().spawnStructure(Structure.TREE, 16 * chunk.getX() + x, (int) (block.getLocation().getY() + 1), 16 * chunk.getZ() + z);
    }
}
