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

        var target = player.getTargetEntity();
        if (target != null) {
            player.attack(target);
        } else {
            player.breakBlock();
        }
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
