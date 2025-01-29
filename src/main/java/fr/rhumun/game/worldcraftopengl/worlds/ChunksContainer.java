package fr.rhumun.game.worldcraftopengl.worlds;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

public class ChunksContainer {
    private final World world;
    private final ConcurrentHashMap<Long, Chunk> chunks = new ConcurrentHashMap<>();

    private final Deque<Short> availableIds; // IDs disponibles
    private short maxID = Short.MIN_VALUE;
    private final HashMap<Short, Chunk> loadedChunks; // Mapping ID -> Chunk

    public ChunksContainer(World world){
        this.world = world;

        this.availableIds = new ArrayDeque<>();
        this.loadedChunks = new HashMap<>();
    }

    public Chunk getChunkById(short chunkId) {
        return loadedChunks.get(chunkId);
    }

    private long toLongKey(int x, int z) {
        return ((long) x << 32) | (z & 0xFFFFFFFFL);
    }

    public Chunk createChunk(int x, int z, boolean generate) {
        long key = toLongKey(x, z);

        return chunks.computeIfAbsent(key, k -> {
            if (availableIds.isEmpty()) {
                if(maxID==Short.MAX_VALUE){
                    GAME.errorLog("No ID Available to create Chunk " + x + " " + z);
                    return null;
                }
                availableIds.add(maxID++);
            }

            short chunkId = availableIds.poll(); // Prend un ID disponible
            GAME.debug("Creating chunk at " + x + " : " + z);
            GAME.debug("Loaded Chunks: " + chunks.size() + " | " + loadedChunks.size());

            Chunk chunk = new Chunk(world, chunkId, x, z);
            loadedChunks.put(chunkId, chunk); // Associe le Chunk à cet ID
            if(generate)
                world.getGenerator().addToGenerate(chunk);
            return chunk;
        });
    }

    public Chunk getChunk(int x, int z, boolean generateIfNull) {
        long key = toLongKey(x, z);
        Chunk chunk = chunks.get(key);

        if (chunk == null) {
            GAME.debug("Chunk " + x + " : " + z + " is not loaded. Loading it...");
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
        GAME.debug("Removing Chunk " + x + " " + z + " from Container...");
        long key = toLongKey(x, z);
        Chunk chunk = chunks.remove(key);
        if(chunk == null){
            GAME.debug("Error during removing chunk " + x + " " + z + " from HashMap 1. NULL");
            return;
        }

        Chunk chunkByID = loadedChunks.remove(chunk.getRenderID());
        if (chunkByID == null){
            GAME.debug("Error during removing chunk " + chunk + " from HashMap 2. NULL");
            return;
        }

        // Remet l'ID dans la liste des disponibles
        availableIds.add(chunk.getRenderID());
    }
    public void remove(Chunk chunk){
        GAME.debug("Removing Chunk " + chunk + " from Container...");
        Chunk chunkByID = loadedChunks.remove(chunk.getRenderID());
        if (chunkByID == null){
            GAME.debug("Error during removing chunk " + chunk + " from HashMap 2. NULL");
            return;
        }

        long key = toLongKey(chunk.getX(), chunk.getZ());
        Chunk chunk2 = chunks.remove(key);
        if(chunk2 == null){
            GAME.debug("Error during removing chunk " + chunk + " from HashMap 1. NULL");
            return;
        }

        // Remet l'ID dans la liste des disponibles
        availableIds.add(chunk.getRenderID());
    }

}
