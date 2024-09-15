package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.Player;
import lombok.Getter;

@Getter
public abstract class Control {
    private boolean isRepeatable;

    public Control(boolean isRepeatable){
        this.isRepeatable = isRepeatable;
    }

    public Control(){ this(false); }

    public abstract void onKeyPressed(Player player);
    public abstract void onKeyReleased(Player player);
}
