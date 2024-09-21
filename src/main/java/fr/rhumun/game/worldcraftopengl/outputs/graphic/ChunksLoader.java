package fr.rhumun.game.worldcraftopengl.outputs.graphic;

import fr.rhumun.game.worldcraftopengl.Player;

import java.util.TimerTask;

public class ChunksLoader extends TimerTask {

    private final Player player;

    public ChunksLoader(Player player){
        this.player = player;
    }
    @Override
    public void run() {
        player.getSavedChunksManager().tryLoadChunks();
    }
}
