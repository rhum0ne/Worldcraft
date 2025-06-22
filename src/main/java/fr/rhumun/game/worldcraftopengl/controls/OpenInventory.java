package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.entities.player.Player;

public class OpenInventory extends Control{

    public OpenInventory(){
        super(false, true);
    }
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
