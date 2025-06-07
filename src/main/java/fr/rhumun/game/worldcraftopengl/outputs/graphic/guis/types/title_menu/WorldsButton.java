package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.title_menu;

import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Button;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.worlds_menu.WorldsGui;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

/**
 * Button that opens the world selection menu.
 */
public class WorldsButton extends Button {

    public WorldsButton(int x, int y, Gui container) {
        super(x, y, container, "Mondes");
    }

    @Override
    public void onClick(Player player) {
        GAME.getGraphicModule().getGuiModule().openGUI(new WorldsGui());
    }
}
