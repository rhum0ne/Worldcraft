package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis;

import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;

import static fr.rhumun.game.worldcraftopengl.Game.GUI_ZOOM;

public class CenteredGUI extends Gui{
    public CenteredGUI(int width, int height, Texture texture) {
        super(960-GUI_ZOOM*width/2, (1080-GUI_ZOOM*height)/2, width, height, texture);
    }
}
