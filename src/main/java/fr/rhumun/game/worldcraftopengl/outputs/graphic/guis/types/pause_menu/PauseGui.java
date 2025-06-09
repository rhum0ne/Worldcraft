package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.pause_menu;


import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.CenteredGUI;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.title_menu.QuitButton;

public class PauseGui extends CenteredGUI {

    public PauseGui() {
        super(500, 500, null);

        this.addText(0, -200, "Pause Menu");

        this.addButton(new MenuButton(0, 100, this));
        this.setAlignCenter(true);
    }
}
