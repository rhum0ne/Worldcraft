package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.Player;

public class Escape extends Control {
    @Override
    public void onKeyPressed(Player player) {
        player.getGame().setPaused(!player.getGame().isPaused());
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
