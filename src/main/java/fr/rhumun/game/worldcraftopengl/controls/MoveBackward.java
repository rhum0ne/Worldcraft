package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.Player;

public class MoveBackward  extends Control{
    @Override
    public void onKeyPressed(Player player) {
        player.moveBackward(0.2);
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
