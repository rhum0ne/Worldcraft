package fr.rhumun.game.worldcraftopengl;

import fr.rhumun.game.worldcraftopengl.controls.Controls;

import java.util.ConcurrentModificationException;
import java.util.TimerTask;

public class GameLoop extends TimerTask {

    private final Player player;
    private final Game game;

    public GameLoop(Game game, Player player){
        this.game = game;
        this.player = player;
    }
    @Override
    public void run() {
        if(game.getGraphicModule() == null) return;
        player.getSavedChunksManager().tryLoadChunks();
        try {
            for (Controls control : game.getPressedKeys()) {
                control.press(player);
                if (!control.isRepeatable()) game.pressedKeys.remove(control);
            }
        } catch(ConcurrentModificationException e){

        }
    }
}
