package fr.rhumun.game.worldcraftopengl.worlds.generators;

import fr.rhumun.game.worldcraftopengl.blocks.Material;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.worlds.World;
import fr.rhumun.game.worldcraftopengl.worlds.structures.Structure;
import lombok.Getter;

@Getter
public abstract class WorldGenerator {

    private final World world;

    public WorldGenerator(World world) {
        this.world = world;
    }


    public void tryGenerate(Chunk chunk){
        if(chunk.isGenerated()) return;
        this.generate(chunk);
        this.populate(chunk);
        chunk.setGenerated(true);
    }

    public abstract void generate(Chunk chunk);
    public abstract void populate(Chunk chunk);

}
