package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.DebugMenu;

public class ShowFPS extends Control{
    @Override
    public void onKeyPressed(Player player) {
        DebugMenu debugMenu = getGame().getGraphicModule().getGuiModule().getDebugMenu();
        debugMenu.setShowed(!debugMenu.isShowed());
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
