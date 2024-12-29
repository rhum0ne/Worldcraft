package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.Player;

public class OpenInventory extends Control{
    @Override
    public void onKeyPressed(Player player) {
        if(player.hasOpenedInventory()){
            player.closeInventory();
        }else{
            player.openInventory();
        }
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
