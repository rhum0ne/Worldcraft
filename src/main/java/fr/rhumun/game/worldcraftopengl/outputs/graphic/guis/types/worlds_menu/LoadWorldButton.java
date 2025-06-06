package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.worlds_menu;

import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Button;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;
import fr.rhumun.game.worldcraftopengl.worlds.WorldInfo;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

public class LoadWorldButton extends Button {

    private final WorldInfo info;

    public LoadWorldButton(int x, int y, Gui container, WorldInfo info) {
        super(x, y, container, info.name());
        this.info = info;
    }

    @Override
    public void onClick(Player player) {
        GAME.startGame(info.seed());
    }
}
