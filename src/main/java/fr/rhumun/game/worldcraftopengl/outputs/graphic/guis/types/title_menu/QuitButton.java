package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.title_menu;

import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Button;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

public class QuitButton extends Button {

    public QuitButton(int x, int y, Gui container) {
        super(x, y, container, "Quitter");
    }

    @Override
    public void onClick(Player player) {
        GAME.closeGame();
    }

}
