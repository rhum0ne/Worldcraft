package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.title_menu;

import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.CenteredGUI;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.pause_menu.QuitButton;

public class TitleMenuGui extends CenteredGUI {

    public TitleMenuGui() {
        super(500, 500, null);

        this.addText(200, 50, "Title Menu");
        this.addButton(new PlayButton(200, 250, this));
        this.addButton(new QuitButton(200, 350, this));
    }
}
