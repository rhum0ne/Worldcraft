package fr.rhumun.game.worldcraftopengl.worlds;

import fr.rhumun.game.worldcraftopengl.content.materials.Material;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static fr.rhumun.game.worldcraftopengl.Game.*;

public class ChunksContainer {

    private final World world;
    private final ConcurrentHashMap<Long, AbstractChunk> chunks = new ConcurrentHashMap<>();
    private final HashMap<Short, AbstractChunk> loadedChunks = new HashMap<>();

    private final Deque<Short> availableIds = new ArrayDeque<>();
    private short maxID = Short.MIN_VALUE;

    public ChunksContainer(World world) {
        this.world = world;
    }

    private long toLongKey(int x, int z) {
        return ((long) x << 32) | (z & 0xFFFFFFFFL);
    }

    public long getChunkKey(int x, int z) {
        return toLongKey(x, z);
    }

    private synchronized Short nextAvailableId() {
        if (availableIds.isEmpty()) {
            if (maxID == Short.MAX_VALUE) {
                GAME.errorLog("No ID Available");
                return null;
            }
            availableIds.add(maxID++);
        }
        return availableIds.poll();
    }

    private synchronized void registerChunk(long key, AbstractChunk chunk) {
        chunks.put(key, chunk);
        loadedChunks.put(chunk.getRenderID(), chunk);
    }

    public synchronized void unregisterChunk(AbstractChunk chunk) {
        long key = toLongKey(chunk.getX(), chunk.getZ());
        AbstractChunk existing = chunks.get(key);

        // Fix NullPointerException here
        if (existing == null || existing != chunk) return;

        loadedChunks.remove(chunk.getRenderID());
        chunks.remove(key);
        availableIds.add(chunk.getRenderID());
    }

    public boolean exists(int x, int z) {
        return chunks.containsKey(toLongKey(x, z));
    }

    public boolean exists(Chunk chunk) {
        return chunks.containsValue(chunk);
    }

    public AbstractChunk getChunkById(short id) {
        return loadedChunks.get(id);
    }

    public AbstractChunk getAbstractChunk(int x, int z) {
        return chunks.get(toLongKey(x, z));
    }

    public void remove(int x, int z) {
        AbstractChunk chunk = chunks.get(toLongKey(x, z));
        if (chunk != null) remove(chunk);
    }

    public void remove(AbstractChunk chunk) {
        GAME.debug("Removing Chunk " + chunk);
        unregisterChunk(chunk);
    }

    public Chunk getChunk(int x, int z, boolean createIfNull, boolean generateIfNull) {
        long key = toLongKey(x, z);
        AbstractChunk chunk = chunks.get(key);
        createIfNull = createIfNull || generateIfNull;

        if (chunk instanceof Chunk c) return c;
        if (generateIfNull && chunk instanceof LightChunk l) return convertLightToFullChunk(l, key, x, z, true);

        return createIfNull ? createChunk(x, z, generateIfNull) : null;
    }

    public LightChunk getLightChunkAt(int x, int z) {
        long key = toLongKey(x, z);
        AbstractChunk chunk = chunks.get(key);

        if (chunk instanceof LightChunk l) return l;
        if (chunk instanceof Chunk c) return convertFullToLightChunk(c, key, x, z);

        return createLightChunkAt(x, z);
    }

    Chunk createChunk(int x, int z, boolean generate) {
        Short id = nextAvailableId();
        if (id == null) return null;

        Chunk chunk = new Chunk(world, id, x, z);
        registerChunk(toLongKey(x, z), chunk);

        if (SaveManager.chunkExists(world, x, z)) {
            SaveManager.loadChunkAsync(chunk, () -> {
                if (!chunk.isGenerated() && generate) {
                    world.getGenerator().addToGenerate(chunk);
                }
            });
        } else if (generate) {
            world.getGenerator().addToGenerate(chunk);
        }

        return chunk;
    }

    private LightChunk createLightChunkAt(int x, int z) {
        Short id = nextAvailableId();
        if (id == null) return null;

        LightChunk chunk = new LightChunk(id, x, z, world);
        registerChunk(toLongKey(x, z), chunk);

        if (SaveManager.chunkExists(world, x, z)) {
            SaveManager.loadLightChunkAsync(chunk, () -> {
                if (!chunk.isGenerated()) {
                    world.getGenerator().addToGenerate(chunk);
                }
            });
        } else {
            world.getGenerator().addToGenerate(chunk);
        }
        chunk.setToUpdate(true);

        return chunk;
    }

    private Chunk convertLightToFullChunk(LightChunk lightChunk, long key, int x, int z, boolean generate) {
        unregisterChunk(lightChunk); // remove safely
        return createChunk(x, z, generate);
    }

    private LightChunk convertFullToLightChunk(Chunk fullChunk, long key, int x, int z) {
        Short id = nextAvailableId();
        if (id == null) return null;

        LightChunk light = new LightChunk(id, x, z, world);
        Material[][][] dest = light.getMaterials();
        Block[][][] src = fullChunk.getBlocks();
        int height = world.getHeigth();
        for (int xi = 0; xi < CHUNK_SIZE; xi++) {
            for (int y = 0; y < height; y++) {
                Material[] destRow = dest[xi][y];
                Block[] srcRow = src[xi][y];
                for (int zi = 0; zi < CHUNK_SIZE; zi++) {
                    destRow[zi] = srcRow[zi].getMaterial();
                }
            }
        }

        light.updateAllBlock();
        light.setToUpdate(true);

        // Save synchronously before discarding data so we don't read half-written files
        fullChunk.unload(true);
        registerChunk(key, light);
        return light;
    }
}
