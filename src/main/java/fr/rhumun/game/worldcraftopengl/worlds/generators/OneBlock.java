package fr.rhumun.game.worldcraftopengl.worlds.generators;

import fr.rhumun.game.worldcraftopengl.content.materials.Material;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.worlds.LightChunk;
import fr.rhumun.game.worldcraftopengl.worlds.World;

public class OneBlock extends WorldGenerator{

    private final Material material;
    public OneBlock(World world, Material material) {
        super(world);
        this.material = material;
    }

    @Override
    public void generate(Chunk chunk) {
        if(chunk.getX()==0 && chunk.getZ()==0){
            chunk.get(0,100,0).setMaterial(material);
        }
    }

    @Override
    public void generate(LightChunk chunk) {

    }

    @Override
    public void populate(Chunk chunk) {

    }
}
