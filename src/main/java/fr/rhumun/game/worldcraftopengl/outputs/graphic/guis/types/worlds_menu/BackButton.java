package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.worlds_menu;

import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Button;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.title_menu.TitleMenuGui;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

public class BackButton extends Button {

    public BackButton(int x, int y, Gui container) {
        super(x, y, container, "Retour");
    }

    @Override
    public void onClick(Player player) {
        GAME.getGraphicModule().getGuiModule().openGUI(new TitleMenuGui());
    }
}
