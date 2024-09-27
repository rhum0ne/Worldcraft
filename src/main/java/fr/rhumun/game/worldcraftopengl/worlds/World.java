package fr.rhumun.game.worldcraftopengl.worlds;

import fr.rhumun.game.worldcraftopengl.blocks.Material;
import fr.rhumun.game.worldcraftopengl.blocks.Block;
import fr.rhumun.game.worldcraftopengl.worlds.generators.Flat;
import fr.rhumun.game.worldcraftopengl.worlds.generators.WorldGenerator;
import fr.rhumun.game.worldcraftopengl.worlds.structures.Structure;
import javafx.scene.paint.Color;
import lombok.Getter;
import org.joml.Vector3f;

import java.util.HashMap;

@Getter
public class World {

    private final WorldGenerator generator;

    private final HashMap<Point, Chunk> chunks = new HashMap<>();
    private final int heigth = 32;

    private short red = 230, green = 200, blue = 180;

    public World(){
        this.generator = new Flat(this);

        this.createChunk(0, 0);

        this.getBlockAt(-20, 11, -20, true).setMaterial(Material.BRICKS);
        this.getBlockAt(-20, 12, -20, true).setMaterial(Material.BRICKS);
        this.getBlockAt(-20, 13, -20, true).setMaterial(Material.BRICKS);
        this.getBlockAt(-20, 14, -20, true).setMaterial(Material.BRICKS);
    }

    public Chunk createChunk(int x, int z){
        Point coos = new Point(x, z);
        if(chunks.containsKey(coos)){
            System.out.println("ERROR : chunk " + x + " : " + z + " already exists.");
            return chunks.get(coos);
        }
        //System.out.println("Creating a new chunk at " + x + " : " + z);
        Chunk chunk = new Chunk(this, x, z);
        this.chunks.put(coos, chunk);
        this.generator.tryGenerate(chunk);
        //System.out.println("");
        //System.out.println(chunks.toString().replace(" ", "\n"));
        // System.out.println("");
        return chunk;
    }

    public boolean isChunkLoadedAt(int x, int z){
        if(x < 0 && x%16!=0) x-=16;
        if(z < 0 && z%16!=0) z-=16;
        return isChunkLoaded(x/16, z/16);
    }

    public boolean isChunkLoaded(int x, int z){
        if(this.chunks.containsKey(new Point(x, z))){
            //System.out.println("Chunk " + x + " " + z + " is loaded");
            return true;
        }
        //System.out.println("Chunk " + x + " " + z + " not loaded");
        return false;
    }

    public Chunk getChunk(int x, int z, boolean generateIfNull){
        Chunk chunk = chunks.get(new Point(x, z));
        if(generateIfNull && chunk == null) {
            System.out.println("ERROR: Chunk " + x + " : " + z + " is not loaded. Loading it...");
            chunk = createChunk(x, z);
        }
        return chunk;
    }

    public Chunk getChunkAt(int x, int z, boolean generateIfNull){
        if(x < 0 && x%16!=0) x-=16;
        if(z < 0 && z%16!=0) z-=16;
        return getChunk(x/16, z/16, generateIfNull);
    }

    public Chunk getChunkAt(double xD, double zD, boolean generateIfNull){
        int x = (int) Math.round(xD);
        int z = (int) Math.round(zD);
        return this.getChunkAt(x, z, generateIfNull);
    }

    public Block getBlockAt(int x, int y, int z, boolean generateIfNull){
        Chunk chunk = this.getChunkAt(x, z, generateIfNull);
        //System.out.println("Looking for chunk at : " + chunk.getX() + " : " + chunk.getZ());
        if(chunk == null) return null;
        return chunk.get(x%16,y,z%16); // blocks est une structure de données représentant le monde
    }

    public Block getBlockAt(double xD, double yD, double zD, boolean generateIfNull){
        int x = (int) Math.round(xD);
        int y = (int) Math.floor(yD);
        int z = (int) Math.round(zD);
        return getBlockAt(x,y,z, generateIfNull);
    }

    public Block getBlockAt(Vector3f position, boolean generateIfNull) {
        return getBlockAt(position.x, position.y, position.z, generateIfNull);
    }

    public void spawnStructure(Structure structure, int x, int y, int z){
        structure.getStructure().tryBuildAt(this, x, y ,z);
    }
}
