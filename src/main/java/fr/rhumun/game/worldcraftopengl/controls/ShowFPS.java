package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.Player;

import static fr.rhumun.game.worldcraftopengl.Game.SHOWING_FPS;
import static fr.rhumun.game.worldcraftopengl.Game.SHOWING_GUIS;

public class ShowFPS extends Control{
    @Override
    public void onKeyPressed(Player player) {
        SHOWING_FPS = !SHOWING_FPS;
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
