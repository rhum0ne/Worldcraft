package fr.rhumun.game.worldcraftopengl.outputs.graphic;

import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.AbstractChunkRenderer;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.ChunkRenderer;
import fr.rhumun.game.worldcraftopengl.worlds.AbstractChunk;

import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChunkLoader extends TimerTask {

    private final GraphicModule graphicModule;
    private final Player player;

    private ExecutorService executor;

    public ChunkLoader(GraphicModule graphicModule, Player player){
        this.graphicModule = graphicModule;
        this.player = player;

        executor = Executors.newFixedThreadPool(1);
    }

    @Override
    public void run() {
        if(!graphicModule.isInitialized()) return;

        player.getWorld().getGenerator().processChunkQueue();
        player.getLoadedChunksManager().updateChunksGradually();
    }

    public void updateDataFor(AbstractChunkRenderer chunk){
        this.executor.submit(chunk::updateData);
    }
}
