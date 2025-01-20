package fr.rhumun.game.worldcraftopengl;

import fr.rhumun.game.worldcraftopengl.controls.Controls;
import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.entities.physics.Movements;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.TimerTask;

public class GameLoop extends Thread {

    private final Player player;
    private final Game game;

    private long time;
    private long previousUpdate;
    private int rate = 60;

    public GameLoop(Game game, Player player){
        this.game = game;
        this.player = player;
        this.setName("GameLoop");

        this.time = System.currentTimeMillis();
    }
    @Override
    public void run() {
        game.log("Starting GameLoop");

        while (game.isPlaying) {
            long nextTime = time + 1000;
            long currentTime = System.currentTimeMillis();
            if(currentTime > nextTime)
                this.time = currentTime;
            else if(currentTime - previousUpdate < 1000/rate){
                //System.out.println(previousUpdate + " - " + currentTime + " = " + (previousUpdate - currentTime) + " < 1/" + rate);
                continue;
            }

            if (game.getGraphicModule() != null) {
                try {
                    for (Controls control : game.getPressedKeys()) {
                        control.press(player);
                        if (!control.isRepeatable()) game.pressedKeys.remove(control);
                    }
                } catch (ConcurrentModificationException e) {
                    continue;
                }
                Movements.applyMovements(player);
            }

            previousUpdate = currentTime;
        }

        game.log("GameLoop Ended");
    }
}
