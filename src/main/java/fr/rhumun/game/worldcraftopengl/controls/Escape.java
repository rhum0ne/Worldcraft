package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.pause_menu.PauseGui;

public class Escape extends Control {

    public Escape(){
        super(false, true);
    }
    @Override
    public void onKeyPressed(Player player) {
        if(getGame().getGraphicModule().getGuiModule().hasGUIOpened()){
            getGame().getGraphicModule().getGuiModule().closeGUI();
        }else {
            player.openGui(new PauseGui());
        }
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
