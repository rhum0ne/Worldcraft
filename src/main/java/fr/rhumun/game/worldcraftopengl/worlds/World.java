package fr.rhumun.game.worldcraftopengl.worlds;

import fr.rhumun.game.worldcraftopengl.entities.Location;
import fr.rhumun.game.worldcraftopengl.content.Block;
import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.worlds.generators.NormalWorldGenerator;
import fr.rhumun.game.worldcraftopengl.worlds.generators.WorldGenerator;
import fr.rhumun.game.worldcraftopengl.worlds.generators.utils.Seed;
import fr.rhumun.game.worldcraftopengl.worlds.structures.Structure;
import javafx.scene.paint.Color;
import lombok.Getter;
import org.joml.Vector3f;

import java.util.HashMap;

import static fr.rhumun.game.worldcraftopengl.Game.CHUNK_SIZE;
import static fr.rhumun.game.worldcraftopengl.Game.GAME;

@Getter
public class World {

    private final WorldGenerator generator;

    //private final HashMap<Point, Chunk> chunks = new HashMap<>();
    private final ChunksContainer chunks;
    private final int heigth = 256;

    private Color skyColor = Color.rgb(77, 150, 230);
    private Color lightColor = Color.rgb(180, 170, 170);

    private final Seed seed;
    private Location spawn;
    private Chunk spawnChunk;
    private int xSpawn;
    private int zSpawn;

    private boolean isLoaded = false;

    public World(){
        //this.seed = Seed.create("6038198250");
        this.seed = Seed.random();
        this.chunks = new ChunksContainer(this);
        this.generator = new NormalWorldGenerator(this);
        //this.generator = new Flat(this);

        GAME.log("Creating a new World... \nSeed: " + seed.getLong());

        xSpawn = seed.getCombinaisonOf(1, 8, 3) * seed.get(7);
        zSpawn = seed.getCombinaisonOf(5, 4, 9) * seed.get(1);
    }

    public void load(){
        spawnChunk = this.getChunkAt(xSpawn, zSpawn, true);
        this.generator.tryGenerate(spawnChunk);

        while(isLoading()){
            try {
                Thread.yield();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                GAME.errorLog(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        this.spawn = new Location(this, xSpawn, spawnChunk.getHighestBlock(xSpawn-CHUNK_SIZE*spawnChunk.getX(), zSpawn-CHUNK_SIZE*spawnChunk.getZ(), true).getLocation().getY()+10, zSpawn);
        this.isLoaded = true;
    }

    public boolean isLoading(){
        return !this.spawnChunk.isGenerated();
    }

    public void spawnPlayer(Player player){
        player.setLocation(spawn);
    }
    public Chunk createChunk(int x, int z){ return this.chunks.createChunk(x, z, false); }

    public Chunk getChunk(int x, int z, boolean generateIfNull){ return this.chunks.getChunk(x, z, generateIfNull); }

    public boolean isChunkLoaded(int x, int z){ return chunks.exists(x, z); }
    public void unload(Chunk chunk) { chunks.remove(chunk); }

    public boolean isChunkLoadedAt(int x, int z){
        if(x < 0 && x%CHUNK_SIZE!=0) x-=CHUNK_SIZE;
        if(z < 0 && z%CHUNK_SIZE!=0) z-=CHUNK_SIZE;
        return isChunkLoaded(x/CHUNK_SIZE, z/CHUNK_SIZE);
    }

    public Chunk getChunkAt(int x, int z, boolean generateIfNull){
        if(x < 0 && x%CHUNK_SIZE!=0) x-=CHUNK_SIZE;
        if(z < 0 && z%CHUNK_SIZE!=0) z-=CHUNK_SIZE;
        return getChunk(x/CHUNK_SIZE, z/CHUNK_SIZE, generateIfNull);
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
        int xInput = x%CHUNK_SIZE;
        int zInput = z%CHUNK_SIZE;
        if(xInput<0) xInput+=CHUNK_SIZE;
        if(zInput<0) zInput+=CHUNK_SIZE;
        return chunk.get(xInput,y,zInput); // blocks est une structure de données représentant le monde
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
