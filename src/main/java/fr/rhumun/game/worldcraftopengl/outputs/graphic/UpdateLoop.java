package fr.rhumun.game.worldcraftopengl.outputs.graphic;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.entities.Player;

public class UpdateLoop extends Thread {

    private final GraphicModule graphicModule;
    private final Game game;
    private final Player player;

    public UpdateLoop(GraphicModule graphicModule, Game game, Player player) {
        this.graphicModule = graphicModule;
        this.game = game;
        this.player = player;
    }

    @Override
    public void run() {
        if (game.getGraphicModule() == null) return;

        //player.getSavedChunksManager().tryLoadChunks();
        game.getGraphicModule().updateViewMatrix();
    }
}
