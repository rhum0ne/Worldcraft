package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.worlds_menu;

import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Button;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;
import fr.rhumun.game.worldcraftopengl.worlds.generators.utils.Seed;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

public class LoadWorldButton extends Button {

    private final Seed seed;

    public LoadWorldButton(int x, int y, Gui container, Seed seed) {
        super(x, y, container, seed.toString());
        this.seed = seed;
    }

    @Override
    public void onClick(Player player) {
        GAME.startGame(seed);
    }
}
