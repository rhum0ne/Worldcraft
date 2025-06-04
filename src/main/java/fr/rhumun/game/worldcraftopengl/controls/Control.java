package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.GameState;
import lombok.Getter;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

@Getter
public abstract class Control {
    private final boolean isRepeatable;
    private final boolean canExecuteInPause;
    private final Game game;

    public Control(boolean isRepeatable){
        this(isRepeatable, false);
    }
    public Control(boolean isRepeatable, boolean canExecuteInPause){
        this.isRepeatable = isRepeatable;
        this.canExecuteInPause = canExecuteInPause;
        this.game = GAME;
    }

    public Control(){ this(false, false); }

    public void testOnKeyPressed(Player player){
        if(game.getGameState() == GameState.RUNNING || canExecuteInPause && game.isPaused())
            onKeyPressed(player);
    }
    public void testOnKeyReleased(Player player){
        if(game.getGameState() == GameState.RUNNING || (canExecuteInPause && game.getGameState() != GameState.PAUSED))
            onKeyReleased(player);
    }

    public abstract void onKeyPressed(Player player);
    public abstract void onKeyReleased(Player player);
}
