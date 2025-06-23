package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.entities.player.Player;

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

        if(player.isInCreativeMode()){
            player.breakBlock();
        }else{
            player.startBreaking();
        }
    }

    @Override
    public void onKeyReleased(Player player) {
        if(!player.isInCreativeMode())
            player.stopBreaking();
    }
}
