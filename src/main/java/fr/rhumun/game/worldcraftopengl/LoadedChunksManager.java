package fr.rhumun.game.worldcraftopengl;

import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.LightChunkRenderer;
import fr.rhumun.game.worldcraftopengl.worlds.AbstractChunk;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.worlds.LightChunk;
import fr.rhumun.game.worldcraftopengl.worlds.World;

import java.util.*;
import java.util.stream.Collectors;

import static fr.rhumun.game.worldcraftopengl.Game.*;

public class LoadedChunksManager {

    private static final int MAX_CHUNKS = 65536; // Nombre maximum de chunks (short = 2^16)

    private final Player player;
    private final Game game;

    private int lastChunkX = Integer.MIN_VALUE;
    private int lastChunkZ = Integer.MIN_VALUE;

    private final LinkedHashSet<Chunk> chunksToRender = new LinkedHashSet<>();
    private final LinkedHashSet<LightChunk> chunksToRenderLight = new LinkedHashSet<>();


    public LoadedChunksManager(Player player) {
        this.player = player;
        this.game = GAME;
    }

    public synchronized Set<Chunk> getChunksToRender() {
        return new LinkedHashSet<>(chunksToRender);
    }

    public synchronized Set<LightChunk> getChunksToRenderLight() {
        return new LinkedHashSet<>(chunksToRenderLight);
    }

    public synchronized void updateChunksGradually() {
        if(!game.isPlaying() || game.isPaused() || !UPDATE_WORLD_RENDER) return;

        World world = player.getLocation().getWorld();
        int centerX = player.getLocation().getChunk().getX();
        int centerZ = player.getLocation().getChunk().getZ();

        if (centerX == lastChunkX && centerZ == lastChunkZ) {
            return;
        }
        lastChunkX = centerX;
        lastChunkZ = centerZ;

        Set<AbstractChunk> keepChunks = new HashSet<>();

        for (int x = centerX - SHOW_DISTANCE; x <= centerX + SHOW_DISTANCE; x++) {
            for (int z = centerZ - SHOW_DISTANCE; z <= centerZ + SHOW_DISTANCE; z++) {
                int dist = (x - centerX) * (x - centerX) + (z - centerZ) * (z - centerZ);
                int distPlayer = (x*CHUNK_SIZE - player.getLocation().getXInt()) * (x*CHUNK_SIZE - player.getLocation().getXInt()) + (z*CHUNK_SIZE - player.getLocation().getZInt()) * (z*CHUNK_SIZE - player.getLocation().getZInt());

                AbstractChunk current = world.getChunks().getAbstractChunk(x, z);
                if (dist <= SIMULATION_DISTANCE*SIMULATION_DISTANCE) {
                    if (!(current instanceof Chunk)) {
                        // Créer ou convertir en Chunk (simulation)
                        current = world.getChunk(x, z, true, true);
                    }
                } else if (dist <= SHOW_DISTANCE*SHOW_DISTANCE) {
                    if (!(current instanceof LightChunk)) {
                        LightChunk lightChunk = world.getLightChunk(x, z, true);
                        lightChunk.setLoaded(true);
                        current = lightChunk;
                    }

                    LightChunkRenderer renderer = (LightChunkRenderer)(current.getRenderer());
                    renderer.setDistanceFromPlayer(distPlayer);
                }

                if (current != null) {
                    keepChunks.add(current);
                }
            }
        }

        for(AbstractChunk chunk : keepChunks) {
            if(chunk instanceof Chunk chunkF && !chunkF.isGenerated() && !chunkF.isLoading()) {
                world.getGenerator().addToGenerate(chunkF);
            }
        }

        // Déchargement de tout ce qui est hors zone
        for (Chunk chunk : chunksToRender) {
            if (!keepChunks.contains(chunk)) {
                chunk.unload();
            }
        }

        for (LightChunk lightChunk : chunksToRenderLight) {
            if (!keepChunks.contains(lightChunk)) {
                lightChunk.unload();
            }
        }

        // Mise à jour des listes internes
        chunksToRender.clear();
        chunksToRender.addAll(keepChunks.stream()
                .filter(c -> c instanceof Chunk)
                .map(c -> (Chunk) c)
                .sorted(Comparator.comparingInt(chunk -> {
                    int dx = chunk.getX() * CHUNK_SIZE - player.getLocation().getXInt();
                    int dz = chunk.getZ() * CHUNK_SIZE - player.getLocation().getZInt();
                    return -(dx * dx + dz * dz); // tri du plus loin au plus proche
                }))
                .collect(Collectors.toCollection(LinkedHashSet::new)));

        chunksToRenderLight.clear();
        chunksToRenderLight.addAll(keepChunks.stream()
                .filter(c -> c instanceof LightChunk)
                .map(c -> (LightChunk) c)
                .collect(Collectors.toCollection(LinkedHashSet::new)));

        GAME.getGraphicModule().changeLoadedBlocks();
    }


    public void printChunksMap() {
        int centerX = player.getLocation().getChunk().getX();
        int centerZ = player.getLocation().getChunk().getZ();

        StringBuilder builder = new StringBuilder();
        for (int z = centerZ + SHOW_DISTANCE; z >= centerZ - SHOW_DISTANCE; z--) {
            for (int x = centerX - SHOW_DISTANCE; x <= centerX + SHOW_DISTANCE; x++) {
                AbstractChunk chunk = game.getWorld().getChunks().exists(x, z)
                        ? game.getWorld().getChunks().getAbstractChunk(x, z)
                        : null;

                if (chunk instanceof Chunk) {
                    if(!chunk.isLoaded()) builder.append("P ");
                    else if(!chunk.isGenerated()) builder.append("G ");
                    else builder.append("F ");
                } else if (chunk instanceof LightChunk) {
                    if(chunk.isGenerated()) builder.append("L ");
                    else if(!game.getGraphicModule().getLoadedFarChunks().contains(chunk)) builder.append("I ");
                    else if(chunk.isToUnload()) builder.append("U ");
                    else builder.append("R ");
                } else {
                    builder.append("\\ ");
                }
            }
            builder.append("\n");
        }

        GAME.log("Carte des chunks :\n" + builder);
    }



}