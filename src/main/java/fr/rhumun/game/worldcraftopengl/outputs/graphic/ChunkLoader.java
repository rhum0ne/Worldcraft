package fr.rhumun.game.worldcraftopengl.outputs.graphic;

import fr.rhumun.game.worldcraftopengl.entities.Player;

import java.util.TimerTask;

public class ChunkLoader extends TimerTask {

    private final GraphicModule graphicModule;
    private final Player player;

    public ChunkLoader(GraphicModule graphicModule, Player player){
        this.graphicModule = graphicModule;
        this.player = player;
    }

    @Override
    public void run() {
        if(!graphicModule.isInitialized()) return;

        player.getSavedChunksManager().tryLoadChunks();
    }
}
