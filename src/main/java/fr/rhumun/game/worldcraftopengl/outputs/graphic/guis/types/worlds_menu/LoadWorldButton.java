package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.worlds_menu;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Button;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;
import fr.rhumun.game.worldcraftopengl.worlds.WorldInfo;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

public class LoadWorldButton extends Button {

    private final WorldInfo info;
    public static final int VISIBLE_TOP = -100;
    private static final int VISIBLE_BOTTOM = 220;

    public LoadWorldButton(int x, int y, Gui container, WorldInfo info) {
        super(x, y, 400, 40, container, info.name());
        this.info = info;
    }

    @Override
    public void render() {
        int localY = (getY() - (getContainer().getHeight() / 4 - getHeight() / 2)) / Game.GUI_ZOOM;
        boolean visible = localY > VISIBLE_TOP && localY < VISIBLE_BOTTOM;
        setVisible(visible);
        if (visible) {
            super.render();
        }
    }

    @Override
    public void onClick(Player player) {
        GAME.startGame(info.seed());
    }
}
