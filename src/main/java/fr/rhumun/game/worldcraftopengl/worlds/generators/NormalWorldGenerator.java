package fr.rhumun.game.worldcraftopengl.worlds.generators;

import de.articdive.jnoise.core.api.functions.Interpolation;
import de.articdive.jnoise.generators.noise_parameters.fade_functions.FadeFunction;
import de.articdive.jnoise.modules.octavation.fractal_functions.FractalFunction;
import de.articdive.jnoise.pipeline.JNoise;
import fr.rhumun.game.worldcraftopengl.blocks.Block;
import fr.rhumun.game.worldcraftopengl.blocks.Material;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.worlds.World;
import fr.rhumun.game.worldcraftopengl.worlds.structures.Structure;

public class NormalWorldGenerator extends WorldGenerator {

    private JNoise continentalness;
    private long seed;
    private int waterHigh = 28;


    public NormalWorldGenerator(World world) {
        super(world);

        this.seed = 123456789;
        this.continentalness=JNoise.newBuilder().perlin(seed, Interpolation.COSINE, FadeFunction.QUINTIC_POLY).octavate(8,1.0,0.9, FractalFunction.FBM,false).build();
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
                    return;
                }
                block.setMaterial(Material.GRASS);
                for(int i=0; i<3; i++){
                    block = block.getBlockAtDown();
                    block.setMaterial(Material.DIRT);
                }
            }
    }

    private void fillWater(Chunk chunk) {
        //System.out.println("Filling water");
        for(Block block: chunk.getBlockList()){
            if(block.getMaterial() != null) continue;
            if(block.getLocation().getY() <= waterHigh) block.setMaterial(Material.WATER);
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
        if(continentalnessValue < 0.5) return (int) (35*continentalnessValue+37);
        return (int) (8*continentalnessValue+49);
    }

    @Override
    public void populate(Chunk chunk) {
        for(int x=0; x<16; x++){
            for(int z=0; z<16; z++){
                if((1+x+z+chunk.getZ())%5==0 && (chunk.getX()+x+2*z)%7==0)
                    this.getWorld().spawnStructure(Structure.TREE, 16*chunk.getX() + x, (int) (chunk.getHighestBlock(x, z).getLocation().getY()+1), 16*chunk.getZ() + z);

            }
        }
    }
}
