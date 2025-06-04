package fr.rhumun.game.worldcraftopengl.worlds;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.worlds.generators.biomes.Biome;
import fr.rhumun.game.worldcraftopengl.worlds.generators.biomes.Biomes;
import fr.rhumun.game.worldcraftopengl.worlds.generators.utils.Seed;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Utility class handling saving/loading of world and chunk data.
 */
public class SaveManager {

    /** Directory where all worlds are stored */
    public static final Path WORLDS_DIR;

    private static final Map<String, Biome> BIOME_BY_NAME = new HashMap<>();
    private static final ExecutorService IO_EXECUTOR = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "SaveManager-IO");
        t.setPriority(Thread.MIN_PRIORITY);
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
        Path dir = WORLDS_DIR.resolve(Long.toString(seed.getLong()));
        try {
            Files.createDirectories(dir.resolve("chunks"));
        } catch (IOException ignored) {
        }
        return dir;
    }

    private static Path chunkFile(World world, int x, int z) {
        return worldDir(world.getSeed()).resolve("chunks").resolve(x + "_" + z + ".bin");
    }

    private static Path metaFile(World world) {
        return worldDir(world.getSeed()).resolve("world.dat");
    }

    public static boolean worldExists(Seed seed) {
        return Files.exists(worldDir(seed));
    }

    public static boolean chunkExists(World world, int x, int z) {
        return Files.exists(chunkFile(world, x, z));
    }

    public static void saveWorldMeta(World world) {
        Path file = metaFile(world);
        try (DataOutputStream out = new DataOutputStream(Files.newOutputStream(file))) {
            out.writeLong(world.getSeed().getLong());
            out.writeInt(world.getXSpawn());
            out.writeInt(world.getZSpawn());
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
        } catch (IOException e) {
            Game.GAME.errorLog(e);
        }
    }

    private static void writeChunk(Chunk chunk) {
        Path file = chunkFile(chunk.getWorld(), chunk.getX(), chunk.getZ());
        try (DataOutputStream out = new DataOutputStream(Files.newOutputStream(file))) {
            for (int x = 0; x < Game.CHUNK_SIZE; x++)
                for (int y = 0; y < chunk.getWorld().getHeigth(); y++)
                    for (int z = 0; z < Game.CHUNK_SIZE; z++)
                        out.writeShort(chunk.getBlocks()[x][y][z].getMaterialID());

            for (int x = 0; x < Game.CHUNK_SIZE; x++)
                for (int z = 0; z < Game.CHUNK_SIZE; z++) {
                    Biome b = chunk.getBiome(chunk.getBlocks()[x][0][z]);
                    out.writeUTF(b == null ? "" : b.getName());
                }
        } catch (IOException e) {
            Game.GAME.errorLog(e);
        }
    }

    public static void saveChunkSync(Chunk chunk) {
        writeChunk(chunk);
    }

    public static void saveChunk(Chunk chunk) {
        saveChunk(chunk, null);
    }

    public static void saveChunk(Chunk chunk, Runnable onComplete) {
        IO_EXECUTOR.submit(() -> {
            writeChunk(chunk);
            if (onComplete != null) onComplete.run();
        });
    }

    public static boolean loadChunk(Chunk chunk) {
        Path file = chunkFile(chunk.getWorld(), chunk.getX(), chunk.getZ());
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

    public static void shutdown() {
        IO_EXECUTOR.shutdown();
    }
}
