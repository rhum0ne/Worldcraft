package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.Player;

public class LeftClick extends Control {
    @Override
    public void onKeyPressed(Player player) {
        player.breakBlock();
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
