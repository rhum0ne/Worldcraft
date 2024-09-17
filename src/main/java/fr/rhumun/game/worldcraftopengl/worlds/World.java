package fr.rhumun.game.worldcraftopengl.worlds;

import fr.rhumun.game.worldcraftopengl.props.Material;
import fr.rhumun.game.worldcraftopengl.props.Block;
import org.joml.Vector3f;

import java.util.HashMap;

public class World {

    private final HashMap<Point, Chunk> chunks = new HashMap<>();

    public World(){
        this.createChunk(0, 0);

        this.getBlockAt(-20, 11, -20).setMaterial(Material.BRICKS);
        this.getBlockAt(-20, 12, -20).setMaterial(Material.BRICKS);
        this.getBlockAt(-20, 13, -20).setMaterial(Material.BRICKS);
        this.getBlockAt(-20, 14, -20).setMaterial(Material.BRICKS);
    }

    public Chunk createChunk(int x, int z){
        Point coos = new Point(x, z);
        if(chunks.containsKey(coos)){
            System.out.println("ERROR : chunk " + x + " : " + z + " already exists.");
            return chunks.get(coos);
        }
        System.out.println("Creating a new chunk at " + x + " : " + z);
        Chunk chunk = new Chunk(x, z);
        this.chunks.put(coos, chunk);
        //System.out.println("");
        //System.out.println(chunks.toString().replace(" ", "\n"));
        // System.out.println("");
        return chunk;
    }

    public Chunk getChunk(int x, int z){
        Chunk chunk = chunks.get(new Point(x, z));
        if(chunk == null) {
            System.out.println("ERROR: Chunk " + x + " : " + z + " is not loaded. Loading it...");
            chunk = createChunk(x, z);
        }
        return chunk;
    }

    public Chunk getChunkAt(int x, int z){
        if(x < 0) x-=16;
        if(z < 0) z-=16;
        return getChunk(x/16, z/16);
    }

    public Chunk getChunkAt(double xD, double zD){
        int x = (int) Math.floor(xD);
        int z = (int) Math.floor(zD);
        return this.getChunkAt(x, z);
    }

    public Block getBlockAt(int x, int y, int z){
        Chunk chunk = this.getChunkAt(x, z);
        //System.out.println("Looking for chunk at : " + chunk.getX() + " : " + chunk.getZ());
        return chunk.get(x%16,y,z%16); // blocks est une structure de données représentant le monde
    }

    public Block getBlockAt(double xD, double yD, double zD){
        int x = (int) Math.floor(xD);
        int y = (int) Math.ceil(yD);
        int z = (int) Math.floor(zD);
        return getBlockAt(x,y,z);
    }

    public Block getBlockAt(Vector3f position) {
        return getBlockAt(position.x, position.y, position.z);
    }
}
