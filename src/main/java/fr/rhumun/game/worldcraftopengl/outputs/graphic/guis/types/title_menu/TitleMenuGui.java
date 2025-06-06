package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.title_menu;

import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.GraphicModule;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.CenteredGUI;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.pause_menu.QuitButton;

public class TitleMenuGui extends CenteredGUI {

    public TitleMenuGui() {
            super(500, 500, Texture.PLANKS);

        this.addText(0, -200, "Title Menu");
        this.addButton(new PlayButton(0, 0, this));
        this.addButton(new QuitButton(0, 80, this));

        this.setAlignCenter(true);
    }
}
