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
    private final HashMap<Short, Chunk> loadedChunks; // Mapping ID -> Chunk

    public ChunksContainer(World world){
        this.world = world;

        this.availableIds = new ArrayDeque<>();
        this.loadedChunks = new HashMap<>();

        // Initialiser tous les IDs possibles (0 à 65535)
        for (short i = Short.MIN_VALUE; i < Short.MAX_VALUE; i++) {
            availableIds.add(i);
        }
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
                GAME.errorLog("No ID Available to create Chunk " + x + " " + z);
                return null;
            }

            short chunkId = availableIds.poll(); // Prend un ID disponible
            GAME.debug("Creating chunk at " + x + " : " + z);

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
        long key = toLongKey(x, z);
         Chunk chunk = chunks.remove(key);

        Chunk chunkByID = loadedChunks.remove(chunk.getRenderID());
        if (chunkByID == null) return; // Chunk non chargé

        // Remet l'ID dans la liste des disponibles
        availableIds.add(chunk.getRenderID());
    }
    public void remove(Chunk chunk){
        remove(chunk.getX(), chunk.getZ());
    }

}
