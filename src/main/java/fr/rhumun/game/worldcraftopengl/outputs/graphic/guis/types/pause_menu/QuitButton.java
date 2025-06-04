package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.pause_menu;

import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Button;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.TextComponent;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

public class QuitButton extends Button {

    private final TextComponent label;

    public QuitButton(int x, int y, Gui container) {
        super(x, y, 100, 20, Texture.BUTTON, container);
        this.label = container.addText(x + 30, y + 6, "Quitter");
    }

    @Override
    public void onClick(Player player) {
        GAME.closeGame();
    }

    @Override
    public void update() {

    }
}
