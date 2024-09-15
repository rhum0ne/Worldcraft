package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.Player;

public class MoveRight extends Control{
    @Override
    public void onKeyPressed(Player player) {
        System.out.println("d");
        player.addZ(0.1);
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
