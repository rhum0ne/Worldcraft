package fr.rhumun.game.worldcraftopengl.graphic;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.Player;
import fr.rhumun.game.worldcraftopengl.controls.Controls;

import java.util.ConcurrentModificationException;
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
