package fr.rhumun.game.worldcraftopengl.worlds;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.items.ItemStack;
import fr.rhumun.game.worldcraftopengl.content.materials.Materials;
import fr.rhumun.game.worldcraftopengl.entities.Entity;
import fr.rhumun.game.worldcraftopengl.entities.ItemEntity;
import fr.rhumun.game.worldcraftopengl.entities.Location;
import fr.rhumun.game.worldcraftopengl.entities.player.Gamemode;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.worlds.generators.biomes.Biome;
import fr.rhumun.game.worldcraftopengl.worlds.generators.biomes.Biomes;
import fr.rhumun.game.worldcraftopengl.worlds.generators.utils.Seed;
import fr.rhumun.game.worldcraftopengl.worlds.WorldType;
import fr.rhumun.game.worldcraftopengl.worlds.LightChunk;

import java.util.concurrent.ConcurrentLinkedDeque;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Utility class handling saving/loading of world and chunk data.
 */
public class SaveManager {

    /** Directory where all worlds are stored */
    public static final Path WORLDS_DIR;

    private static final Map<String, Biome> BIOME_BY_NAME = new HashMap<>();
    private static final Biome[] BIOME_BY_ID;
    private static final Map<Biome, Byte> BIOME_TO_ID = new HashMap<>();
    private static final Set<Path> SAVING_FILES = ConcurrentHashMap.newKeySet();
    private static final ExecutorService IO_EXECUTOR = Executors.newFixedThreadPool(
            Math.max(3, Runtime.getRuntime().availableProcessors() / 2), r -> {
            Thread t = new Thread(r, "SaveManager-IO");
            t.setPriority(10);
            return t;
    });

    /** Queue for deferred light chunk loads to avoid loading them all at once */
    private static final java.util.concurrent.ConcurrentLinkedDeque<QueuedLightLoad> LIGHT_LOAD_QUEUE = new java.util.concurrent.ConcurrentLinkedDeque<>();
    private static final int MAX_LIGHT_LOADS_PER_TICK = 2;

    private record QueuedLightLoad(LightChunk chunk, Runnable onComplete) {}


    static {
        String appdata = System.getenv("APPDATA");
        if (appdata == null) appdata = System.getProperty("user.home");
        WORLDS_DIR = Paths.get(appdata, "Worldcraft", "worlds");
        try {
            Files.createDirectories(WORLDS_DIR);
        } catch (IOException ignored) {
        }

        BIOME_BY_NAME.put(Biomes.PLAIN.getName(), Biomes.PLAIN);
        BIOME_BY_NAME.put(Biomes.HILL.getName(), Biomes.HILL);
        BIOME_BY_NAME.put(Biomes.MOUNTAIN.getName(), Biomes.MOUNTAIN);
        BIOME_BY_NAME.put(Biomes.BEACH.getName(), Biomes.BEACH);
        BIOME_BY_NAME.put(Biomes.MESA.getName(), Biomes.MESA);
        BIOME_BY_NAME.put(Biomes.DESERT.getName(), Biomes.DESERT);
        BIOME_BY_NAME.put(Biomes.OCEAN.getName(), Biomes.OCEAN);
        BIOME_BY_NAME.put(Biomes.FOREST.getName(), Biomes.FOREST);
        BIOME_BY_NAME.put(Biomes.BIRCH_FOREST.getName(), Biomes.BIRCH_FOREST);

        BIOME_BY_ID = new Biome[] {
                Biomes.PLAIN,
                Biomes.HILL,
                Biomes.MOUNTAIN,
                Biomes.BEACH,
                Biomes.MESA,
                Biomes.DESERT,
                Biomes.OCEAN,
                Biomes.FOREST,
                Biomes.BIRCH_FOREST
        };

        for (byte i = 0; i < BIOME_BY_ID.length; i++) {
            BIOME_TO_ID.put(BIOME_BY_ID[i], i);
        }
    }

    private static Path worldDir(Seed seed) {
        return WORLDS_DIR.resolve(Long.toString(seed.getLong()));
    }

