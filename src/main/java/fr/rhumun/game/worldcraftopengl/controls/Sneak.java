package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.entities.player.Player;

public class Sneak extends Control{
    @Override
    public void onKeyPressed(Player player) {
        if(player.isFlying() || player.isSwimming()){
            player.getMovements()[1] -= 1;
            return;
        }
        player.setSneaking(true);
    }

    @Override
    public void onKeyReleased(Player player) {
        if(player.isFlying() || player.isSwimming()){
            player.getMovements()[1] += 1;
        }
        player.setSneaking(false);
    }
}
