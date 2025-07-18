package fr.rhumun.game.worldcraftopengl;

import fr.rhumun.game.worldcraftopengl.controls.Controls;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.entities.physics.Movements;
import fr.rhumun.game.worldcraftopengl.worlds.utils.fluids.FluidSimulator;


public class GameLoop extends Thread {

    private final Player player;
    private final Game game;

    private long time;
    private long previousUpdate;
    private final int rate = 60;

    public GameLoop(Game game, Player player){
        this.game = game;
        this.player = player;
        this.setName("GameLoop");

        this.setPriority(Thread.MAX_PRIORITY);
        this.time = System.currentTimeMillis();
    }
    @Override
    public void run() {
        game.log("Starting GameLoop");

        while (game.isPlaying() ) {
            long nextTime = time + 1000;
            long currentTime = System.currentTimeMillis();
            long delta = currentTime - previousUpdate;
            if(currentTime > nextTime) {
                this.time = currentTime;
            } else if(delta < 1000 / rate){
                try {
                    Thread.sleep(1000 / rate - delta);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                continue;
            } else if (delta > Game.LAG_SPIKE_LIMIT) {
                game.warn("Lag Spike detected : " + delta + " ms");
            }

            if (game.getGraphicModule() != null) {
                for (Controls control : game.getPressedKeys()) {
                    control.press(player);
                    if (!control.isRepeatable()) game.pressedKeys.remove(control);
                }

                if(game.getGameState() == GameState.RUNNING) {
                    player.getWorld().updateTime();
                    Movements.applyMovements(player);
                    player.update();

                    player.getWorld().updateEntities(player, 48);
                    FluidSimulator.tick();
                }
            }

            previousUpdate = currentTime;
            try {
                Thread.sleep(1000 / rate);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        game.log("GameLoop Ended");
    }

    public float getDeltaTime() {
        return (System.currentTimeMillis() - previousUpdate) / 1000f;
    }
}
