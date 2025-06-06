package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.title_menu;

import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Button;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.TextComponent;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.worlds_menu.CreateWorldGui;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

public class PlayButton extends Button {


    public PlayButton(int x, int y, Gui container) {
        super(x, y, container, "Jouer");
    }

    @Override
    public void onClick(Player player) {
        player.openGui(new CreateWorldGui());
    }
}
