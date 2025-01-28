package fr.rhumun.game.worldcraftopengl;

import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import lombok.Getter;

import java.util.*;

import static fr.rhumun.game.worldcraftopengl.Game.*;

@Getter
public class LoadedChunksManager {

    private static final int MAX_CHUNKS = 65536; // Nombre maximum de chunks (short = 2^16)

    private final Player player;
    private final Game game;

    private LinkedHashSet<Chunk> chunksToRender = new LinkedHashSet<>();

    private Chunk centralChunk;


    public LoadedChunksManager(Player player) {
        this.player = player;
        this.game = GAME;
    }

    public void tryLoadChunks(){ //A REVOIR ENTIEREMENT -> LAGS
        game.getWorld().getGenerator().processChunkQueue();
        if(!GENERATION) {
            if(chunksToRender.isEmpty()) chunksToRender.add(player.getLocation().getChunk());
            return;
        }
        Chunk chunk = player.getLocation().getChunk();
        if(chunk == null) {
            game.errorLog("Player's chunk seems to be null");
            return;
        }
        if(centralChunk == null) loadChunks(chunk);
        else if(!chunk.equals(centralChunk)) loadChunks(chunk);
    }

    private LinkedHashSet<Chunk> getChunksToLoad() {
        LinkedHashSet<Chunk> chunks = new LinkedHashSet<>();
        PriorityQueue<ChunkDistance> chunkQueue = new PriorityQueue<>((c1, c2) -> {
            return Double.compare(c2.distance, c1.distance); // Trier du plus éloigné au plus proche
        });

        int centerX = this.centralChunk.getX();
        int centerZ = this.centralChunk.getZ();

        // On parcourt les chunks dans un carré qui englobe le cercle
        for (int x = centerX - SIMULATION_DISTANCE; x <= centerX + SIMULATION_DISTANCE; x++) {
            for (int z = centerZ - SIMULATION_DISTANCE; z <= centerZ + SIMULATION_DISTANCE; z++) {
                // Calculer la distance au centre du chunk du joueur
                double distance = Math.sqrt(Math.pow(centerX - x, 2) + Math.pow(centerZ - z, 2));

                // Si la distance est dans le rayon de chargement, on l'ajoute
                if (distance <= SIMULATION_DISTANCE) {
                    Chunk chunk = player.getLocation().getWorld().getChunk(x, z, false);
                    chunk.getRenderer().setDistanceFromPlayer((int) distance);
                    chunkQueue.add(new ChunkDistance(chunk, distance));
                }
            }
        }

        // Extraire les chunks dans l'ordre croissant de distance
        while (!chunkQueue.isEmpty()) {
            chunks.add(chunkQueue.poll().chunk);
        }

        return chunks;
    }

    private static class ChunkDistance {
        Chunk chunk;
        double distance;

        public ChunkDistance(Chunk chunk, double distance) {
            this.chunk = chunk;
            this.distance = distance;
        }
    }


    public void loadChunks(Chunk chunk){
        long start = System.currentTimeMillis();
        this.centralChunk = chunk;

        LinkedHashSet<Chunk> toLoad = getChunksToLoad();

        for(Chunk loadedChunk : toLoad){
            if(this.chunksToRender.contains(loadedChunk)) continue;
            game.getWorld().getGenerator().addToGenerate(loadedChunk);
            //game.log("Starting first loading of " + loadedChunk);
            chunk.setLoaded(true);
            GAME.getGraphicModule().addChunkToLoad(loadedChunk);
        }

        for(Chunk loadedChunk : this.chunksToRender){
            if(toLoad.contains(loadedChunk)) continue;
            loadedChunk.unload();
        }

        this.chunksToRender = toLoad;

        GAME.getGraphicModule().changeLoadedBlocks();
        long end = System.currentTimeMillis();
        GAME.debug("Updating loaded Chunks in " + (end - start) + " ms");
    }
}
