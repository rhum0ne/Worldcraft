package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.title_menu;

import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.CenteredGUI;

public class TitleMenuGui extends CenteredGUI {

    public TitleMenuGui() {
        super(960, 540, Texture.WALLPAPER);

        this.addText(0, -200, "WORLDCRAFT");
        this.addButton(new WorldsButton(0, 0, this));
        this.addButton(new QuitButton(0, 80, this));

        this.setAlignCenter(true);
    }
}
