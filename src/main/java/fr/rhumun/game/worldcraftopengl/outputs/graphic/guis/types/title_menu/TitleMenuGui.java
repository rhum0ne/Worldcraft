package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.title_menu;

import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.CenteredGUI;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Image;

public class TitleMenuGui extends CenteredGUI {

    public TitleMenuGui() {
        super(1450, 600, Texture.WALLPAPER);

        this.addComponent(new Image(0, -190, 475, 75, Texture.WORLDCRAFT_TITLE, this));

        this.addButton(new WorldsButton(0, 0, this));
        this.addButton(new QuitButton(0, 80, this));

        this.setAlignCenter(true);
    }
}
