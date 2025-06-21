package fr.rhumun.game.worldcraftopengl.worlds;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.worlds.generators.biomes.Biome;
import fr.rhumun.game.worldcraftopengl.worlds.generators.biomes.Biomes;
import fr.rhumun.game.worldcraftopengl.worlds.generators.utils.Seed;
import fr.rhumun.game.worldcraftopengl.worlds.WorldInfo;

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
    private static final Set<Path> SAVING_FILES = ConcurrentHashMap.newKeySet();
    private static final ExecutorService IO_EXECUTOR = Executors.newFixedThreadPool(
            Math.max(3, Runtime.getRuntime().availableProcessors() / 2), r -> {
            Thread t = new Thread(r, "SaveManager-IO");
            t.setPriority(10);
            return t;
    });


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
    }

    private static Path worldDir(Seed seed) {
        return WORLDS_DIR.resolve(Long.toString(seed.getLong()));
    }

    private static Path ensureWorldDir(Seed seed) {
        Path dir = worldDir(seed);
        try {
            Files.createDirectories(dir.resolve("chunks"));
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
        } catch (IOException e) {
            Game.GAME.errorLog(e);
        }
    }

    private static void writeChunk(Chunk chunk) throws IOException {
        Path file = chunkFile(chunk.getWorld(), chunk.getX(), chunk.getZ());
        Path tmp = file.resolveSibling(file.getFileName().toString() + ".tmp");
        try (DataOutputStream out = new DataOutputStream(Files.newOutputStream(tmp))) {
            for (int x = 0; x < Game.CHUNK_SIZE; x++)
                for (int y = 0; y < chunk.getWorld().getHeigth(); y++)
                    for (int z = 0; z < Game.CHUNK_SIZE; z++)
                        out.writeShort(chunk.getBlocks()[x][y][z].getMaterialID());

            for (int x = 0; x < Game.CHUNK_SIZE; x++)
                for (int z = 0; z < Game.CHUNK_SIZE; z++) {
                    Biome b = chunk.getBiome(chunk.getBlocks()[x][0][z]);
                    out.writeUTF(b == null ? "" : b.getName());
                }
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
            for (int xi = 0; xi < Game.CHUNK_SIZE; xi++)
                for (int y = 0; y < chunk.getWorld().getHeigth(); y++)
                    for (int zi = 0; zi < Game.CHUNK_SIZE; zi++) {
                        short mat = in.readShort();
                        if (mat >= 0)
                            chunk.getBlocks()[xi][y][zi].setMaterial(Material.getById(mat));
                    }
            for (int xi = 0; xi < Game.CHUNK_SIZE; xi++)
                for (int zi = 0; zi < Game.CHUNK_SIZE; zi++) {
                    String name = in.readUTF();
                    Biome b = BIOME_BY_NAME.get(name);
                    if (b != null)
                        chunk.setBiome(chunk.getBlocks()[xi][0][zi], b);
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
            for (int xi = 0; xi < Game.CHUNK_SIZE; xi++)
                for (int y = 0; y < chunk.getWorld().getHeigth(); y++)
                    for (int zi = 0; zi < Game.CHUNK_SIZE; zi++) {
                        short mat = in.readShort();
                        if (mat >= 0)
                            chunk.getMaterials()[xi][y][zi] = Material.getById(mat);
                    }
            for (int xi = 0; xi < Game.CHUNK_SIZE; xi++)
                for (int zi = 0; zi < Game.CHUNK_SIZE; zi++) {
                    in.readUTF(); // skip biome name
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
