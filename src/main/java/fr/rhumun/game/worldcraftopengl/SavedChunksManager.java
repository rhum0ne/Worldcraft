package fr.rhumun.game.worldcraftopengl;

import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import lombok.Getter;

import java.util.*;

import static fr.rhumun.game.worldcraftopengl.Game.*;

@Getter
public class SavedChunksManager {

    private final Player player;
    private final Game game;

    private Set<Chunk> chunksToRender = new LinkedHashSet<>();
    private Chunk centralChunk;


    public SavedChunksManager(Player player) {
        this.player = player;
        this.game = GAME;
    }

    public void tryLoadChunks(){
        if(!GENERATION) {
            if(chunksToRender.isEmpty()) chunksToRender.add(player.getLocation().getChunk());
            return;
        }
        Chunk chunk = player.getLocation().getChunk();
        if(chunk == null) {
            game.log("ERROR: Player's chunk seems to be null");
            return;
        }
        if(centralChunk == null) loadChunks(chunk);
        else if(!chunk.equals(centralChunk)) loadChunks(chunk);
    }

    private LinkedHashSet<Chunk> getChunksToLoad() {
        LinkedHashSet<Chunk> chunks = new LinkedHashSet<>();
        PriorityQueue<ChunkDistance> chunkQueue = new PriorityQueue<>(new Comparator<ChunkDistance>() {
            @Override
            public int compare(ChunkDistance c1, ChunkDistance c2) {
                return Double.compare(c2.distance, c1.distance); // Trier du plus éloigné au plus proche
            }
        });

        int centerX = this.centralChunk.getX();
        int centerZ = this.centralChunk.getZ();

        // On parcourt les chunks dans un carré qui englobe le cercle
        for (int x = centerX - SHOW_DISTANCE; x <= centerX + SHOW_DISTANCE; x++) {
            for (int z = centerZ - SHOW_DISTANCE; z <= centerZ + SHOW_DISTANCE; z++) {
                // Calculer la distance au centre du chunk du joueur
                double distance = Math.sqrt(Math.pow(centerX - x, 2) + Math.pow(centerZ - z, 2));

                // Si la distance est dans le rayon de chargement, on l'ajoute
                if (distance <= SHOW_DISTANCE) {
                    Chunk chunk = player.getLocation().getWorld().getChunk(x, z, true);
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
        this.centralChunk = chunk;

        Set<Chunk> toLoad = getChunksToLoad();

        for(Chunk loadedChunk : toLoad){
            if(this.chunksToRender.contains(loadedChunk)) continue;
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
    }
}
