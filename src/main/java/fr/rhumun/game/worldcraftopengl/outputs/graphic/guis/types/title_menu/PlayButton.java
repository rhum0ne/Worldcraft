package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.title_menu;

import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Button;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.TextComponent;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

public class PlayButton extends Button {

    private final TextComponent label;

    public PlayButton(int x, int y, Gui container) {
        super(x, y, 200, 40, Texture.PLAY_BUTTON, container);
        this.label = container.addText(x + 35, y + 6, "Jouer");
    }

    @Override
    public void update() {

    }

    @Override
    public void onClick(Player player) {
        GAME.startGame();
    }
}
