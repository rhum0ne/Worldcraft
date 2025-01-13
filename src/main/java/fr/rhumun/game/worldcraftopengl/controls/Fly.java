package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.entities.Player;

public class Fly extends Control {
    @Override
    public void onKeyPressed(Player player) {
        player.setFlying(!player.isFlying());
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
