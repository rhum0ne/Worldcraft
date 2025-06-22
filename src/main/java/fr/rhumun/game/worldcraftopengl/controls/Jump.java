package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.entities.player.Player;

public class Jump extends Control{

    public Jump(){
        super(true);
    }

    @Override
    public void onKeyPressed(Player player) {
        if(player.isFlying() || player.isSwimming()){
            player.getMovements()[1] = 1;
            return;
        }
        player.jump();
    }

    @Override
    public void onKeyReleased(Player player) {
        if(player.isFlying() || player.isSwimming()){
            player.getMovements()[1] = 0;
        }
    }
}