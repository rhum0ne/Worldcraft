package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.pause_menu;

import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Button;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

public class MenuButton extends Button {

    public MenuButton(int x, int y, Gui container) {
        super(x, y, 400, 20, container, "Quitter le monde");
    }

    @Override
    public void onClick(Player player) {
        GAME.quitWorld();
    }
}
