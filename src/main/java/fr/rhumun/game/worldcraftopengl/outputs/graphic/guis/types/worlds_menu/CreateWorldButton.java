package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.worlds_menu;

import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Button;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.worlds_menu.CreateWorldGui;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

public class CreateWorldButton extends Button {

    public CreateWorldButton(int x, int y, Gui container) {
        super(x, y, container, "Nouveau Monde");
    }

    @Override
    public void onClick(Player player) {
        GAME.getGraphicModule().getGuiModule().openGUI(new CreateWorldGui());
    }
}
