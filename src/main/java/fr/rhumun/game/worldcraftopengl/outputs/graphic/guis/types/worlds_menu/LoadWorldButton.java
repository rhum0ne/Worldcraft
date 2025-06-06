package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.worlds_menu;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Button;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;
import fr.rhumun.game.worldcraftopengl.worlds.generators.utils.Seed;

public class LoadWorldButton extends Button {
    private final Seed seed;

    public LoadWorldButton(int x, int y, Seed seed, String name, Gui container) {
        super(x, y, container, name);
        this.seed = seed;
    }

    @Override
    public void onClick(Player player) {
        Game.GAME.startGame(seed);
    }
}
