package fr.rhumun.game.worldcraftopengl.outputs.graphic;

import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers.AbstractChunkRenderer;

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
        if(!graphicModule.isInitialized() || player.getWorld() == null) return;

        player.getWorld().getGenerator().processChunkQueue();
        fr.rhumun.game.worldcraftopengl.worlds.SaveManager.processQueuedLightLoads();
        player.getLoadedChunksManager().updateChunksGradually();
    }

    public void updateDataFor(AbstractChunkRenderer chunk){
        if(chunk.isUpdating()) return;
        chunk.setUpdating(true);
        this.executor.submit(() -> {
            chunk.updateData();
            chunk.setUpdating(false);
        });
    }
}
