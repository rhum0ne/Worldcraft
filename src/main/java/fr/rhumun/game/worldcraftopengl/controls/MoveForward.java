package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.Player;

public class MoveForward  extends Control{
    @Override
    public void onKeyPressed(Player player) {
        System.out.println("z");
        player.addX(0.1);
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
