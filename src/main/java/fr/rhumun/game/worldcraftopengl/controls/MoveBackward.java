package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.Player;

public class MoveBackward  extends Control{
    @Override
    public void onKeyPressed(Player player) {
        System.out.println("s");
        player.addX(-0.1);
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
