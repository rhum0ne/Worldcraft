package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.Player;

public class Sprint extends Control{
    @Override
    public void onKeyPressed(Player player) {
        player.setSprinting(true);
    }

    @Override
    public void onKeyReleased(Player player) {
        player.setSprinting(false);
    }
}
