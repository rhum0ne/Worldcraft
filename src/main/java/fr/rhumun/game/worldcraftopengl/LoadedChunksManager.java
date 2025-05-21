package fr.rhumun.game.worldcraftopengl;

import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.worlds.AbstractChunk;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.worlds.LightChunk;
import fr.rhumun.game.worldcraftopengl.worlds.World;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

import static fr.rhumun.game.worldcraftopengl.Game.*;

@Getter
public class LoadedChunksManager {

    private static final int MAX_CHUNKS = 65536; // Nombre maximum de chunks (short = 2^16)

    private final Player player;
    private final Game game;

    private LinkedHashSet<Chunk> chunksToRender = new LinkedHashSet<>();
    private LinkedHashSet<LightChunk> chunksToRenderLight = new LinkedHashSet<>();


    private Chunk centralChunk;


    public LoadedChunksManager(Player player) {
        this.player = player;
        this.game = GAME;
    }

    public void updateChunksGradually() {
        if(!game.isPlaying() || game.isPaused) return;

        System.out.println("CHECKING CHUNKS");

        World world = player.getLocation().getWorld();
        int centerX = player.getLocation().getChunk().getX();
        int centerZ = player.getLocation().getChunk().getZ();

        Set<AbstractChunk> keepChunks = new HashSet<>();

        for (int x = centerX - SHOW_DISTANCE; x <= centerX + SHOW_DISTANCE; x++) {
            for (int z = centerZ - SHOW_DISTANCE; z <= centerZ + SHOW_DISTANCE; z++) {
                double dist = Math.sqrt((x - centerX) * (x - centerX) + (z - centerZ) * (z - centerZ));

                AbstractChunk current = world.getChunks().getAbstractChunk(x, z);

                if (dist <= SIMULATION_DISTANCE) {
                    if (!(current instanceof Chunk)) {
                        // Créer ou convertir en Chunk (simulation)
                        Chunk chunk = world.getChunk(x, z, true);
                        game.getWorld().getGenerator().tryGenerate(chunk);
                        chunk.setLoaded(true);
                        current = chunk;
                    }
                } else if (dist <= SHOW_DISTANCE) {
                    if (!(current instanceof LightChunk)) {
                        // Créer ou convertir en LightChunk (visuel uniquement)
                        LightChunk lightChunk = world.getLightChunk(x, z, true);
                        lightChunk.setLoaded(true);
                        current = lightChunk;
                    }
                }

                if (current != null) {
                    keepChunks.add(current);
                }
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
        chunksToRender = keepChunks.stream()
                .filter(c -> c instanceof Chunk)
                .map(c -> (Chunk) c)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        chunksToRenderLight = keepChunks.stream()
                .filter(c -> c instanceof LightChunk)
                .map(c -> (LightChunk) c)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        GAME.getGraphicModule().changeLoadedBlocks();
        System.out.println("Chunks : " + chunksToRender.size());
        System.out.println("Light Chunks : " + chunksToRenderLight.size());
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
                    builder.append("F ");
                } else if (chunk instanceof LightChunk) {
                    builder.append("L ");
                } else {
                    builder.append("\\ ");
                }
            }
            builder.append("\n");
        }

        GAME.log("Carte des chunks :\n" + builder);
    }



}