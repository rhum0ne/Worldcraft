package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.Player;

public class RightClick extends Control {
    @Override
    public void onKeyPressed(Player player) {
        player.placeBlock(player.getSelectedMaterial());
        System.out.println(player.getSelectedMaterial());
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
