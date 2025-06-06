package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.worlds_menu;

import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.CenteredGUI;

public class WorldsGui extends CenteredGUI {

    public WorldsGui() {
        super(500, 500, Texture.PLANKS);

        this.addText(0, -200, "Worlds");
        this.addComponent(new WorldsPanel(0, -150, this));
    }
}
