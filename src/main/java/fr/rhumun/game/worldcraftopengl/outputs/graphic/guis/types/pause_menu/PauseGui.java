package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.pause_menu;


import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.CenteredGUI;

public class PauseGui extends CenteredGUI {

    public PauseGui() {
        super(500, 500, null);

        this.addText(200, 50, "Pause Menu");

        this.addButton(new QuitButton(200, 350, this));
    }
}
