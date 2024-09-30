package fr.rhumun.game.worldcraftopengl;

import fr.rhumun.game.worldcraftopengl.blocks.Block;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static fr.rhumun.game.worldcraftopengl.Game.*;

@Getter
public class SavedChunksManager {

    private final Player player;

    private List<Chunk> chunksToRender = new ArrayList<>();
    private final List<Block> loadedBlocks = new ArrayList<>();
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
        if(centralChunk == null) loadChunks(chunk);
        else if(!chunk.equals(centralChunk)) loadChunks(chunk);
    }

    private ArrayList<Chunk> getChunksToLoad(){
        ArrayList<Chunk> chunks = new ArrayList<>();

        int X = this.centralChunk.getX();
        int Z = this.centralChunk.getZ();

        for(int x=X-SHOW_DISTANCE; x<X+SHOW_DISTANCE; x++){
            for(int z=Z-SHOW_DISTANCE; z<Z+SHOW_DISTANCE; z++){
                 chunks.add(player.getLocation().getWorld().getChunk(x,z, true));
            }
        }

        return chunks;
    }

    public void loadChunks(Chunk chunk){
        this.centralChunk = chunk;

        this.chunksToRender = this.getChunksToLoad();

        GAME.getGraphicModule().changeLoadedBlocks();
    }
}
