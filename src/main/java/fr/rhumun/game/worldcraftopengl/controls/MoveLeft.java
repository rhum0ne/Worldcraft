package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.Player;

public class MoveLeft  extends Control {
    @Override
    public void onKeyPressed(Player player) {
        System.out.println("q");
        player.addZ(-0.1);
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
