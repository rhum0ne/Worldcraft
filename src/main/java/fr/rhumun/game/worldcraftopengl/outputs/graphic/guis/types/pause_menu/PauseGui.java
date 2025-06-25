package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.pause_menu;


import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.CenteredGUI;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Button;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.title_menu.QuitButton;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

public class PauseGui extends CenteredGUI {

    public PauseGui() {
        super(500, 500, null);

        this.addText(0, -200, "Pause Menu");

        this.addButton(new Button(0, 100, 400, 40, this, "Quitter le monde") {
            @Override
            public void onClick(Player player) {
                GAME.quitWorld();
            }
        });

        this.addButton(new Button(0, 40, 400, 40, this, "Parametres") {

            @Override
            public boolean isActive(){
                return false;
            }

            @Override
            public void onClick(Player player) {

            }
        });

        this.addButton(new Button(0, -50, 400, 40, this, "Reprendre le jeu") {
            @Override
            public void onClick(Player player) {
                player.closeInventory();
            }
        });

        this.setAlignCenter(true);
    }
}
