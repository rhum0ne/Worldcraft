package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.Player;

public class MoveUp extends Control{
    @Override
    public void onKeyPressed(Player player) {
        player.addY(0.2);
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}