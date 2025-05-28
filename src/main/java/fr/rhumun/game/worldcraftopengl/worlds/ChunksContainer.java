package fr.rhumun.game.worldcraftopengl.worlds;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static fr.rhumun.game.worldcraftopengl.Game.CHUNK_SIZE;
import static fr.rhumun.game.worldcraftopengl.Game.GAME;

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

    private void registerChunk(long key, AbstractChunk chunk) {
        chunks.put(key, chunk);
        loadedChunks.put(chunk.getRenderID(), chunk);
    }

    private void unregisterChunk(AbstractChunk chunk) {
        long key = toLongKey(chunk.getX(), chunk.getZ());
        if(!chunks.get(key).getClass().equals(chunk.getClass())) return;

        loadedChunks.remove(chunk.getRenderID());
        chunks.remove(key);
        availableIds.add(chunk.getRenderID());
    }

    private Short nextAvailableId() {
        if (availableIds.isEmpty()) {
            if (maxID == Short.MAX_VALUE) {
                GAME.errorLog("No ID Available");
                return null;
            }
            availableIds.add(maxID++);
        }
        return availableIds.poll();
    }

    public Chunk getChunk(int x, int z, boolean generateIfNull) {
        long key = toLongKey(x, z);
        AbstractChunk chunk = chunks.get(key);

        if (chunk instanceof Chunk c) return c;
        if (chunk instanceof LightChunk l) return convertLightToFullChunk(l, key, x, z, generateIfNull);

        return createChunk(x, z, generateIfNull);
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
        if (generate) world.getGenerator().addToGenerate(chunk);

        long key = toLongKey(x, z);
        registerChunk(key, chunk);
        return chunk;
    }

    private LightChunk createLightChunkAt(int x, int z) {
        Short id = nextAvailableId();
        if (id == null) return null;

        LightChunk chunk = new LightChunk(id, x, z, world);
        world.getGenerator().addToGenerate(chunk);
        chunk.setToUpdate(true);

        long key = toLongKey(x, z);
        registerChunk(key, chunk);
        return chunk;
    }

    private Chunk convertLightToFullChunk(LightChunk lightChunk, long key, int x, int z, boolean generate) {
        lightChunk.setToUnload(true);
        return createChunk(x, z, generate);
    }

    private LightChunk convertFullToLightChunk(Chunk fullChunk, long key, int x, int z) {
        Short id = nextAvailableId();
        if (id == null) return null;

        LightChunk light = new LightChunk(id, x, z, world);

        for (int xi = 0; xi < CHUNK_SIZE; xi++)
            for (int y = 0; y < world.getHeigth(); y++)
                for (int zi = 0; zi < CHUNK_SIZE; zi++)
                    light.getMaterials()[xi][y][zi] = fullChunk.getBlocks()[xi][y][zi].getMaterial();

        light.setToUpdate(true);

        fullChunk.setToUnload(true);
        registerChunk(key, light);
        return light;
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

    public void remove(int x, int z) {
        AbstractChunk chunk = chunks.get(toLongKey(x, z));
        if (chunk != null) remove(chunk);
    }

    public void remove(AbstractChunk chunk) {
        GAME.debug("Removing Chunk " + chunk);
        unregisterChunk(chunk);
    }

    public AbstractChunk getAbstractChunk(int x, int z) {
        long key = toLongKey(x, z);
        return chunks.get(key);
    }

    public long getChunkKey(int x, int z) {
        return toLongKey(x, z);
    }
}
