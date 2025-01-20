package fr.rhumun.game.worldcraftopengl.worlds;

import java.util.concurrent.ConcurrentHashMap;

public class ChunksContainer {
    private final World world;
    private final ConcurrentHashMap<Long, Chunk> chunks = new ConcurrentHashMap<>();

    public ChunksContainer(World world){
        this.world = world;
    }

    private long toLongKey(int x, int z) {
        return ((long) x << 32) | (z & 0xFFFFFFFFL);
    }

    public Chunk createChunk(int x, int z, boolean generate) {
        long key = toLongKey(x, z);

        return chunks.computeIfAbsent(key, k -> {
            System.out.println("Creating chunk at " + x + " : " + z);
            Chunk chunk = new Chunk(world, x, z);
            if(generate)
                world.getGenerator().addToGenerate(chunk);
            return chunk;
        });
    }

    public Chunk getChunk(int x, int z, boolean generateIfNull) {
        long key = toLongKey(x, z);
        Chunk chunk = chunks.get(key);

        if (chunk == null) {
            System.out.println("ERROR: Chunk " + x + " : " + z + " is not loaded. Loading it...");
            chunk = createChunk(x, z, generateIfNull);
        }

        return chunk;
    }

    public boolean exists(Chunk chunk){ return chunks.containsValue(chunk); }

    public boolean exists(int x, int z){
        long key = toLongKey(x, z);
        return chunks.containsKey(key);
    }

    public void remove(int x, int z){
        long key = toLongKey(x, z);
        chunks.remove(key);
    }
    public void remove(Chunk chunk){
        remove(chunk.getX(), chunk.getZ());
    }

}
