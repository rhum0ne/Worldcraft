package fr.rhumun.game.worldcraftopengl;

import java.util.TimerTask;

public class GameLoop extends TimerTask {

    private Player player;
    private Game game;

    public GameLoop(Game game, Player player){
        this.game = game;
        this.player = player;
    }
    @Override
    public void run() {

    }
}
