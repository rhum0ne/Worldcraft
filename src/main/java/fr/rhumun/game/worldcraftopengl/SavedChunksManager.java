package fr.rhumun.game.worldcraftopengl;

import fr.rhumun.game.worldcraftopengl.blocks.Block;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import static fr.rhumun.game.worldcraftopengl.Game.*;

@Getter
public class SavedChunksManager {

    private final Player player;

    private List<Chunk> chunksToRender = new ArrayList<>();
    private Chunk centralChunk;


    public SavedChunksManager(Player player) {
        this.player = player;
    }

    public void tryLoadChunks(){
        if(!GENERATION) {
            if(chunksToRender.isEmpty()) chunksToRender.add(player.getLocation().getChunk());
            return;
        }
        Chunk chunk = player.getLocation().getChunk();
        if(chunk == null) {
            System.out.println("ERROR: Player's chunk seems to be null");
            return;
        }
        if(centralChunk == null) loadChunks(chunk);
        else if(!chunk.equals(centralChunk)) loadChunks(chunk);
    }

    private ArrayList<Chunk> getChunksToLoad() {
        ArrayList<Chunk> chunks = new ArrayList<>();
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

        List<Chunk> toLoad = getChunksToLoad();

        for(Chunk loadedChunk : toLoad){
            if(this.chunksToRender.contains(loadedChunk)) continue;
            System.out.println("Starting first loading of " + loadedChunk);
            chunk.getRenderer().updateData();
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
