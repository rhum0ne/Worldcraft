package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.entities.player.Player;

import static fr.rhumun.game.worldcraftopengl.Game.SHOWING_GUIS;

public class HideGUIS extends Control{
    @Override
    public void onKeyPressed(Player player) {
        SHOWING_GUIS = !SHOWING_GUIS;
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
