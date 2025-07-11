package fr.rhumun.game.worldcraftopengl.worlds;

import fr.rhumun.game.worldcraftopengl.content.items.ItemStack;
import fr.rhumun.game.worldcraftopengl.entities.*;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.worlds.generators.NormalWorldGenerator;
import fr.rhumun.game.worldcraftopengl.worlds.generators.Flat;
import fr.rhumun.game.worldcraftopengl.worlds.generators.WorldGenerator;
import fr.rhumun.game.worldcraftopengl.worlds.WorldType;
import fr.rhumun.game.worldcraftopengl.worlds.generators.utils.Seed;
import fr.rhumun.game.worldcraftopengl.worlds.structures.Structure;
import fr.rhumun.game.worldcraftopengl.worlds.utils.DayNightCycle;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

import static fr.rhumun.game.worldcraftopengl.Game.CHUNK_SIZE;
import static fr.rhumun.game.worldcraftopengl.Game.GAME;

@Getter
public class World {

    /** Duration of a full day/night cycle in ticks. */
    public static final int DAY_NIGHT_TICKS = 24000;

    private final Seed seed;
    private WorldGenerator generator;
    @Setter
    private WorldType worldType = WorldType.NORMAL;

    @Setter
    private String name;

    //private final HashMap<Point, Chunk> chunks = new HashMap<>();
    private final ChunksContainer chunks;
    private final ConcurrentLinkedQueue<Entity> entities = new ConcurrentLinkedQueue<>();

    private Color skyColor = Color.rgb(77, 150, 230);
    private Color lightColor = Color.rgb(180, 170, 170);
    private Vector3f lightDirection = DayNightCycle.getLightDirection(0);

    private final int heigth = 256;

    private Location spawn;
    private Chunk spawnChunk;
    private int xSpawn;
    private int zSpawn;

    private boolean isLoaded = false;

    private int worldTime = 4000;
    /** Whether the day/night cycle is active. */
    private boolean dayNightCycle = true;

    public int getWorldTime() {
        return worldTime;
    }

    public boolean isDayNightCycle() {
        return dayNightCycle;
    }

    public void setDayNightCycle(boolean dayNightCycle) {
        this.dayNightCycle = dayNightCycle;
    }

    public void setWorldTime(int worldTime) {
        this.worldTime = worldTime;
        this.skyColor = DayNightCycle.getSkyColor(worldTime);
        this.lightColor = DayNightCycle.getLightColor(worldTime);
        this.lightDirection = DayNightCycle.getLightDirection(worldTime);
    }

    public float getCelestialAngle() {
        return DayNightCycle.getAngle(worldTime);
    }

    public void updateTime() {
        if (dayNightCycle) {
            setWorldTime(worldTime + 1);
        }
    }

    public void setSpawnPosition(int x, int z) {
        this.xSpawn = x;
        this.zSpawn = z;
    }

    public World(){
        this(Seed.random(), null, WorldType.NORMAL);
    }

    public World(Seed seed){
        this(seed, null, WorldType.NORMAL);
    }

    public World(Seed seed, String name){
        this(seed, name, WorldType.NORMAL);
    }

    public World(Seed seed, String name, WorldType type){
        this.seed = seed;
        this.name = name;
        this.worldType = type;
        this.chunks = new ChunksContainer(this);
        setGeneratorFromType();

        GAME.log("Creating a new World... \nSeed: " + seed.getLong());

        xSpawn = seed.getCombinaisonOf(1, 8, 3) * seed.get(7);
        zSpawn = seed.getCombinaisonOf(5, 4, 9) * seed.get(1);
    }

    private void setGeneratorFromType() {
        if (worldType == WorldType.FLAT) this.generator = new Flat(this);
        else this.generator = new NormalWorldGenerator(this);
    }

    public void load(){
        if(SaveManager.worldExists(this.seed)) {
            SaveManager.loadWorldMeta(this);
            setGeneratorFromType();
        } else {
            SaveManager.saveWorldMeta(this);
        }

        spawnChunk = this.getChunkAt(xSpawn, zSpawn, true);
        if(!spawnChunk.isGenerated())
            this.generator.forceGenerate(spawnChunk);

        while(isLoading()){
            try {
                Thread.yield();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                GAME.errorLog(e);
                throw new RuntimeException(e);
            }
        }

        this.spawn = new Location(this, xSpawn, spawnChunk.getHighestBlock(xSpawn-CHUNK_SIZE*spawnChunk.getX(), zSpawn-CHUNK_SIZE*spawnChunk.getZ(), true).getLocation().getY()+10, zSpawn);
        SaveManager.loadEntities(this);
        this.isLoaded = true;
    }

    public void addEntity(Entity entity){
        this.entities.add(entity);
    }

    public void removeEntity(Entity entity){
        this.entities.remove(entity);
    }

    public boolean isLoading(){
        return !this.spawnChunk.isGenerated();
    }

    public void spawnPlayer(Player player){
        player.setLocation(spawn);
    }
    public Chunk createChunk(int x, int z){ return this.chunks.createChunk(x, z, false); }

    public Chunk getChunk(int x, int z, boolean createIfNull, boolean generateIfNull){ return this.chunks.getChunk(x, z, createIfNull, generateIfNull); }

    public Chunk getChunk(int x, int z, boolean generateIfNull){ return this.chunks.getChunk(x, z, false, generateIfNull); }

    public boolean isChunkLoaded(int x, int z){ return chunks.exists(x, z); }
    public void unload(Chunk chunk) { chunks.remove(chunk); }

    public boolean isChunkLoadedAt(int x, int z){
        if(x < 0 && x%CHUNK_SIZE!=0) x-=CHUNK_SIZE;
        if(z < 0 && z%CHUNK_SIZE!=0) z-=CHUNK_SIZE;
        return isChunkLoaded(x/CHUNK_SIZE, z/CHUNK_SIZE);
    }

    public Chunk getChunkAt(int x, int z, boolean createIfNull, boolean generateIfNull){
        if(x < 0 && x%CHUNK_SIZE!=0) x-=CHUNK_SIZE;
        if(z < 0 && z%CHUNK_SIZE!=0) z-=CHUNK_SIZE;
        return getChunk(x/CHUNK_SIZE, z/CHUNK_SIZE, generateIfNull);
    }

    public Chunk getChunkAt(int x, int z, boolean generateIfNull){ return getChunkAt(x, z, false, generateIfNull); }

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

    //Update entities around the given entity with the given radius
    public void updateEntities(Entity entity, int radius) {
        for(Entity entities : this.entities){
            if(entities.equals(entity)) continue;

            entities.update();
        }
    }

    public LightChunk getLightChunk(int x, int z, boolean b) {
        return this.chunks.getLightChunkAt(x, z);
    }


    public void save() {
        for(Chunk chunk : GAME.getGraphicModule().getLoadedChunks())
            SaveManager.saveChunk(chunk);
        SaveManager.savePlayer(this, GAME.getPlayer());
        SaveManager.saveEntities(this);
        SaveManager.saveWorldMeta(this);
    }

    public void spawnItem(ItemStack selectedItem, Location location) {
        EntityFactory.createItemEntity(location, selectedItem);
    }

}
