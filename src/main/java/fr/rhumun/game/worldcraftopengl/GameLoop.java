package fr.rhumun.game.worldcraftopengl;

import fr.rhumun.game.worldcraftopengl.controls.Controls;
import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.entities.physics.Movements;

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

        try {
            for (Controls control : game.getPressedKeys()) {
                control.press(player);
                if (!control.isRepeatable()) game.pressedKeys.remove(control);
            }
        } catch(ConcurrentModificationException e){

        }
        Movements.applyMovements(player);
    }
}