    private static Path ensureWorldDir(Seed seed) {
        Path dir = worldDir(seed);
        try {
            Files.createDirectories(dir.resolve("chunks"));
            Files.createDirectories(dir.resolve("player"));
            Files.createDirectories(dir.resolve("entities"));
        } catch (IOException ignored) {
        }
        return dir;
    }

    private static Path chunkFile(World world, int x, int z) {
        return ensureWorldDir(world.getSeed()).resolve("chunks").resolve(x + "_" + z + ".bin");
    }

    private static Path metaFile(World world) {
        return ensureWorldDir(world.getSeed()).resolve("world.dat");
    }

    private static void waitForSave(Path file) {
        while (SAVING_FILES.contains(file)) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public static boolean worldExists(Seed seed) {
        return Files.exists(worldDir(seed));
    }

    /**
     * Returns the list of worlds present on disk with their name and seed.
     */
    public static java.util.List<WorldInfo> listWorldInfos() {
        java.util.List<WorldInfo> infos = new java.util.ArrayList<>();
        try (java.nio.file.DirectoryStream<java.nio.file.Path> stream = java.nio.file.Files.newDirectoryStream(WORLDS_DIR)) {
            for (java.nio.file.Path path : stream) {
                if (java.nio.file.Files.isDirectory(path)) {
                    try {
                        Seed seed = Seed.create(path.getFileName().toString());
                        String name = "World " + seed.getLong();
                        Path meta = path.resolve("world.dat");
                        if (java.nio.file.Files.exists(meta)) {
                            try (DataInputStream in = new DataInputStream(java.nio.file.Files.newInputStream(meta))) {
                                in.readLong();
                                in.readInt();
                                in.readInt();
                                if (in.available() > 0) name = in.readUTF();
                            } catch (Exception ignored) {
                            }
                        }
                        infos.add(new WorldInfo(seed, name));
                    } catch (Exception ignored) {
                    }
                }
            }
        } catch (IOException e) {
            Game.GAME.errorLog(e);
        }
        return infos;
    }

    public static boolean chunkExists(World world, int x, int z) {
        Path file = chunkFile(world, x, z);
        waitForSave(file);
        return Files.exists(file);
    }

    public static void saveWorldMeta(World world) {
        Path file = metaFile(world);
        try (DataOutputStream out = new DataOutputStream(Files.newOutputStream(file))) {
            out.writeLong(world.getSeed().getLong());
            out.writeInt(world.getXSpawn());
            out.writeInt(world.getZSpawn());
            out.writeUTF(world.getName() == null ? "" : world.getName());
            out.writeByte(world.getWorldType().ordinal());
        } catch (IOException e) {
            Game.GAME.errorLog(e);
        }
    }

    public static void loadWorldMeta(World world) {
        Path file = metaFile(world);
        if (!Files.exists(file)) return;
        try (DataInputStream in = new DataInputStream(Files.newInputStream(file))) {
            long seed = in.readLong();
            if (seed != world.getSeed().getLong()) {
                Game.GAME.warn("Seed mismatch while loading world metadata");
            }
            world.setSpawnPosition(in.readInt(), in.readInt());
            try {
                world.setName(in.readUTF());
            } catch (IOException ignored) {
                world.setName("World " + world.getSeed().getLong());
            }
            try {
                byte type = in.readByte();
                if (type >= 0 && type < WorldType.values().length) {
                    world.setWorldType(WorldType.values()[type]);
                }
            } catch (IOException ignored) {
                world.setWorldType(WorldType.NORMAL);
            }
        } catch (IOException e) {
            Game.GAME.errorLog(e);
        }
    }

    private static void writeChunk(Chunk chunk) throws IOException {
        Path file = chunkFile(chunk.getWorld(), chunk.getX(), chunk.getZ());
        Path tmp = file.resolveSibling(file.getFileName().toString() + ".tmp");
        int height = chunk.getWorld().getHeigth();
        int blockCount = Game.CHUNK_SIZE * height * Game.CHUNK_SIZE;
        byte[] buffer = new byte[blockCount * 4];
        java.nio.ByteBuffer bb = java.nio.ByteBuffer.wrap(buffer);

        for (int x = 0; x < Game.CHUNK_SIZE; x++)
            for (int y = 0; y < height; y++)
                for (int z = 0; z < Game.CHUNK_SIZE; z++) {
                    Block b = chunk.getBlocks()[x][y][z];
                    bb.putShort(b.getMaterialID());
                    bb.put(b.getModel().getId());
                    bb.put(b.getState());
                }

        byte[] biomes = new byte[Game.CHUNK_SIZE * Game.CHUNK_SIZE];
        int idx = 0;
        for (int x = 0; x < Game.CHUNK_SIZE; x++)
            for (int z = 0; z < Game.CHUNK_SIZE; z++) {
                Biome b = chunk.getBiome(chunk.getBlocks()[x][0][z]);
                biomes[idx++] = b == null ? (byte) -1 : BIOME_TO_ID.getOrDefault(b, (byte) -1);
            }

        try (DataOutputStream out = new DataOutputStream(Files.newOutputStream(tmp))) {
            out.write(buffer);
            out.write(biomes);
        }

        try {
            Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public static void saveChunkSync(Chunk chunk) {
        Path file = chunkFile(chunk.getWorld(), chunk.getX(), chunk.getZ());
        SAVING_FILES.add(file);
        try {
            writeChunk(chunk);
        } catch (Exception e) {
            Game.GAME.errorLog(e);
        } finally {
            SAVING_FILES.remove(file);
        }
    }

    public static void saveChunk(Chunk chunk) {
        saveChunk(chunk, null);
    }

    public static void saveChunk(Chunk chunk, Runnable onComplete) {
        Path file = chunkFile(chunk.getWorld(), chunk.getX(), chunk.getZ());
        SAVING_FILES.add(file);
        IO_EXECUTOR.submit(() -> {
            try {
                writeChunk(chunk);
            } catch (Exception e) {
                Game.GAME.errorLog(e);
            } finally {
                SAVING_FILES.remove(file);
                if (onComplete != null) onComplete.run();
            }
        });
    }

    public static boolean loadChunk(Chunk chunk) {
        Path file = chunkFile(chunk.getWorld(), chunk.getX(), chunk.getZ());
        waitForSave(file);
        if (!Files.exists(file)) return false;
        try (DataInputStream in = new DataInputStream(Files.newInputStream(file))) {
            int height = chunk.getWorld().getHeigth();
            int blockCount = Game.CHUNK_SIZE * height * Game.CHUNK_SIZE;
            byte[] buffer = new byte[blockCount * 4];
            in.readFully(buffer);
            java.nio.ByteBuffer bb = java.nio.ByteBuffer.wrap(buffer);

            for (int x = 0; x < Game.CHUNK_SIZE; x++)
                for (int y = 0; y < height; y++)
                    for (int z = 0; z < Game.CHUNK_SIZE; z++) {
                        Block b = chunk.getBlocks()[x][y][z];
                        short mat = bb.getShort();
                        byte model = bb.get();
                        byte state = bb.get();
                        if (mat >= 0)
                            b.setMaterial(Materials.getById(mat));
                        b.setModel(Model.getById(model));
                        b.setState(state);
                    }

            byte[] biomes = new byte[Game.CHUNK_SIZE * Game.CHUNK_SIZE];
            in.readFully(biomes);
            int idx = 0;
            for (int x = 0; x < Game.CHUNK_SIZE; x++)
                for (int z = 0; z < Game.CHUNK_SIZE; z++) {
                    byte id = biomes[idx++];
                    if (id >= 0 && id < BIOME_BY_ID.length) {
                        chunk.setBiome(chunk.getBlocks()[x][0][z], BIOME_BY_ID[id]);
                    }
                }
        } catch (IOException e) {
            Game.GAME.errorLog(e);
            return false;
        }
        chunk.setGenerated(true);
        chunk.updateAllBlock();
        chunk.setToUpdate(true);
        return true;
    }

    public static boolean loadLightChunk(LightChunk chunk) {
        Path file = chunkFile(chunk.getWorld(), chunk.getX(), chunk.getZ());
        waitForSave(file);
        if (!Files.exists(file)) return false;
        try (DataInputStream in = new DataInputStream(Files.newInputStream(file))) {
            int height = chunk.getWorld().getHeigth();
            int blockCount = Game.CHUNK_SIZE * height * Game.CHUNK_SIZE;
            byte[] buffer = new byte[blockCount * 4];
            in.readFully(buffer);
            java.nio.ByteBuffer bb = java.nio.ByteBuffer.wrap(buffer);

            for (int x = 0; x < Game.CHUNK_SIZE; x++)
                for (int y = 0; y < height; y++)
                    for (int z = 0; z < Game.CHUNK_SIZE; z++) {
                        short mat = bb.getShort();
                        bb.get(); // model
                        bb.get(); // state
                        if (mat >= 0)
                            chunk.getMaterials()[x][y][z] = Materials.getById(mat);
                    }

            byte[] biomes = new byte[Game.CHUNK_SIZE * Game.CHUNK_SIZE];
            in.readFully(biomes); // ignore biomes
        } catch (IOException e) {
            Game.GAME.errorLog(e);
            return false;
        }
        chunk.setGenerated(true);
        chunk.updateAllBlock();
        chunk.setToUpdate(true);
        return true;
    }
    public static void loadChunkAsync(Chunk chunk, Runnable onComplete) {
        chunk.setLoading(true);
        IO_EXECUTOR.submit(() -> {
            try {
                loadChunk(chunk);
            } finally {
                chunk.setLoading(false);
                if (onComplete != null) onComplete.run();
            }
        });
    }

    public static void loadLightChunkAsync(LightChunk chunk, Runnable onComplete) {
        chunk.setLoading(true);
        IO_EXECUTOR.submit(() -> {
            try {
                loadLightChunk(chunk);
            } finally {
                chunk.setLoading(false);
                if (onComplete != null) onComplete.run();
            }
        });
    }

    /**
     * Queues a light chunk to be loaded later. This avoids launching too many
     * I/O tasks simultaneously when a large area is discovered.
     */
    public static void queueLightChunkLoad(LightChunk chunk, Runnable onComplete) {
        LIGHT_LOAD_QUEUE.add(new QueuedLightLoad(chunk, onComplete));
    }

    /**
     * Processes a few queued light chunk loads. Should be called regularly from
     * the game loop to gradually load far chunks.
     */
    public static void processQueuedLightLoads() {
        int processed = 0;
        QueuedLightLoad q;
        while (processed < MAX_LIGHT_LOADS_PER_TICK && (q = LIGHT_LOAD_QUEUE.poll()) != null) {
            loadLightChunkAsync(q.chunk, q.onComplete);
            processed++;
        }
    }

    /* ***************************** Player & Entities ****************************** */

    private static Path playerFile(World world) {
        return ensureWorldDir(world.getSeed()).resolve("player").resolve("player.dat");
    }

    private static Path entitiesFile(World world) {
        return ensureWorldDir(world.getSeed()).resolve("entities").resolve("entities.dat");
    }

    public static void savePlayer(World world, Player player) {
        Path file = playerFile(world);
        try (DataOutputStream out = new DataOutputStream(Files.newOutputStream(file))) {
            var loc = player.getLocation();
            out.writeDouble(loc.getX());
            out.writeDouble(loc.getY());
            out.writeDouble(loc.getZ());
            out.writeFloat(loc.getYaw());
            out.writeFloat(loc.getPitch());
            out.writeInt(player.getSelectedSlot());
            out.writeByte(player.getGamemode().ordinal());
            var items = player.getInventory().getItems();
            out.writeInt(items.length);
            for (var it : items) {
                if (it == null) {
                    out.writeBoolean(false);
                } else {
                    out.writeBoolean(true);
                    out.writeShort((short) it.getMaterial().getId());
                    out.writeByte(it.getModel().getId());
                    out.writeInt(it.getQuantity());
                }
            }
        } catch (IOException e) {
            Game.GAME.errorLog(e);
        }
    }

    public static boolean loadPlayer(World world, Player player) {
        Path file = playerFile(world);
        if (!Files.exists(file)) return false;
        try (DataInputStream in = new DataInputStream(Files.newInputStream(file))) {
            double x = in.readDouble();
            double y = in.readDouble();
            double z = in.readDouble();
            float yaw = in.readFloat();
            float pitch = in.readFloat();
            player.setLocation(new Location(world, x, y, z, yaw, pitch));
            player.setSelectedSlotRaw(in.readInt());
            try {
                byte gm = in.readByte();
                if (gm >= 0 && gm < Gamemode.values().length) {
                    player.setGamemode(Gamemode.values()[gm]);
                }
            } catch (IOException ignored) {
            }
            int len = in.readInt();
            var items = player.getInventory().getItems();
            for (int i = 0; i < len && i < items.length; i++) {
                boolean present = in.readBoolean();
                if (present) {
                    short mat = in.readShort();
                    byte model = in.readByte();
                    int qty = in.readInt();
                    items[i] = new ItemStack(
                            Materials.getById(mat), Model.getById(model), qty);
                } else {
                    items[i] = null;
                }
            }
            // Inventory will be synchronized with the GUI on the render thread
            // once loading is complete.
        } catch (IOException e) {
            Game.GAME.errorLog(e);
            return false;
        }
        return true;
    }

    public static void saveEntities(World world) {
        Path file = entitiesFile(world);
        try (DataOutputStream out = new DataOutputStream(Files.newOutputStream(file))) {
            out.writeInt(world.getEntities().size());
            for (var e : world.getEntities()) {
                out.writeUTF(e.getClass().getName());
                var loc = e.getLocation();
                out.writeDouble(loc.getX());
                out.writeDouble(loc.getY());
                out.writeDouble(loc.getZ());
                out.writeFloat(loc.getYaw());
                out.writeFloat(loc.getPitch());
                if (e instanceof fr.rhumun.game.worldcraftopengl.entities.ItemEntity item) {
                    out.writeBoolean(true);
                    out.writeShort((short) item.getMaterial().getId());
                    out.writeByte(item.getModel().getId());
                } else {
                    out.writeBoolean(false);
                }
            }
        } catch (IOException e) {
            Game.GAME.errorLog(e);
        }
    }

    public static void loadEntities(World world) {
        Path file = entitiesFile(world);
        if (!Files.exists(file)) return;
        try (DataInputStream in = new DataInputStream(Files.newInputStream(file))) {
            int count = in.readInt();
            for (int i = 0; i < count; i++) {
                String className = in.readUTF();
                double x = in.readDouble();
                double y = in.readDouble();
                double z = in.readDouble();
                float yaw = in.readFloat();
                float pitch = in.readFloat();
                boolean isItem = in.readBoolean();
                Entity entity = null;
                if (isItem) {
                    short mat = in.readShort();
                    byte model = in.readByte();
                    entity = new ItemEntity(
                            Model.getById(model), Materials.getById(mat),
                            new Location(world, x, y, z, yaw, pitch), 1);
                } else {
                    try {
                        Class<?> c = Class.forName(className);
                        var ctor = c.getConstructor(double.class, double.class, double.class, float.class, float.class);
                        entity = (Entity) ctor.newInstance(x, y, z, yaw, pitch);
                    } catch (Exception ex) {
                        Game.GAME.errorLog(ex);
                    }
                }
                if (entity != null) world.getEntities().add(entity);
            }
        } catch (IOException e) {
            Game.GAME.errorLog(e);
        }
    }

    public static void shutdown() {
        IO_EXECUTOR.shutdown();
        try {
            if (!IO_EXECUTOR.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)) {
                IO_EXECUTOR.shutdownNow();
            }
        } catch (InterruptedException e) {
            IO_EXECUTOR.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
