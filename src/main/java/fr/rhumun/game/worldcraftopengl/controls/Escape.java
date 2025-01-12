package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.Player;

public class Escape extends Control {

    public Escape(){
        super(false, true);
    }
    @Override
    public void onKeyPressed(Player player) {
        if(getGame().getGraphicModule().getGuiModule().hasGUIOpened()){
            getGame().getGraphicModule().getGuiModule().closeGUI();
        }else {
            //else open PauseGui
            player.getGame().setPaused(!player.getGame().isPaused());
        }
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
