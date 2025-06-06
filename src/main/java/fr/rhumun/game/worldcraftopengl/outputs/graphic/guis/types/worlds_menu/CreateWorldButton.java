package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.worlds_menu;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Button;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;
import fr.rhumun.game.worldcraftopengl.worlds.generators.utils.Seed;

/**
 * Button used in CreateWorldGui to launch the game with the chosen parameters.
 */
public class CreateWorldButton extends Button {
    private final CreateWorldGui gui;

    public CreateWorldButton(int x, int y, CreateWorldGui gui) {
        super(x, y, gui, "Create");
        this.gui = gui;
    }

    @Override
    public void onClick(Player player) {
        gui.createWorld();
    }
}
