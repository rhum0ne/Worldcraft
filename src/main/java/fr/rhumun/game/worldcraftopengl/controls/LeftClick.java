package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.Player;

public class LeftClick extends Control {

    public LeftClick(){
        super(false, true);
    }
    @Override
    public void onKeyPressed(Player player) {
        if(getGame().isPaused()){
            getGame().getGraphicModule().getGuiModule().leftClick(player);
            return;
        }

        player.breakBlock();
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
