package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.DebugMenu;

import static fr.rhumun.game.worldcraftopengl.Game.SHOWING_FPS;

public class ShowFPS extends Control{
    @Override
    public void onKeyPressed(Player player) {
        DebugMenu debugMenu = getGame().getGraphicModule().getGuiModule().getDebugMenu();
        debugMenu.setShowed(!debugMenu.isShowed());
        SHOWING_FPS = !SHOWING_FPS;
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
