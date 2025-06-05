package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.title_menu;

import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Button;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.TextComponent;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

public class PlayButton extends Button {


    public PlayButton(int x, int y, Gui container) {
        super(x, y, 200, 40, Texture.PLAY_BUTTON, container);
        this.addComponent(new TextComponent(35, 6, "Jouer", this));
    }

    @Override
    public void update() {

    }

    @Override
    public void onClick(Player player) {
        GAME.startGame();
    }
}
