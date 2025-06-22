package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.entities.player.Player;

public class MoveLeft  extends Control {
    @Override
    public void onKeyPressed(Player player) {
        player.getMovements()[2] -= 1;
    }

    @Override
    public void onKeyReleased(Player player) {
        player.getMovements()[2] += 1;
    }
}
